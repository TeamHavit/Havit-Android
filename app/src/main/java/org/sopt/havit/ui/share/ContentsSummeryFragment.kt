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
import org.sopt.havit.data.remote.CategoryAddRequest
import org.sopt.havit.data.remote.ContentsScrapResponse
import org.sopt.havit.data.remote.CreateContentsRequest
import org.sopt.havit.data.remote.CreateContentsResponse
import org.sopt.havit.databinding.FragmentContentsSummeryBinding
import org.sopt.havit.ui.category.CategoryViewModel
import org.sopt.havit.util.MySharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContentsSummeryFragment : Fragment() {
    private var _binding: FragmentContentsSummeryBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ContentsSummeryFragmentArgs>()
    private lateinit var cateIdString: List<String>
    private  var cateIdNum= arrayListOf<Int>()
    private lateinit var cateIdInt: MutableList<Int>
    private lateinit var responseContents: ContentsSummeryData
    private val categoryViewModel: CategoryViewModel by lazy { CategoryViewModel(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContentsSummeryBinding.inflate(layoutInflater, container, false)


        // 선택된 카테고리 배열 생성
        cateIdString = args.contentsCategoryIds.split(" ")
        //args.contentsCategoryIds.tr

      /*  cateIdString.forEach {
//            var data = it.replace(" ","").toInt()
            cateIdNum.add(it.toInt())
        }*/
       // cateIdNum.addAll(ca)
        Log.d("CateIdString111", cateIdString.toString())
        cateIdInt = MutableList(cateIdString.size -1) { _ -> 0 }


        for (i in 0..cateIdString.size - 2) {
            Log.d("cateIdString", cateIdString[i])
            cateIdInt[i] = ((cateIdString[i]).toInt() + 1)
        }
        for (i in 0..cateIdString.size - 2) {
            Log.d("cateIdInt", cateIdInt[i].toString())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        toolbarClickListener()
        initIntent()
        gerNotificationTime()


    }

    private fun gerNotificationTime() {
        val setTime = MySharedPreference.getNotificationTime(requireContext())
        if (setTime.isEmpty()) {
            Log.d("Notification", "No")
            // 알림 없음
        } else {
            Log.d("Notification Yes", MySharedPreference.getNotificationTime(requireContext()))
            binding.tvSetAlarm.text = setDateFormat(setTime)
            // boolean 변수 true
            // 알림 있음
        }
    }

    private fun setDateFormat(originTime: String): String {
        Log.d("originTime", originTime)
        return "${originTime[2]}${originTime[2]}.${originTime[5]}${originTime[6]}.${originTime[8]}${originTime[9]}" +
                " ${originTime[11]}${originTime[12]}시 ${originTime[14]}${originTime[15]}분에 알림 예정"
    }

    private fun initIntent() {
        val intent = activity?.intent
        val action = intent?.action
        val type = intent?.type

        if ((action == Intent.ACTION_SEND) && (type == "text/plain")) {
            handleSendText(intent)
        }
    }

    private fun handleSendText(intent: Intent): String? {
        val url = intent.getStringExtra(Intent.EXTRA_TEXT)
        binding.tvUrl.text = url
        setContentsCallback(url!!)
        return url
    }

    private fun setContentsCallback(url: String) {
        RetrofitObject.provideHavitApi(MySharedPreference.getXAuthToken(requireContext()))
            .getOgData(url).enqueue(object : Callback<ContentsScrapResponse> {
                override fun onResponse(
                    call: Call<ContentsScrapResponse>,
                    response: Response<ContentsScrapResponse>
                ) {
                    if (response.isSuccessful) {
                        responseContents = response.body()!!.data
                        val response = response.body()
                        Log.d("ContentsSummeryFragment", response.toString())

                        Glide.with(requireContext()).load(response?.data?.ogImage)
                            .into(binding.ivOgImage)

                        if (MySharedPreference.getTitle(requireContext()).isNotEmpty()) {
                            Log.d("shared_title", MySharedPreference.getTitle(requireContext()))
                            binding.tvOgTitle.text = MySharedPreference.getTitle(requireContext())
//                            MySharedPreference.clearTitle(requireContext())
                        } else {
                            binding.tvOgTitle.text = response?.data?.ogTitle
                            Log.d("shared_title", "No Shared Preference data")
                        }
                    }
                }

                override fun onFailure(call: Call<ContentsScrapResponse>, t: Throwable) {
                }
            })
    }

    private fun initListener() {
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

        binding.btnComplete.setOnClickListener {
            Log.d("CateIdString222", cateIdString.toString())
            setCustomToast()
            initNetwork()
            categoryViewModel.shareDelay.observe(viewLifecycleOwner) {
                if(it) {
                    categoryViewModel.setShareDelay(false)
                    MySharedPreference.clearTitle(requireContext())
                    MySharedPreference.clearNotificationTime(requireContext())
                    requireActivity().finish()

                }
            }
//            requireActivity().finish()

        }

        binding.tvSetAlarm.setOnClickListener {
            findNavController().navigate(R.id.action_contentsSummeryFragment_to_setNotificationFragment)
        }
    }

    private fun network(){

    }

    private fun initNetwork() {
        lifecycleScope.launch {
            try {

                var title:String
                var des:String
                var image:String
                var url:String
                var noti:Boolean
                var time:String
                var id:List<Int>

                if (MySharedPreference.getTitle(requireContext()).isNotEmpty()) {
                    Log.d("shared_title", MySharedPreference.getTitle(requireContext()))
                    title = MySharedPreference.getTitle(requireContext())
                } else {
                    title = responseContents.ogTitle
                    Log.d("shared_title", "No Shared Preference data")
                }

                if (MySharedPreference.getNotificationTime(requireContext()).isNotEmpty()){
                    var timestamp = MySharedPreference.getNotificationTime(requireContext())
                    Log.d("timestamp", timestamp)

                    timestamp = timestamp.replace(".","-")


                    time = timestamp.substring(0,16)
                    Log.d("timestamp_substring", timestamp)
                    //time = timestamp.replace(".","-").dropLast(3)
                    //Log.d("timestamp_replace", time.toString())
                   noti = true
                } else {
                    Log.d("timestamp", "not setted")
                    time = ""
                    noti = false
                }

                //Log.d("createContentsRequest", createContentsRequest.toString())

                var body= CreateContentsRequest(title,responseContents.ogDescription,responseContents.ogImage,responseContents.ogUrl,noti,time,
                   arrayListOf<Int>(1,3))
               /* var createContentsRequest = CreateContentsRequest(
                    binding.tvOgTitle.text as String,
                    responseContents.ogDescription,
                    responseContents.ogImage,
                    responseContents.ogUrl,
                    true,
                    "2022-01-23 03:12",
                    cateIdInt
                )*/
                Log.d("createContentsRequest", title.toString())
                Log.d("createContentsRequest", responseContents.ogUrl.toString())
                Log.d("createContentsRequest", responseContents.ogDescription.toString())
                Log.d("createContentsRequest", responseContents.ogImage.toString())
                Log.d("createContentsRequest", noti.toString())
                Log.d("createContentsRequest", time.toString())
                Log.d("createContentsRequest", cateIdNum.toString())

                val response =
                    RetrofitObject.provideHavitApi(MySharedPreference.getXAuthToken(requireContext()))
                        .createContents(body)
                Log.d("createContentsRequest", body.toString())

                categoryViewModel.setShareDelay(true)
                Log.d("CreateContents", response.success.toString())
            } catch (e: Exception) {
                Log.d("Server Failed", e.toString())
                // 서버 통신 실패 시
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

    override fun onDestroyView() {
        super.onDestroyView()
//        MySharedPreference.clearNotificationTime(requireContext())
    }
}