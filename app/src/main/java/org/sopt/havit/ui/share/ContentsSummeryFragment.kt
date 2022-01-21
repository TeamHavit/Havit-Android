package org.sopt.havit.ui.share

import android.content.Context
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
import androidx.lifecycle.whenCreated
import androidx.lifecycle.whenStarted
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.launch
import org.sopt.havit.ContentsFinalData
import org.sopt.havit.R
import org.sopt.havit.data.ContentsTitleImageData
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.ContentsScrapResponse
import org.sopt.havit.databinding.FragmentContentsSummeryBinding
import org.sopt.havit.util.MySharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContentsSummeryFragment : Fragment() {
    private var _binding: FragmentContentsSummeryBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ContentsSummeryFragmentArgs>()
    lateinit var contentsFinalData: ContentsFinalData
    private var isSeverConnected = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContentsSummeryBinding.inflate(layoutInflater, container, false)

        // TODO : imageview 모서리 둥글게
        binding.ivOgImage.clipToOutline = true

        MySharedPreference.clearTitle(requireContext())

        initListener()
        toolbarClickListener()
        initIntent()

        if (MySharedPreference.getTitle(requireContext()).isEmpty()) {
            Log.d("shared_title", MySharedPreference.getTitle(requireContext()))
            binding.tvOgTitle.text = MySharedPreference.getTitle(requireContext())
        }

        return binding.root
    }

    private fun setContents(url: String) {
        lifecycleScope.launch {
            whenCreated {
                try {
                    Log.d("server", "log")
                    // 서버 통신
                    val response =
                        RetrofitObject.provideHavitApi(
                            MySharedPreference
                                .getXAuthToken(requireContext())
                        ).getOgData(url)


//                contentsFinalData = ContentsFinalData(
//                    response.data.ogTitle,
//                    response.data.ogDescription,
//                    response.data.ogImage,
//                    response.data.ogUrl,
//                    false,
//                    "",
//                    getCategoryArray() as MutableList<Int>
//                )

//                    Log.d("ContentsSummeryFragment", response.toString())
//
//                    val contentsTitleImageData =
//                        ContentsTitleImageData(response.data.ogTitle, response.data.ogImage)
//                    binding.contentsTitleImageData = contentsTitleImageData
//
//                    if (MySharedPreference.getTitle(requireContext()) == "") {
//                        contentsFinalData.title = response.data.ogTitle // 이거 지우고 바로 밑에 데이터 클래스로 넣기
//                    } else {
//                        contentsFinalData.title = MySharedPreference.getTitle(requireContext())
//                    }

                } catch (e: Exception) {
                    Log.d("ContentsSummeryFragment", e.toString())
                    // 서버 통신 실패 시
                }
            }

        }
    }

    private fun getCategoryArray(): List<Int> {
        val categoryIds = args.contentsCategoryIds
        val arrString = categoryIds.split(" ")
        Log.d("split category : ", arrString.toString())
        val arrInt: MutableList<Int> = List(arrString.size) { _ -> 0 }.toMutableList()
        for (i in arrString.indices) {
            arrInt[i] = arrString[i].toInt()
        }
        return arrInt
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
//        setContents(url!!)
        setContentsCallback(url!!)
        return url
    }

    private fun setContentsCallback(url: String) {
        RetrofitObject.provideHavitApi(MySharedPreference.getXAuthToken(requireContext()))
            .getOgData(url).enqueue(object : Callback<ContentsScrapResponse>{
                override fun onResponse(
                    call: Call<ContentsScrapResponse>,
                    response: Response<ContentsScrapResponse>
                ) {
                    if (response.isSuccessful){
                        val response = response.body()
                        Log.d("ContentsSummeryFragment", response.toString())

                        val contentsTitleImageData =
                            ContentsTitleImageData(response!!.data.ogTitle, response.data.ogImage)
                        binding.contentsTitleImageData = contentsTitleImageData

                        if (MySharedPreference.getTitle(requireContext()).isEmpty()) {
                            Log.d("shared1", MySharedPreference.getTitle(requireContext()) )
                            binding.tvOgTitle.text = response.data.ogTitle // 이거 지우고 바로 밑에 데이터 클래스로 넣기
                        } else {
                            Log.d("shared2", MySharedPreference.getTitle(requireContext()) )
                            binding.tvOgTitle.text= MySharedPreference.getTitle(requireContext())
                        }
                    }
                }

                override fun onFailure(call: Call<ContentsScrapResponse>, t: Throwable) {
                    TODO("Not yet implemented")
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
            setCustomToast()
            //서버연동
            requireActivity().finish()
        }


        binding.tvSetAlarm.setOnClickListener {
            findNavController().navigate(R.id.action_contentsSummeryFragment_to_setNotificationFragment)
        }
    }

    private fun toolbarClickListener() {
        binding.icBack.setOnClickListener {
            findNavController().navigate(R.id.action_contentsSummeryFragment_to_selectCategoryFragment)
        }

        binding.icClose.setOnClickListener {
            MySharedPreference.clearTitle(requireContext())
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