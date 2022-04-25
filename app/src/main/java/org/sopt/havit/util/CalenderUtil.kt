package org.sopt.havit.util

import android.content.res.Resources
import android.widget.NumberPicker
import android.widget.TimePicker
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object CalenderUtil {

    const val DURATION = 100
    const val INTERVAL = 5

    val dayStrMapper =
        mapOf(1 to "일", 2 to "월", 3 to "화", 4 to "수", 5 to "목", 6 to "금", 7 to "토")

    val dateFormatMD: DateFormat
        get() = SimpleDateFormat("M월 d일 ", Locale.getDefault())

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

//    val today: Calendar = Calendar.getInstance().apply { time = Date() }
//
//    // 무슨 요일인지 가져오기
//    private fun getDayOfWeekList(duration: Int = DURATION): Array<String?> { // ex. 월
//        val dayOfWeekList = arrayOfNulls<String>(duration)
//        val calList = getCalList()
//        for (i in 0 until duration)
//            dayOfWeekList[i] = dayStrMapper[calList[i]?.get(Calendar.DAY_OF_WEEK)]
//
//        return dayOfWeekList
//    }
//
//    // 몇년 몇월인지 가져오기
//    fun getCalList(duration: Int = DURATION): Array<Calendar?> {
//        val calList = arrayOfNulls<Calendar>(DURATION)
//        val calendarInstance = Calendar.getInstance().apply { time = Date() }
//        for (i in 0 until duration) {
//            calList[i] = calendarInstance
//            calendarInstance.add(Calendar.DATE, 1)
//        }
//        return calList
//    }
}
