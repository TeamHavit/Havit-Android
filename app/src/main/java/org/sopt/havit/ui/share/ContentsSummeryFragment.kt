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
import androidx.lifecycle.whenCreated
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import org.sopt.havit.ContentsFinalData
import org.sopt.havit.R
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        toolbarClickListener()
        initIntent()

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
                        val response = response.body()
                        Log.d("ContentsSummeryFragment", response.toString())

                        Glide.with(requireContext()).load(response?.data?.ogImage)
                            .into(binding.ivOgImage)

                        if (MySharedPreference.getTitle(requireContext()).isNotEmpty()) {
                            Log.d("shared_title", MySharedPreference.getTitle(requireContext()))
                            binding.tvOgTitle.text = MySharedPreference.getTitle(requireContext())
                            MySharedPreference.clearTitle(requireContext())
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