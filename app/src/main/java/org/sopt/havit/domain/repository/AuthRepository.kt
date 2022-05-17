package org.sopt.havit.domain.repository

import org.sopt.havit.data.remote.SignInResponse
import org.sopt.havit.data.remote.SignUpResponse

interface AuthRepository {

    fun getFcmToken(): String

    fun saveAccessToken(token: String)

    fun getAccessToken(): String

    suspend fun checkNewUser(fcmToken: String, kakaoToken: String): SignInResponse

    suspend fun postSignUp(
        email: String,
        nickName: String,
        age: Int,
        gender: String,
        fcmToken: String,
        kakaoToken: String
    ): SignUpResponse
}