package org.sopt.havit.domain.entity

data class Contents(
    val createdAt: String,
    val description: String,
    val id: Int,
    val image: String,
    val isNotified: Boolean,
    var isSeen: Boolean,
    val notificationTime: String,
    val title: String,
    val url: String
)
