package org.sopt.havit.ui.share

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.TimePicker
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentPickerBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class PickerFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentPickerBinding? = null
    private val binding get() = _binding!!

    // Round Corner
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
        setBottomSheetHeight()
        setDateByUsingNumberPicker()
        setTimePickerInterval(binding.pickerTime)
    }

    private fun setDateByUsingNumberPicker() {
        val datePicker = binding.pickerDate
        val dateList = arrayOfNulls<String>(100)

        val cal: Calendar = Calendar.getInstance().apply { time = Date() }
        val dayStrMapper =
            mapOf(1 to "일", 2 to "월", 3 to "화", 4 to "수", 5 to "목", 6 to "금", 7 to "토")
        val df: DateFormat = SimpleDateFormat("M월 d일 ", Locale.getDefault())
        dateList[0] = "${df.format(cal.time)}${dayStrMapper[cal.get(Calendar.DAY_OF_WEEK)]}"
        for (i in 1..99) {
            cal.add(Calendar.DATE, 1)
            val todayDay = dayStrMapper[cal.get(Calendar.DAY_OF_WEEK)]
            dateList[i] = "${df.format(cal.time)}$todayDay"
            Log.d("time_string", "${df.format(cal.time)}$todayDay")
        }

        datePicker.displayedValues = dateList
        datePicker.minValue = 0
        datePicker.maxValue = dateList.size - 1
        datePicker.wrapSelectorWheel = false
    }

    private fun setTimePickerInterval(timePicker: TimePicker) {
        try {
            val minutePicker = timePicker.findViewById(
                Resources.getSystem().getIdentifier("minute", "id", "android")
            ) as NumberPicker

            minutePicker.minValue = 0
            minutePicker.maxValue = 60 / 5 - 1
            val displayedValues: MutableList<String> = ArrayList()
            for (i in 0..59 step 5) {
                displayedValues.add(String.format("%02d", i))
            }
            minutePicker.displayedValues = displayedValues.toTypedArray()
        } catch (e: Exception) {
            Log.e(TAG, "Exception: $e")
        }
    }

    private fun setBottomSheetHeight() {
        (dialog as BottomSheetDialog).behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED // 높이 고정
            skipCollapsed = true // HALF_EXPANDED 안되게 설정
        }

        binding.fragmentPicker.layoutParams.height =
            (resources.displayMetrics.heightPixels * 0.45).toInt()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
