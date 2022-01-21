package org.sopt.havit.data.remote

data class ContentsResponse(
    val data: List<ContentsData>,
    val message: String,
    val status: Int,
    val success: Boolean
) {
    data class ContentsData(
        var createdAt: String,
        val description: String,
        val id: Int,
        val image: String,
        val isNotified: Boolean,
        var isSeen: Boolean,
        var notificationTime: String,
        val seenAt: String,
        val title: String,
        val url: String
    )
}