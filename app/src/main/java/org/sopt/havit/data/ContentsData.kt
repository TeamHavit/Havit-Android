package org.sopt.havit.data

data class ContentsData(
    val hasAlarm: Boolean,
    val category: String,
    val title: String,
    val description: String,
    val isHavit: Boolean,
    val date: String,
    val image: String,
    val alarmDate: String,
    val url: String
)
