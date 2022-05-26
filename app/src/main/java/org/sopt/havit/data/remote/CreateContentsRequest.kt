package org.sopt.havit.data.remote

data class CreateContentsRequest(
    var title: String,
    var url: String,
    var isNotified: Boolean,
    var notificationTime: String,
    var categoryIds: List<Int>
)
