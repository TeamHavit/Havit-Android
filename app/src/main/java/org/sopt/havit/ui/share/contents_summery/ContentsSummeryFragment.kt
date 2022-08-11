package org.sopt.havit.ui.share.contents_summery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.sopt.havit.R
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.ContentsSummeryData
import org.sopt.havit.data.remote.CreateContentsRequest
import org.sopt.havit.databinding.FragmentContentsSummeryBinding
import org.sopt.havit.ui.category.CategoryViewModel
import org.sopt.havit.ui.share.ShareViewModel
import org.sopt.havit.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

@AndroidEntryPoint
class ContentsSummeryFragment : Fragment() {
    private val viewModel: ShareViewModel by activityViewModels()

    private var _binding: FragmentContentsSummeryBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ContentsSummeryFragmentArgs>()
    private lateinit var cateIdString: List<String>
    private lateinit var cateIdInt: MutableList<Int>
    private lateinit var ogData: ContentsSummeryData
    private val categoryViewModel: CategoryViewModel by lazy { CategoryViewModel(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentsSummeryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setContents()
        getCategoryList()
        initListener()
        toolbarClickListener()
    }

//    private fun getUrl(): String {
//        val intent = activity?.intent
//        if (isEnterWithShareBtn(intent)) // 공유하기 버튼으로 진입시
//            return intent?.getStringExtra(Intent.EXTRA_TEXT).toString()
//        return intent?.getStringExtra("url").toString() // MainActivity FIB 로 진입시
//    }
//
//    private fun isEnterWithShareBtn(intent: Intent?): Boolean {
//        return (intent?.action == Intent.ACTION_SEND) && (intent.type == "text/plain")
//    }

    private fun getCategoryList() {
        // 선택된 카테고리 배열 생성
        cateIdString = args.contentsCategoryIds.split(" ")
        // split 이후 마지막 값에 공백이 들어가는 문제 해결
        cateIdString = cateIdString.subList(0, cateIdString.size - 1)

        // 배열 초기화 및 값 할당
        cateIdInt = MutableList(cateIdString.size) { 0 }
        for (i in cateIdString.indices) cateIdInt[i] = ((cateIdString[i]).toInt())
    }

    private fun setContents() {
        val url = viewModel.url.value ?: return
        ogData = ContentsSummeryData(ogUrl = url)
        GlobalScope.launch {
            getOgData(url)
            if (MySharedPreference.getTitle(requireContext()).isNotEmpty())
                ogData.ogTitle = MySharedPreference.getTitle(requireContext())
            if (ogData.ogTitle == "") ogData.ogTitle = "제목 없는 콘텐츠"
            binding.contentsSummeryData = ogData
        }
    }

    private fun extractUrl(content: String?): String {
        return try {
            val regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"
            val p: Pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
            val m: Matcher = p.matcher(content)
            if (m.find()) m.group() else ""
        } catch (e: java.lang.Exception) {
            ""
        }
    }

    private suspend fun getOgData(url: String) {
        GlobalScope.launch {
            kotlin.runCatching {
                val doc: Document = Jsoup.connect(url).get()
                val ogTags = doc.select("meta[property^=og:]")
                ogData.apply {
                    if (ogTags.size == 0) return@apply
                    ogTags.forEachIndexed { index, _ ->
                        val tag = ogTags[index]
                        when (tag.attr("property")) {
                            "og:image" -> this.ogImage = tag.attr("content")
                            "og:description" -> this.ogDescription = tag.attr("content")
                            "og:title" -> this.ogTitle = tag.attr("content")
                        }
                    }
                }
            }
        }.join()
    }

    private fun initListener() {

        // 제목 수정 (TextView)
        binding.tvOgTitle.setOnClickListener {
            findNavController().navigate(
                ContentsSummeryFragmentDirections.actionContentsSummeryFragmentToEditTitleFragment(
                    binding.tvOgTitle.text.toString()
                )
            )
        }

        // 제목 수정 (ImageView)
        binding.ibEditTitle.setOnClickListener {
            findNavController().navigate(
                ContentsSummeryFragmentDirections.actionContentsSummeryFragmentToEditTitleFragment(
                    binding.tvOgTitle.text.toString()
                )
            )
        }

        // 알림 설정 ImageView
        binding.tvSetAlarm.setOnClickListener {
            findNavController().navigate(R.id.action_contentsSummeryFragment_to_setNotificationFragment)
        }

        // 완료 버튼
        binding.btnComplete.setOnSinglePostClickListener {
            initNetwork()
            categoryViewModel.shareDelay.observe(viewLifecycleOwner) {
                if (it) {
                    categoryViewModel.setShareDelay(false)
                    requireActivity().finish()
                    setCustomToast()
                }
            }
        }
    }

    private fun initNetwork() {
        lifecycleScope.launch {
            try {
                // 알림 설정 여부에 따른 notification 과 time 변수 초기화
                val notification: Boolean
                val time: String

                val reservedNotification =
                    viewModel.finalNotificationTime.value

                if (reservedNotification == null) {
                    time = ""
                    notification = false
                } else {
                    time = reservedNotification
                        .replace(".", "-")
                        .substring(0, 16)
                    notification = true
                }

                val createContentsRequest = CreateContentsRequest(
                    ogData.ogTitle,
                    ogData.ogUrl,
                    isNotified = notification,
                    notificationTime = time,
                    categoryIds = cateIdInt
                )

                RetrofitObject.provideHavitApi(MySharedPreference.getXAuthToken(requireContext()))
                    .createContents(createContentsRequest)
                categoryViewModel.setShareDelay(true)
            } catch (e: Exception) {
                Log.d("Server Failed", e.toString())
            }
        }
    }

    private fun toolbarClickListener() {
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.icClose.setOnClickListener {
            val dialog = DialogUtil(DialogUtil.CANCEL_SAVE_CONTENTS, ::finishSavingContents)
            dialog.show(parentFragmentManager, this.javaClass.name)
        }
    }

    private fun finishSavingContents() {
        MySharedPreference.clearTitle(requireContext())
        requireActivity().finish()
    }

    private fun setCustomToast() = ToastUtil(requireContext()).makeToast(ADD_CONTENT_TYPE)
}
