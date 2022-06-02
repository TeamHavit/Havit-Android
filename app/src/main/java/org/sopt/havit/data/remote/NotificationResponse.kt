package org.sopt.havit.data.remote

data class NotificationResponse(
    val data: List<NotificationData>,
    val message: String,
    val status: Int,
    val success: Boolean
) {
    data class NotificationData(
        val id: Int,
        val notificationTime: String,
        val title: String,
        val description: String,
        val image: String,
        val url: String,
        val isSeen: Boolean,
        val createdAt: String
    )
}