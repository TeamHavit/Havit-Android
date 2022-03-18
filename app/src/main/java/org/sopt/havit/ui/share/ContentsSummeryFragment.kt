package org.sopt.havit.ui.share

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
import org.sopt.havit.util.CustomToast
import org.sopt.havit.util.MySharedPreference

class ContentsSummeryFragment : Fragment() {
    private var _binding: FragmentContentsSummeryBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ContentsSummeryFragmentArgs>()
    private lateinit var cateIdString: List<String>
    private lateinit var cateIdInt: MutableList<Int>
    private lateinit var ogData: ContentsSummeryData
    private val categoryViewModel: CategoryViewModel by lazy { CategoryViewModel(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContentsSummeryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setContents()
        getCategoryList()
        initListener()
        toolbarClickListener()
        getNotificationTime()
    }

    private fun getUrl(): String {
        val intent = activity?.intent
        if (isEnterWithShareBtn(intent))                       // 공유하기 버튼으로 진입시
            return intent?.getStringExtra(Intent.EXTRA_TEXT).toString()
        return intent?.getStringExtra("url").toString() // MainActivity FIB 로 진입시
    }

    private fun isEnterWithShareBtn(intent: Intent?): Boolean {
        return (intent?.action == Intent.ACTION_SEND) && (intent.type == "text/plain")
    }


    private fun getCategoryList() {
        // 선택된 카테고리 배열 생성
        cateIdString = args.contentsCategoryIds.split(" ")
        // split 이후 마지막 값에 공백이 들어가는 문제 해결
        cateIdString = cateIdString.subList(0, cateIdString.size - 1)

        // 배열 초기화 및 값 할당
        cateIdInt = MutableList(cateIdString.size) { 0 }
        for (i in cateIdString.indices) cateIdInt[i] = ((cateIdString[i]).toInt())
    }

    private fun getNotificationTime() {
        // 알림을 설정 했다면 tv_set_alarm 값을 알림설정한 시간으로 변경
        val setTime = MySharedPreference.getNotificationTime(requireContext())
        binding.alarm = if (setTime.isNotEmpty()) setDateFormat(setTime) else "알림 설정"
    }

    private fun setDateFormat(originTime: String): String {
        Log.d("originTime", originTime) // 2022.01.25 00:04:54

        // 날짜 (2022.01.25)
        val date =
            "${originTime[2]}${originTime[3]}.${originTime[5]}${originTime[6]}.${originTime[8]}${originTime[9]}"
        // 시 (오후 11시 :: 12시간제 적용)
        val hour = "${originTime[11]}${originTime[12]}".toInt()
        val newHour = when (hour) {
            in 0..12 -> " 오전 ${hour}시 "
            else -> " 오후 ${hour - 12}시 "
        }
        // 분 (3분 :: 자릿수 재졍렬을 위한 이중 형변환 사용)
        val min = "${originTime[14]}${originTime[15]}".toInt().toString() + "분"

        return "$date$newHour$min 알림 예정"
    }

    private fun setContents() {
        val url = getUrl()
        ogData = ContentsSummeryData(ogUrl = url)
        GlobalScope.launch {
            getOgData(url)
            if (MySharedPreference.getTitle(requireContext()).isNotEmpty())
                ogData.ogTitle = MySharedPreference.getTitle(requireContext())
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

        // 제목 수정 (TextView)
        binding.tvOgTitle.setOnClickListener {
            findNavController().navigate(
                ContentsSummeryFragmentDirections.actionContentsSummeryFragmentToEditTitleFragment(
                    binding.tvOgTitle.text.toString()
                )
            )
            MySharedPreference.clearTitle(requireContext())
        }

        // 제목 수정 (ImageView)
        binding.ibEditTitle.setOnClickListener {
            findNavController().navigate(
                ContentsSummeryFragmentDirections.actionContentsSummeryFragmentToEditTitleFragment(
                    binding.tvOgTitle.text.toString()
                )
            )
            MySharedPreference.clearTitle(requireContext())
        }

        // 완료 버튼
        binding.btnComplete.setOnClickListener {
            setCustomToast()
            initNetwork()
            categoryViewModel.shareDelay.observe(viewLifecycleOwner) {
                if (it) {
                    categoryViewModel.setShareDelay(false)
                    MySharedPreference.clearTitle(requireContext())
                    MySharedPreference.clearNotificationTime(requireContext())
                    requireActivity().finish()
                }
            }
        }

        // 알림 설정 ImageView
        binding.tvSetAlarm.setOnClickListener {
            findNavController().navigate(R.id.action_contentsSummeryFragment_to_setNotificationFragment)
        }
    }

    private fun initNetwork() {
        lifecycleScope.launch {
            try {
                // 알림 설정 여부에 따른 notification 과 time 변수 초기화
                val notification: Boolean
                val time: String

                val reservedNotification =
                    MySharedPreference.getNotificationTime(requireContext())

                if (reservedNotification.isEmpty()) {
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
                    ogData.ogDescription,
                    ogData.ogImage,
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
            findNavController().navigate(R.id.action_contentsSummeryFragment_to_selectCategoryFragment)
            MySharedPreference.clearNotificationTime(requireContext())
        }

        binding.icClose.setOnClickListener {
            MySharedPreference.clearTitle(requireContext())
            MySharedPreference.clearNotificationTime(requireContext())
            requireActivity().finish()
        }
    }

    private fun setCustomToast() = CustomToast.contentsAddedToast(requireContext())

}