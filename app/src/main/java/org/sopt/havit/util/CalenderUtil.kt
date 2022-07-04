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
        val date = originTime.substring(2, 10).replace("-", ". ")
        val amPmHour = getHourAmPm(originTime.substring(11, 13).toInt())
        val min = originTime.substring(14, 16).toInt().toString()
        return "$date ${amPmHour}시 ${min}분"
    }

    private fun getHourAmPm(hour24: Int): String {
        return when (hour24) {
            0 -> "오전 12"
            12 -> "오후 12"
            in 0..11 -> "오전 $hour24"
            in 13..24 -> "오후 ${hour24 - 12}"
            else -> throw IllegalStateException("잘못된 시간입니다")
        }
    }

    fun setDateFormatOnCategoryView(originTime: String): String {
        val date = originTime.substring(0, 10).replace("-", ". ")
        val amPmHour = getHourAmPm(originTime.substring(11, 13).toInt())
        val min = originTime.substring(14, 16).toInt().toString()
        val text: String

        // 현재 시간 형식에 맞게 가져오기
        val now = System.currentTimeMillis()
        val nowDate = Date(now)
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val getTime: String = sdf.format(nowDate)

        text = if (originTime > getTime)
            "알림 예정"
        else
            "알림 완료"
        return "$date ${amPmHour}시 ${min}분 $text"
    }

    fun setDateFormat(originTime: String): String {
        val time = setDateFormatOnRadioBtn(originTime)
        return "$time 알림 예정"
    }
}
