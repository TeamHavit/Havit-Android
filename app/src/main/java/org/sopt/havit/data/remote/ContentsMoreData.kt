package org.sopt.havit.data.remote

import java.io.Serializable

data class ContentsMoreData(
    val id: Int,
    val image: String,
    val title: String,
    val createdAt: String,
    val url: String,
    val isNotified: Boolean,
    val notificationTime: String,
) : Serializable
