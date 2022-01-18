package org.sopt.havit.data.remote

data class ContentsResponse(
    val data: List<ContentsData>,
    val message: String,
    val status: Int,
    val success: Boolean
) {
    data class ContentsData(
        val createdAt: String,
        val description: String,
        val id: Int,
        val image: String,
        val isNotified: Boolean,
        val isSeen: Boolean,
        val notificationTime: String,
        val seenAt: String,
        val title: String,
        val url: String
    )
}