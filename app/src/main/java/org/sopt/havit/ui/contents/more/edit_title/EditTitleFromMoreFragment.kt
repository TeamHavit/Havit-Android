package org.sopt.havit.ui.contents.more.edit_title

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.data.remote.ContentsMoreData
import org.sopt.havit.databinding.FragmentEditTitleFromMoreBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.contents.more.BottomSheetMoreFragment.Companion.CONTENTS_DATA
import org.sopt.havit.ui.contents.more.edit_notification.EditNotificationFromMoreViewModel.Companion.FAIL
import org.sopt.havit.ui.contents.more.edit_title.EditTitleFromMoreViewModel.Companion.SUCCESS
import org.sopt.havit.util.DialogUtil
import org.sopt.havit.util.ERROR_OCCUR_TYPE
import org.sopt.havit.util.EventObserver
import org.sopt.havit.util.KeyBoardUtil
import org.sopt.havit.util.MODIFY_TITLE_COMPLETE_TYPE
import org.sopt.havit.util.OnBackPressedHandler
import org.sopt.havit.util.ToastUtil
import org.sopt.havit.util.setOnSingleClickListener

@AndroidEntryPoint
class EditTitleFromMoreFragment :
    BaseBindingFragment<FragmentEditTitleFromMoreBinding>(R.layout.fragment_edit_title_from_more),
    OnBackPressedHandler {
    private val viewModel: EditTitleFromMoreViewModel by viewModels()
    private lateinit var bottomSheetDialogFragment: BottomSheetDialogFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.initProperty(getBundleData() as ContentsMoreData)
        initCompleteBtnClick()
        initBottomSheetDialogFragment()
        setKeyBoardUp()
        setCursor()
        onCloseClicked()
        adjustWarningMessagePositionToKeyBoard()
        showWhitespaceWarningMessage()
    }

    private fun getBundleData(): Parcelable? {
        arguments?.let { return it.getParcelable(CONTENTS_DATA) }
        throw IllegalArgumentException()
    }

    private fun initBottomSheetDialogFragment() {
        bottomSheetDialogFragment = requireParentFragment() as BottomSheetDialogFragment
    }

    private fun initCompleteBtnClick() {
        binding.tvComplete.setOnSingleClickListener {
            if (viewModel.isTitleModified()) {
                viewModel.patchNewTitle()
                viewModel.isNetworkCorrespondenceEnd.observe(
                    viewLifecycleOwner,
                    EventObserver { message ->
                        if (message == SUCCESS)
                            ToastUtil(requireContext()).makeToast(MODIFY_TITLE_COMPLETE_TYPE)
                        if (message == FAIL) ToastUtil(requireContext()).makeToast(ERROR_OCCUR_TYPE)
                        dismissBottomSheet()
                    }
                )
            } else dismissBottomSheet()
        }
    }

    private fun setKeyBoardUp() {
        bottomSheetDialogFragment.dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    private fun setCursor() {
        binding.etTitle.post {
            if (viewModel.originTitle.value != null) {
                binding.etTitle.requestFocus()
                binding.etTitle.setSelection(
                    viewModel.originTitle.value?.length!!
                )
            }
        }
    }

    private fun adjustWarningMessagePositionToKeyBoard() {
        // 공백 warning 위/아래 움직이게
        this.view?.let { KeyBoardUtil.setUpAsSoftKeyboard(it) }
    }

    private fun showWhitespaceWarningMessage() {
        binding.etTitle.addTextChangedListener {
            val title = it.toString()
            if (title.isNotEmpty() && title.isBlank()) { // 공백만 있는 경우
                binding.clWarningWhitespace.visibility = View.VISIBLE
            } else {
                binding.clWarningWhitespace.visibility = View.GONE
            }
        }
    }

    private fun onCloseClicked() {
        binding.icClose.setOnSingleClickListener {
            if (viewModel.isTitleModified()) showEditTitleWarningDialog()
            else dismissBottomSheet()
        }
    }

    override fun onBackPressed(): Boolean {
        if (viewModel.isTitleModified()) {
            showEditTitleWarningDialog()
            return true
        }
        return false
    }

    private fun showEditTitleWarningDialog() {
        val dialog = DialogUtil(DialogUtil.CANCEL_EDIT_TITLE, ::dismissBottomSheet)
        dialog.show(requireActivity().supportFragmentManager, this.javaClass.name)
    }

    private fun dismissBottomSheet() {
        bottomSheetDialogFragment.dismiss()
    }
}