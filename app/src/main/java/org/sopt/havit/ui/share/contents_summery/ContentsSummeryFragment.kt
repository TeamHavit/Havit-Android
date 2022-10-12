package org.sopt.havit.ui.share.contents_summery

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentContentsSummeryBinding
import org.sopt.havit.domain.model.NetworkStatus
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.share.ShareViewModel
import org.sopt.havit.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ContentsSummeryFragment :
    BaseBindingFragment<FragmentContentsSummeryBinding>(R.layout.fragment_contents_summery) {
    private val viewModel: ShareViewModel by activityViewModels()

    @Inject
    lateinit var preference: HavitSharedPreference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setContents()
        initListener()
        toolbarClickListener()
        onClickRefreshButtonOnNetworkError()
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
            saveContents()
            viewModel.saveContentsViewState.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkStatus.Success -> {
                        setCustomToast()
                        requireActivity().finish()
                    }
                    else -> return@observe
                }
            }
        }
    }

    private fun saveContents() {
        viewModel.saveContents()
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

    private fun onClickRefreshButtonOnNetworkError() {
        binding.networkErrorLayout.ivRefresh.setOnSingleClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotation_refresh))
            saveContents()
        }
    }

    private fun setCustomToast() = ToastUtil(requireContext()).makeToast(ADD_CONTENT_TYPE)
}
