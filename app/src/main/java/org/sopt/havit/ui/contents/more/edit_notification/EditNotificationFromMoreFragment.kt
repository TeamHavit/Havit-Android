package org.sopt.havit.ui.contents.more.edit_notification

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.data.remote.ContentsMoreData
import org.sopt.havit.databinding.FragmentEditNotificationFromMoreBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.contents.more.BottomSheetMoreFragment
import org.sopt.havit.ui.contents.more.edit_notification.EditNotificationFromMoreViewModel.Companion.SUCCESS
import org.sopt.havit.ui.share.notification.AfterTime
import org.sopt.havit.ui.share.notification.PickerFragment
import org.sopt.havit.util.*

@AndroidEntryPoint
class EditNotificationFromMoreFragment :
    BaseBindingFragment<FragmentEditNotificationFromMoreBinding>(R.layout.fragment_edit_notification_from_more),
    OnBackPressedHandler {
    private val viewModel: EditNotificationFromMoreViewModel by activityViewModels()
    private lateinit var bottomSheetDialogFragment: BottomSheetDialogFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()

        viewModel.initProperty(getBundleData() as ContentsMoreData)
        syncTempDataWithFinalData()
        initBottomSheetDialogFragment()
        setButtonSelectedIfOriginDataExist()
        initRadioGroupListener()
        initToolbarListener()
        initDeleteNotiBtn()
    }

    private fun getBundleData(): Parcelable? {
        arguments?.let { return it.getParcelable(BottomSheetMoreFragment.CONTENTS_DATA) }
        throw IllegalArgumentException()
    }

    private fun initBottomSheetDialogFragment() {
        bottomSheetDialogFragment = requireParentFragment() as BottomSheetDialogFragment
    }

    private fun initDeleteNotiBtn() {
        binding.btnDeleteNotification.setOnClickListener {
            showDeleteNotificationWarningDialog()
        }
    }

    private fun syncTempDataWithFinalData() {
        viewModel.syncTempDataWithFinalData()
    }

    private fun setButtonSelectedIfOriginDataExist() {
        if (viewModel.isNotificationSet())
            binding.rgNotificationTime.check(binding.rgNotificationTime.getChildAt(DIRECTLY_SET_TIME).id)
    }

    private fun initRadioGroupListener() {
        with(binding) {
            rgNotificationTime.setOnCheckedChangeListener { _, selectedId ->
                val intervalTime = AfterTime.findClassByButtonId(selectedId)
                if (intervalTime != null && intervalTime.buttonIndex <= DIRECTLY_SET_TIME - 1) {
                    viewModel?.setSelectedIndex(afterTime = intervalTime)
                    viewModel?.setNotificationTimeIndirectly(afterTime = intervalTime)
                }
            }
            rbtnChooseTime.setOnClickListener { showPickerFragment() }
        }
    }

    private fun initToolbarListener() {
        binding.tvComplete.setOnClickListener {
            if (viewModel.isNotificationDataChanged()) {
                viewModel.patchNotification()
                viewModel.isNetworkCorrespondenceEnd.observe(
                    requireActivity(),
                    EventObserver { message ->
                        if (message == SUCCESS) ToastUtil(requireContext()).makeToast(SET_ALARM_TYPE)
                        dismissBottomSheet()
                    }
                )
            } else dismissBottomSheet()
        }
    }

    private fun showPickerFragment() {
        val bottomSheet = PickerFragment()
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    override fun onBackPressed(): Boolean {
        if (viewModel.isNotificationDataChanged()) {
            showEditTitleWarningDialog()
            return true
        }
        return false
    }

    private fun showEditTitleWarningDialog() {
        val dialog = DialogUtil(DialogUtil.CANCEL_SET_NOTIFICATION, ::doAfterConfirm)
        dialog.show(requireActivity().supportFragmentManager, this.javaClass.name)
    }

    private fun showDeleteNotificationWarningDialog() {
        val dialog = DialogUtil(DialogUtil.REMOVE_NOTIFICATION, ::doAfterDeleteConfirm)
        dialog.show(requireActivity().supportFragmentManager, this.javaClass.name)
    }

    private fun doAfterConfirm() {
        dismissBottomSheet()
    }

    private fun doAfterDeleteConfirm() {
        viewModel.deleteNotification()
        dismissBottomSheet()
    }

    private fun dismissBottomSheet() {
        bottomSheetDialogFragment.dismiss()
    }

    companion object {
        const val DIRECTLY_SET_TIME = 4 // 직접 시간 선택
    }
}
