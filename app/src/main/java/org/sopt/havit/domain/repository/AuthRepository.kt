package org.sopt.havit.domain.repository

import org.sopt.havit.data.remote.SignInResponse
import org.sopt.havit.data.remote.SignUpResponse

interface AuthRepository {

    fun getFcmToken(): String

    fun saveAccessToken(accessToken: String)

    fun saveKakaoToken(kakaoToken: String)

    fun getAccessToken(): String

    fun getKakaoToken(): String

    suspend fun getSignIn(fcmToken: String, kakaoToken: String): SignInResponse

    suspend fun postSignUp(
        email: String,
        nickName: String,
        age: Int,
        gender: String,
        fcmToken: String,
        kakaoToken: String
    ): SignUpResponse
}
