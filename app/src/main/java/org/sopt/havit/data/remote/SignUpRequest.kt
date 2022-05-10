package org.sopt.havit.data.remote

data class SignUpRequest(
    val age: Int,
    val email: String,
    val gender: String,
    val nickname: String,
    val fcmToken: String,
    val kakaoAccessToken: String
)