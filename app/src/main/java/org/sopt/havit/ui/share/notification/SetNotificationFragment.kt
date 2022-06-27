package org.sopt.havit.ui.share.notification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentSetNotificationBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.share.ShareViewModel
import org.sopt.havit.ui.share.notification.AfterTime.Companion.findClassByButtonId
import org.sopt.havit.ui.share.notification.SetNotificationFragment.Companion.ONE
import org.sopt.havit.ui.share.notification.SetNotificationFragment.Companion.THREE
import org.sopt.havit.ui.share.notification.SetNotificationFragment.Companion.TWENTY_FOUR
import org.sopt.havit.ui.share.notification.SetNotificationFragment.Companion.TWO
import org.sopt.havit.util.DialogUtil
import org.sopt.havit.util.OnBackPressedHandler
import java.util.*

@AndroidEntryPoint
class SetNotificationFragment :
    BaseBindingFragment<FragmentSetNotificationBinding>(R.layout.fragment_set_notification),
    OnBackPressedHandler {
    private val viewModel: ShareViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()

        syncTempDataWithFinalData()
        setButtonSelectedIfOriginDataExist()
        initRadioGroupListener()
        initToolbarListener()
    }

    private fun syncTempDataWithFinalData() {
        viewModel.syncTempDataWithFinalData()
    }

    private fun setButtonSelectedIfOriginDataExist() {
        binding.rgNotificationTime.check(
            binding.rgNotificationTime.getChildAt(viewModel.tempIndex.value ?: return).id
        )
    }

    private fun initRadioGroupListener() {
        with(binding) {
            rgNotificationTime.setOnCheckedChangeListener { _, selectedId ->
                val intervalTime = findClassByButtonId(selectedId)
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

    private fun doAfterConfirm() {
        goBack()
    }

    private fun goBack() {
        findNavController().popBackStack()
    }

    companion object {
        const val ONE = 1
        const val TWO = 2
        const val THREE = 3
        const val TWENTY_FOUR = 24
    }
}

enum class AfterTime(
    val type: Int?,
    val interval: Int?,
    val buttonId: Int,
    val buttonIndex: Int
) {
    ONE_HOUR(Calendar.HOUR, ONE, R.id.rbtn_1h, 0),
    TWO_HOUR(Calendar.HOUR, TWO, R.id.rbtn_2h, 1),
    THREE_HOUR(Calendar.HOUR, THREE, R.id.rbtn_3h, 2),
    TWENTY_FOUR_HOUR(Calendar.HOUR, TWENTY_FOUR, R.id.rbtn_tomorrow, 3),
    DIRECTLY_SELECT(null, null, R.id.rbtn_choose_time, 4);

    companion object {
        fun findClassByButtonId(buttonId: Int): AfterTime? =
            values().find { it.buttonId == buttonId }
    }
}
