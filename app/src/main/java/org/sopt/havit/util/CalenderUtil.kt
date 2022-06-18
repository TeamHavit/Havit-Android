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

    val dateAndTimeWithDotFormatMD: DateFormat
        get() = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault())

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

    fun setDateFormatOnRadioBtn(originTime: String): String {
        /** 자릿수 꼭 지켜서 사용 ex) 2022.01.25 00:04:54 */
        // 날짜 (2022.01.25)
        val date =
            "${originTime[2]}${originTime[3]}.${originTime[5]}${originTime[6]}.${originTime[8]}${originTime[9]}"
        // 시 (오후 11시 :: 12시간제 적용)
        val newHour = when (val hour = "${originTime[11]}${originTime[12]}".toInt()) {
            0 -> "오전 12"
            12 -> "오후 12"
            in 0..11 -> "오전 $hour"
            in 13..24 -> "오후 ${hour - 12}"
            else -> Error("잘못된 시간입니다")
        }
        // 분 (3분 :: 자릿수 재졍렬을 위한 이중 형변환 사용)
        val min = "${originTime[14]}${originTime[15]}".toInt().toString() + "분"
        return "$date ${newHour}시 $min"
    }

    fun setDateFormat(originTime: String): String {
        val time = setDateFormatOnRadioBtn(originTime)
        return "$time 알림 예정"
    }
}
