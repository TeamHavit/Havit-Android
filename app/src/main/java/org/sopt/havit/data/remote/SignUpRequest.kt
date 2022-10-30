package org.sopt.havit.data.remote

data class SignUpRequest(
    val age: Int,
    val email: String,
    val fcmToken: String,
    val gender: String,
    val kakaoAccessToken: String,
    val nickname: String,
    val isMarketing: Boolean
)
