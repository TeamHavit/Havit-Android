package org.sopt.havit.data.remote

data class ContentsSearchResponse(
    val data: List<Data>,
    val message: String,
    val status: Int,
    val success: Boolean
) {
    data class Data(
        val createdAt: String,
        val description: String,
        val id: Int,
        val image: String,
        val isNotified: Boolean,
        val isSeen: Boolean,
        val notificationTime: String,
        val title: String,
        val url: String
    )
}