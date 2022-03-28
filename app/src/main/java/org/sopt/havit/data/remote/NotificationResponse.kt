package org.sopt.havit.data.remote

data class NotificationResponse(
    val data: List<NotificationData>,
    val message: String,
    val status: Int,
    val success: Boolean
) {
    data class NotificationData(
        val createdAt: String,
        val description: String,
        val id: Int,
        val image: String,
        val isSeen: Boolean,
        val time: String,
        val title: String,
        val url: String
    )
}