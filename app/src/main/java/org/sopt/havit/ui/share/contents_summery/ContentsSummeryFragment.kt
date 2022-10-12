package org.sopt.havit.ui.share.contents_summery

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentContentsSummeryBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.category.CategoryViewModel
import org.sopt.havit.ui.share.ShareViewModel
import org.sopt.havit.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ContentsSummeryFragment :
    BaseBindingFragment<FragmentContentsSummeryBinding>(R.layout.fragment_contents_summery) {
    private val viewModel: ShareViewModel by activityViewModels()

    @Inject
    private lateinit var preference: HavitSharedPreference

    private val categoryViewModel: CategoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setContents()
        initListener()
        toolbarClickListener()
    }

    private fun setContents() {
        viewModel.setCrawlingContents()
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
                    setCustomToast()
                    requireActivity().finish()
                }
            }
        }
    }

    private fun initNetwork() {
        // todo 커밋하기 위해 주석 처리. 바로 수정예정
//        lifecycleScope.launch {
//            try {
//                // 알림 설정 여부에 따른 notification 과 time 변수 초기화
//                val notification: Boolean
//                val time: String
//
//                val reservedNotification =
//                    viewModel.finalNotificationTime.value
//
//                if (reservedNotification == null) {
//                    time = ""
//                    notification = false
//                } else {
//                    time = reservedNotification
//                        .replace(".", "-")
//                        .substring(0, 16)
//                    notification = true
//                }
//
//                val createContentsRequest = CreateContentsRequest(
//                    title = ogData.ogTitle,
//                    url = ogData.ogUrl,
//                    description = ogData.ogDescription,
//                    imageUrl = ogData.ogImage ?: "",
//                    isNotified = notification,
//                    notificationTime = time,
//                    categoryIds = viewModel.selectedCategoryId.value ?: return@launch
//                )
//
//                Log.d(TAG, "initNetwork: $createContentsRequest")
//
//                RetrofitObject.provideHavitApi(MySharedPreference.getXAuthToken())
//                    .createContents(createContentsRequest)
//                categoryViewModel.setShareDelay(true)
//            } catch (e: Exception) {
//                Log.d("Server Failed", e.toString())
//            }
//        }
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
        preference.clearTitle()
        requireActivity().finish()
    }

    private fun setCustomToast() = ToastUtil(requireContext()).makeToast(ADD_CONTENT_TYPE)
}
