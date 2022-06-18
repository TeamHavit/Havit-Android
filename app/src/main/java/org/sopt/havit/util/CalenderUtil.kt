package org.sopt.havit.util

import android.content.res.Resources
import android.util.Log
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

    fun setDateFormat(originTime: String): String {
        Log.d("originTime", originTime) // 2022.01.25 00:04:54

        // 날짜 (2022.01.25)
        val date =
            "${originTime[2]}${originTime[3]}.${originTime[5]}${originTime[6]}.${originTime[8]}${originTime[9]}"
        // 시 (오후 11시 :: 12시간제 적용)
        val hour = "${originTime[11]}${originTime[12]}".toInt()
        val newHour = when (hour) {
            in 0..12 -> " 오전 ${hour}시 "
            else -> " 오후 ${hour - 12}시 "
        }
        // 분 (3분 :: 자릿수 재졍렬을 위한 이중 형변환 사용)
        val min = "${originTime[14]}${originTime[15]}".toInt().toString() + "분"

        return "$date$newHour$min 알림 예정"
    }
}
