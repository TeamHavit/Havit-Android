package org.sopt.havit.ui.share

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentSetNotificationBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.util.CalenderUtil.setDateFormatOnRadioBtn
import org.sopt.havit.util.DialogUtil
import org.sopt.havit.util.MySharedPreference
import org.sopt.havit.util.OnBackPressedHandler
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class SetNotificationFragment :
    BaseBindingFragment<FragmentSetNotificationBinding>(R.layout.fragment_set_notification),
    OnBackPressedHandler {
    private val viewModel: ShareViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRadioGroupListener()
        initToolbarListener()
        setTextOnLastRadioBtn()
    }

    private fun setTextOnLastRadioBtn() {
        viewModel.notificationTime.observe(requireActivity()) {
            binding.rbtnChooseTime.text =
                if (viewModel.isTimeDirectlySetFromUser.value == true)
                    setDateFormatOnRadioBtn(requireNotNull(viewModel.notificationTime.value))
                else getString(R.string.choose_time)
        }
    }

    private fun initRadioGroupListener() {
        binding.rgNotificationTime.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbtn_1h -> getNotificationTime(ONE_HOUR)
                R.id.rbtn_2h -> getNotificationTime(TWO_HOUR)
                R.id.rbtn_3h -> getNotificationTime(THREE_HOUR)
                R.id.rbtn_tomorrow -> getNotificationTime(TWENTY_FOUR_HOUR)
                R.id.rbtn_choose_time -> return@setOnCheckedChangeListener
                else -> throw IllegalStateException()
            }
        }
        binding.rbtnChooseTime.setOnClickListener { showPickerFragment() }
    }

    private fun initToolbarListener() {
        binding.icBack.setOnClickListener { onBackClicked() }
        binding.tvComplete.setOnClickListener {
            setNotiTimeOnPrefence()
            goBack()
        }
    }

    private fun setNotiTimeOnPrefence() {
        MySharedPreference.setNotificationTime(
            requireContext(),
            viewModel.notificationTime.value.toString()
        )
    }

    private fun getNotificationTime(idx: Int) {
        val cal = Calendar.getInstance().apply { time = Date() }
        val df: DateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault())
        when (idx) {
            ONE_HOUR -> cal.add(Calendar.HOUR, 1)
            TWO_HOUR -> cal.add(Calendar.HOUR, 2)
            THREE_HOUR -> cal.add(Calendar.HOUR, 3)
            TWENTY_FOUR_HOUR -> cal.add(Calendar.DATE, 1)
            else -> throw IllegalStateException()
        }
        Log.d("After  change : ", df.format(cal.time))
        viewModel.isTimeDirectlySetFromUser(false)
        viewModel.setNotificationTime(df.format(cal.time))
    }

    private fun showPickerFragment() {
        val bottomSheet = PickerFragment()
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun onBackClicked() {
        if (viewModel.notificationTime.value != null) showEditTitleWarningDialog()
        else goBack()
    }

    override fun onBackPressed(): Boolean {
        if (viewModel.notificationTime.value != null) {
            showEditTitleWarningDialog()
            return true
        }
        return false
    }

    private fun showEditTitleWarningDialog() {
        val dialog = DialogUtil(DialogUtil.CANCEL_SET_NOTIFICATION, ::goBack)
        dialog.show(requireActivity().supportFragmentManager, this.javaClass.name)
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
