package org.sopt.havit.util

import android.content.res.Resources
import android.widget.NumberPicker
import android.widget.TimePicker
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object CalenderUtil {

    const val DURATION = 100
    private const val INTERVAL = 5

    val dayStrMapper =
        mapOf(1 to "일", 2 to "월", 3 to "화", 4 to "수", 5 to "목", 6 to "금", 7 to "토")

    val dateWithKorFormatMD: DateFormat
        get() = SimpleDateFormat("M월 d일", Locale.getDefault())

    val dateWithDashFormatMD: DateFormat
        get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val calWithHavitFormat_YMD_HM: DateFormat
        get() = SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault())

    fun setTimePickerInterval(timePicker: TimePicker) {
        val minutePicker = timePicker.findViewById(
            Resources.getSystem().getIdentifier("minute", "id", "android")
        ) as NumberPicker
        minutePicker.minValue = 0
        minutePicker.maxValue = 60 / INTERVAL - 1
        val displayedValues: MutableList<String> = ArrayList()
        for (i in 0..59 step INTERVAL)
            displayedValues.add(String.format("%02d", i))
        minutePicker.displayedValues = displayedValues.toTypedArray()
    }
}
