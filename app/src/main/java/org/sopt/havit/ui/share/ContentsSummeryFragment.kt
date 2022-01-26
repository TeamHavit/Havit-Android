package org.sopt.havit.ui.share

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import org.sopt.havit.R
import org.sopt.havit.data.ContentsSummeryData
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.ContentsScrapResponse
import org.sopt.havit.data.remote.CreateContentsRequest
import org.sopt.havit.databinding.FragmentContentsSummeryBinding
import org.sopt.havit.ui.category.CategoryViewModel
import org.sopt.havit.util.CallbackUtil.enqueueUtil
import org.sopt.havit.util.MySharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContentsSummeryFragment : Fragment() {
    private var _binding: FragmentContentsSummeryBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ContentsSummeryFragmentArgs>()
    private lateinit var cateIdString: List<String>
    private var cateIdNum = arrayListOf<Int>()
    private lateinit var url: String
    private lateinit var cateIdInt: MutableList<Int>
    private lateinit var responseContents: ContentsSummeryData
    private val categoryViewModel: CategoryViewModel by lazy { CategoryViewModel(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContentsSummeryBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUrl()
        setUrl(url)
        setContents(url)        // get()을 통해 정보 받아와서 UI 띄움
        getCategoryList()
        initListener()
        toolbarClickListener()
        gerNotificationTime()
    }

    // Initialize url on Global var url
    private fun getUrl() {
        val intent = activity?.intent
        if ((intent?.action == Intent.ACTION_SEND) && (intent.type == "text/plain"))
            url = intent.getStringExtra(Intent.EXTRA_TEXT).toString()
    }

    private fun setUrl(url: String) {
        binding.tvUrl.text = url
    }

    private fun getCategoryList() {

        // 선택된 카테고리 배열 생성
        cateIdString = args.contentsCategoryIds.split(" ")
        cateIdString =
            cateIdString.subList(0, cateIdString.size - 1)  // split 이후 마지막 값에 공백이 들어가는 문제 해결
        Log.d("CateIdString", cateIdString.toString())

        cateIdInt = MutableList(cateIdString.size) { _ -> 0 }
        for (i in cateIdString.indices) {
            cateIdInt[i] = ((cateIdString[i]).toInt() + 1)
        }
        Log.d("CateIdInt", cateIdInt.toString())
    }

    private fun gerNotificationTime() {
        // 알림을 설정 했다면 tv_set_alarm 값을 알림설정한 시간으로 변경
        val setTime = MySharedPreference.getNotificationTime(requireContext())
        if (setTime.isNotEmpty())
            binding.tvSetAlarm.text = setDateFormat(setTime)
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

        return date + newHour + min
    }

    private fun setContents(url: String) {
        val call = RetrofitObject.provideHavitApi(
            MySharedPreference.getXAuthToken(requireContext())
        ).getOgData(url)

        call.enqueueUtil(
            onSuccess = {
                responseContents = it.data

                // OgImage 설정
                Glide.with(requireContext()).load(it.data.ogImage).into(binding.ivOgImage)
                // OgTitle 설정
                if (MySharedPreference.getTitle(requireContext()).isEmpty())
                    binding.tvOgTitle.text = it.data.ogTitle
                else   // 제목 수정 시
                    binding.tvOgTitle.text = MySharedPreference.getTitle(requireContext())
            }
        )
    }

    private fun initListener() {

        // 제목 수정 (TextView & ImageButton)
        binding.tvOgTitle.setOnClickListener {
            findNavController().navigate(
                ContentsSummeryFragmentDirections.actionContentsSummeryFragmentToEditTitleFragment(
                    binding.tvOgTitle.text.toString()
                )
            )
            MySharedPreference.clearTitle(requireContext())
        }
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

                if (MySharedPreference.getNotificationTime(requireContext()).isEmpty()) {
                    time = ""
                    notification = false
                } else {
                    time = MySharedPreference.getNotificationTime(requireContext())
                        .replace(".", "-")
                        .substring(0, 16)
                    notification = true
                }


                val createContentsRequest = CreateContentsRequest(
                    if (MySharedPreference.getTitle(requireContext()).isNotEmpty()) {   // title
                        MySharedPreference.getTitle(requireContext())
                    } else {
                        responseContents.ogTitle
                    },
                    responseContents.ogDescription,
                    responseContents.ogImage,
                    responseContents.ogUrl,
                    notification,
                    time,
                    cateIdInt
                )
                Log.d("CreateContentsBody", createContentsRequest.toString())


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

    private fun setCustomToast() {
        // TODO: snack bar 로 custom (release)
        val toast = Toast(requireContext())
        toast.setGravity(Gravity.TOP, 0, 54)
        // set text
        val view = layoutInflater.inflate(R.layout.toast_contents_added, null)
        toast.view = view
        toast.show()
    }
}