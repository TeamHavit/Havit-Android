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
import org.sopt.havit.util.CalenderUtil.dateAndTimeWithDotFormatMD
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

        initRadioGroupListener()
        initLastRbtnColor()
        initToolbarListener()
    }

    private fun initRadioGroupListener() {
        with(binding) {
            rgNotificationTime.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.rbtn_1h -> getNotificationTime(ONE_HOUR)
                    R.id.rbtn_2h -> getNotificationTime(TWO_HOUR)
                    R.id.rbtn_3h -> getNotificationTime(THREE_HOUR)
                    R.id.rbtn_tomorrow -> getNotificationTime(TWENTY_FOUR_HOUR)
                    else -> return@setOnCheckedChangeListener
                }
            }
            rbtnChooseTime.setOnClickListener { showPickerFragment() }
        }
    }

    private fun initLastRbtnColor() {
        if (viewModel.isTimeDirectlySetFromUser.value == true)
            binding.rbtnChooseTime.isChecked = true
    }

    private fun initToolbarListener() {
        binding.icBack.setOnClickListener { onBackClicked() }
        binding.tvComplete.setOnClickListener {
            goBack()
        }
    }

    private fun getNotificationTime(idx: Int): String {
        val cal = Calendar.getInstance().apply { time = Date() }
        when (idx) {
            ONE_HOUR -> cal.add(Calendar.HOUR, 1)
            TWO_HOUR -> cal.add(Calendar.HOUR, 2)
            THREE_HOUR -> cal.add(Calendar.HOUR, 3)
            TWENTY_FOUR_HOUR -> cal.add(Calendar.DATE, 1)
            else -> throw IllegalStateException()
        }
        viewModel.isTimeDirectlySetFromUser(false)
        viewModel.setNotificationTime(dateAndTimeWithDotFormatMD.format(cal.time))
        return dateAndTimeWithDotFormatMD.format(cal.time)
    }

    private fun showPickerFragment() {
        val bottomSheet = PickerFragment()
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun onBackClicked() {
        if (viewModel.notificationTime.value != null) {
            showEditTitleWarningDialog()
        } else goBack()
    }

    override fun onBackPressed(): Boolean {
        if (viewModel.notificationTime.value != null) {
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
        viewModel.setNotificationTime(null)
        goBack()
    }

    private fun goBack() {
        findNavController().popBackStack()
    }

    companion object {
        const val ONE_HOUR = 1
        const val TWO_HOUR = 2
        const val THREE_HOUR = 3
        const val TWENTY_FOUR_HOUR = 24
    }
}
