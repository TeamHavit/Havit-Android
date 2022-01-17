package org.sopt.havit.data.remote

import org.sopt.havit.domain.Contents

data class SearchContentsResponse(
    val data: List<Contents>,
    val message: String,
    val status: Int,
    val success: Boolean
) {
    data class Contents(
        val createdAt: String,
        val id: Int,
        val image: String,
        val isNotified: Boolean,
        val isSeen: Boolean,
        val notificationTime: String,
        val title: String,
        val url: String
    )
}