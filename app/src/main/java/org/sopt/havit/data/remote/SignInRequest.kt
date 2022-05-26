package org.sopt.havit.data.remote

data class SignInRequest(
    val fcmToken: String,
    val kakaoAccessToken: String
)