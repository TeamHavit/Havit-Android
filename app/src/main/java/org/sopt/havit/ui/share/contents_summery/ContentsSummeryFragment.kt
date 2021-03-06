package org.sopt.havit.ui.share.contents_summery

import android.content.Intent
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
import org.sopt.havit.util.ADD_CONTENT_TYPE
import org.sopt.havit.util.MySharedPreference
import org.sopt.havit.util.ToastUtil

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

    private fun getUrl(): String {
        val intent = activity?.intent
        if (isEnterWithShareBtn(intent)) // ???????????? ???????????? ?????????
            return intent?.getStringExtra(Intent.EXTRA_TEXT).toString()
        return intent?.getStringExtra("url").toString() // MainActivity FIB ??? ?????????
    }

    private fun isEnterWithShareBtn(intent: Intent?): Boolean {
        return (intent?.action == Intent.ACTION_SEND) && (intent.type == "text/plain")
    }

    private fun getCategoryList() {
        // ????????? ???????????? ?????? ??????
        cateIdString = args.contentsCategoryIds.split(" ")
        // split ?????? ????????? ?????? ????????? ???????????? ?????? ??????
        cateIdString = cateIdString.subList(0, cateIdString.size - 1)

        // ?????? ????????? ??? ??? ??????
        cateIdInt = MutableList(cateIdString.size) { 0 }
        for (i in cateIdString.indices) cateIdInt[i] = ((cateIdString[i]).toInt())
    }

    private fun setContents() {
        val url = getUrl()
        ogData = ContentsSummeryData(ogUrl = url)
        GlobalScope.launch {
            getOgData(url)
            if (MySharedPreference.getTitle(requireContext()).isNotEmpty())
                ogData.ogTitle = MySharedPreference.getTitle(requireContext())
            if (ogData.ogTitle == "") ogData.ogTitle = "?????? ?????? ?????????"
            binding.contentsSummeryData = ogData
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

        // ?????? ?????? (TextView)
        binding.tvOgTitle.setOnClickListener {
            findNavController().navigate(
                ContentsSummeryFragmentDirections.actionContentsSummeryFragmentToEditTitleFragment(
                    binding.tvOgTitle.text.toString()
                )
            )
        }

        // ?????? ?????? (ImageView)
        binding.ibEditTitle.setOnClickListener {
            findNavController().navigate(
                ContentsSummeryFragmentDirections.actionContentsSummeryFragmentToEditTitleFragment(
                    binding.tvOgTitle.text.toString()
                )
            )
        }

        // ?????? ?????? ImageView
        binding.tvSetAlarm.setOnClickListener {
            findNavController().navigate(R.id.action_contentsSummeryFragment_to_setNotificationFragment)
        }

        // ?????? ??????
        binding.btnComplete.setOnClickListener {
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
                // ?????? ?????? ????????? ?????? notification ??? time ?????? ?????????
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
            MySharedPreference.clearTitle(requireContext())
            requireActivity().finish()
        }
    }

    private fun setCustomToast() = ToastUtil(requireContext()).makeToast(ADD_CONTENT_TYPE)
}
