package org.sopt.havit.ui.share

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentSetNotificationBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.util.MySharedPreference
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class SetNotificationFragment :
    BaseBindingFragment<FragmentSetNotificationBinding>(R.layout.fragment_set_notification) {

    private lateinit var notificationTime: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        initRadioGroupListener()
        initToolbarListener()

        return binding.root
    }

    private fun initRadioGroupListener() {
        binding.rgNotificationTime.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.btn_1h -> getNotificationTime(ONE_HOUR)
                R.id.btn_2h -> getNotificationTime(TWO_HOUR)
                R.id.btn_3h -> getNotificationTime(THREE_HOUR)
                R.id.btn_tomorrow -> getNotificationTime(TWENTY_FOUR_HOUR)
                else -> getNotificationTime(ONE_MIN) // DatePicker Dialog 로 변경 예정
            }
        }
    }

    private fun initToolbarListener() {
        binding.icBack.setOnClickListener { findNavController().popBackStack() }
        binding.tvComplete.setOnClickListener {
            MySharedPreference.setNotificationTime(requireContext(), notificationTime)
            findNavController().popBackStack()
        }
    }

    private fun getNotificationTime(idx: Int) {
        val cal = Calendar.getInstance().apply { time = Date() }
        val df: DateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault())
        when (idx) {
            ONE_HOUR -> cal.add(Calendar.HOUR, 1)
            TWO_HOUR -> cal.add(Calendar.HOUR, 2)
            THREE_HOUR -> cal.add(Calendar.HOUR, 3)
            TWENTY_FOUR_HOUR -> cal.add(Calendar.DATE, 1)
            else -> cal.add(Calendar.MINUTE, 1)
        }
        Log.d("After  change : ", df.format(cal.time))
        notificationTime = df.format(cal.time)
    }


    companion object {
        const val ONE_HOUR = 1
        const val TWO_HOUR = 2
        const val THREE_HOUR = 3
        const val TWENTY_FOUR_HOUR = 24
        const val ONE_MIN = -1
    }
}