package org.sopt.havit.data.remote

import com.google.gson.annotations.SerializedName

data class FcmTokenParams(
    @SerializedName("fcmToken")
    val fcmToken: String
)
