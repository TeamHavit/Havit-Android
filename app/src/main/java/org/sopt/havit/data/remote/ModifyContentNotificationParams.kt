package org.sopt.havit.data.remote

import com.google.gson.annotations.SerializedName

data class ModifyContentNotificationParams(
    @SerializedName("notificationTime")
    val notificationTime: String
)
