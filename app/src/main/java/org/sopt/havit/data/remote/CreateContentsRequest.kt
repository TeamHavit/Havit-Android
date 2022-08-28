package org.sopt.havit.data.remote

import com.google.gson.annotations.SerializedName

data class CreateContentsRequest(
    var title: String,
    var url: String,
    val description: String,
    @SerializedName("image")
    val imageUrl: String,
    var isNotified: Boolean,
    var notificationTime: String,
    var categoryIds: List<Int>
)
