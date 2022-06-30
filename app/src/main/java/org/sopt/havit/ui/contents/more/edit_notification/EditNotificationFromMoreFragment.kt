package org.sopt.havit.ui.contents.more.edit_notification

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.data.remote.ContentsMoreData
import org.sopt.havit.databinding.FragmentEditNotificationFromMoreBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.contents.more.BottomSheetMoreFragment
import org.sopt.havit.ui.share.notification.AfterTime
import org.sopt.havit.ui.share.notification.PickerFragment
import org.sopt.havit.util.DialogUtil
import org.sopt.havit.util.OnBackPressedHandler

@AndroidEntryPoint
class EditNotificationFromMoreFragment :
    BaseBindingFragment<FragmentEditNotificationFromMoreBinding>(R.layout.fragment_edit_notification_from_more),
    OnBackPressedHandler {
    private val viewModel: EditNotificationFromMoreViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()

        viewModel.initProperty(getBundleData() as ContentsMoreData)
        syncTempDataWithFinalData()
        setButtonSelectedIfOriginDataExist()
        initRadioGroupListener()
        initToolbarListener()
        initDeleteNotiBtn()
    }

    private fun getBundleData(): Parcelable? {
        arguments?.let { return it.getParcelable(BottomSheetMoreFragment.CONTENTS_DATA) }
        throw IllegalArgumentException()
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
        binding.rgNotificationTime.check(
            binding.rgNotificationTime.getChildAt(4).id
        )
    }

    private fun initRadioGroupListener() {
        with(binding) {
            rgNotificationTime.setOnCheckedChangeListener { _, selectedId ->
                val intervalTime = AfterTime.findClassByButtonId(selectedId)
                if (intervalTime != null && intervalTime.buttonIndex <= 3) {
                    viewModel?.setSelectedIndex(afterTime = intervalTime)
                    viewModel?.setNotificationTimeIndirectly(afterTime = intervalTime)
                }
            }
            rbtnChooseTime.setOnClickListener { showPickerFragment() }
        }
    }

    private fun initToolbarListener() {
        binding.icBack.setOnClickListener { onBackClicked() }
        binding.tvComplete.setOnClickListener {
            viewModel.syncFinalDataWithTempData()
            goBack()
        }
    }

    private fun showPickerFragment() {
        val bottomSheet = PickerFragment()
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun onBackClicked() {
        if (viewModel.isNotificationDataChanged())
            showEditTitleWarningDialog()
        else goBack()
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
        goBack()
    }

    private fun doAfterDeleteConfirm() {
        viewModel.deleteNotification()
        goBack()
    }

    private fun goBack() {
        findNavController().popBackStack()
    }
}
