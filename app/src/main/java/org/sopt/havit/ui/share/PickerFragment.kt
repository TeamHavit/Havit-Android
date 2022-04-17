package org.sopt.havit.ui.share

import android.app.Dialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    }

    private fun setDateByUsingNumberPicker() {
        val datePicker = binding.pickerDate
        val dateList = arrayOfNulls<String>(100)

        val today: Calendar = Calendar.getInstance().apply { time = Date() }
        val dayStrMapperList =
            mapOf(1 to "일", 2 to "월", 3 to "화", 4 to "수", 5 to "목", 6 to "금", 7 to "토")
        val df: DateFormat = SimpleDateFormat("MM월 dd일 ", Locale.getDefault())
        dateList[0] = "${df.format(today.time)}${dayStrMapperList[today.get(Calendar.DAY_OF_WEEK)]}"
        for (i in 1..99) {
            today.add(Calendar.DATE, 1)
            val todayDay = dayStrMapperList[today.get(Calendar.DAY_OF_WEEK)]
            dateList[i] = "${df.format(today.time)}$todayDay"
            Log.d("time_string", "${df.format(today.time)}$todayDay")
        }

        datePicker.displayedValues = dateList
        datePicker.minValue = 0
        datePicker.maxValue = dateList.size - 1
        datePicker.wrapSelectorWheel = false

        Log.d(TAG, "setDateByUsingNumberPicker: $today")
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
        super.onDestroy()
        _binding = null
    }
}
