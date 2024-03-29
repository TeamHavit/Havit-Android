package org.sopt.havit.ui.share.notification

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.TimePicker
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentPickerBinding
import org.sopt.havit.ui.contents.more.edit_notification.EditNotificationFromMoreViewModel
import org.sopt.havit.ui.share.ShareViewModel
import org.sopt.havit.util.CalenderUtil.DURATION
import org.sopt.havit.util.CalenderUtil.dateWithDashFormatMD
import org.sopt.havit.util.CalenderUtil.dateWithKorFormatMD
import org.sopt.havit.util.CalenderUtil.dayStrMapper
import org.sopt.havit.util.CalenderUtil.setTimePickerInterval
import org.sopt.havit.util.PAST_TIME_TYPE
import org.sopt.havit.util.ToastUtil
import org.sopt.havit.util.setOnSingleClickListener
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 *  1. 용도
 *      날짜를 선택하는 bottom sheet 를 생성을 위해 만든 CustomPicker 파일.
 *  2. 구현
 *      시간 선택 부분은 안드로이드 내장 TimePicker를 사용했고,
 *      DatePicker 는 Date 객체로부터 불러온 날짜 정보를 Number Picker 에 표시하여 구현함.
 * */

@AndroidEntryPoint
class PickerFragment : BottomSheetDialogFragment() {
    private val viewModel: ShareViewModel by activityViewModels()
    private val modifyViewModel: EditNotificationFromMoreViewModel by activityViewModels()

    private var _binding: FragmentPickerBinding? = null
    private val binding get() = _binding!!
    private lateinit var datePicker: NumberPicker
    private lateinit var timePicker: TimePicker
    private val dateList = arrayOfNulls<String>(DURATION)
    private val calList = arrayOfNulls<Calendar>(DURATION)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPickerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initPicker()
        setBottomSheetHeight()
        setDatePickerData()
        setTimePickerInterval(timePicker)
        setTimePickerIdx()
        setDateText()
        setTimeText()
        initCompleteBtnClick()
        dateScrollListener()
        timeScrollListener()
    }

    private fun initPicker() {
        datePicker = binding.pickerDate
        timePicker = binding.pickerTime
    }

    private fun setDatePickerData() {
        var cal: Calendar
        for (i in 0 until DURATION) {
            cal = Calendar.getInstance()
                .apply { time = Date() }
                .apply { add(Calendar.DATE, i) }
            calList[i] = cal
            val dateOfCal = dayStrMapper[cal.get(Calendar.DAY_OF_WEEK)]
            dateList[i] = "${dateWithKorFormatMD.format(cal.time)} $dateOfCal"
        }
        datePicker.displayedValues = dateList
        datePicker.minValue = 0
        datePicker.maxValue = dateList.size - 1
        datePicker.wrapSelectorWheel = false
    }

    private fun setTimePickerIdx() {
        // 현재시간과 가까운 5분단위 분 설정
        val cal = Calendar.getInstance().apply { time = Date() }
        val df = SimpleDateFormat("m", Locale.getDefault())
        val min = df.format(cal.time).toInt()
        val quotient = min / 5
        val idx = (quotient + 1) % 12
        binding.pickerTime.minute = idx
        if (idx == 0 && idx != quotient) binding.pickerTime.hour += 1
    }

    private fun setDateText() {
        val idx = datePicker.value
        val year = calList[idx]?.get(Calendar.YEAR)
        val monthDay = dateList[idx]
        val displayStr = year.toString() + "년 " + monthDay.toString()
        binding.tvNotiDate.text = displayStr
    }

    private fun setTimeText() {
        val hourDisplay = getFocusedHour()
        val minDisplay = getFocusedMin()
        binding.tvNotiTime.text = hourDisplay + minDisplay
    }

    private fun getFocusedHour() = when (val hour = timePicker.hour) {
        0 -> "오전 12"
        12 -> "오후 12"
        in 0..11 -> "오전 $hour"
        in 13..24 -> "오후 ${hour - 12}"
        else -> throw IllegalStateException()
    } + ":"

    private fun getFocusedMin() = DecimalFormat("00").format(timePicker.minute * 5)

    private fun initCompleteBtnClick() {
        binding.btnComplete.setOnSingleClickListener {
            if (isSelectedTimeAvailable()) {
                viewModel.setNotificationTimeDirectly(getSelectedNotiTime())
                modifyViewModel.setNotificationTimeDirectly(getSelectedNotiTime())
                dismiss()
            } else showPastTimeToast()
        }
    }

    private fun isSelectedTimeAvailable(): Boolean {
        // current time
        val dateCurrent = Calendar.getInstance().apply { time = Date() } // 현재 시간과 날짜
        val hourCurrent = Calendar.getInstance().apply { time = Date() }.time.hours
        val minCurrent = Calendar.getInstance().apply { time = Date() }.time.minutes
        // selected time
        val dateSelected: Calendar = calList[datePicker.value] ?: throw IllegalStateException()
        val hourSelected: Int = timePicker.hour
        val minSelected: Int = timePicker.minute * 5

        return if (dateSelected.after(dateCurrent)) true
        else if (hourSelected > hourCurrent) true
        else if (hourCurrent > hourSelected) false
        else (minCurrent < minSelected)
    }

    private fun showPastTimeToast() {
        ToastUtil(requireContext()).makeToast(PAST_TIME_TYPE)
    }

    private fun getSelectedNotiTime(): String {
        val calSelected = calList[datePicker.value] ?: throw IllegalStateException()
        val date = dateWithDashFormatMD.format(calSelected.time)
        val hour = DecimalFormat("00").format(timePicker.hour)
        val min = getFocusedMin()
        return "$date $hour:$min"
    }

    private fun dateScrollListener() {
        binding.pickerDate.setOnValueChangedListener { _, _, _ -> setDateText() }
    }

    private fun timeScrollListener() {
        binding.pickerTime.setOnTimeChangedListener { _, _, _ -> setTimeText() }
    }

    private fun setBottomSheetHeight() {
        (dialog as BottomSheetDialog).behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED // 높이 고정
            skipCollapsed = true // HALF_EXPANDED 안되게 설정
        }

        binding.fragmentPicker.layoutParams.height =
            (resources.displayMetrics.heightPixels * 0.38).toInt()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
