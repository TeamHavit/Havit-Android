package org.sopt.havit.domain.repository

import org.sopt.havit.data.remote.SignInResponse
import org.sopt.havit.data.remote.SignUpResponse

interface AuthRepository {

    fun getFcmToken(getFcmToken: (String) -> Unit)

    fun saveAccessToken(accessToken: String)

    fun saveKakaoToken(kakaoToken: String)

    fun getAccessToken(): String

    fun getKakaoToken(): String

    fun removeHavitAuthToken()

    fun getUserNickName(): String

    fun getUserAge(): Int

    fun getUserGender(): String

    fun getUserEmail(): String

    suspend fun getSignIn(fcmToken: String, kakaoToken: String): SignInResponse

    suspend fun postSignUp(
        email: String,
        nickName: String,
        age: Int,
        gender: String,
        fcmToken: String,
        kakaoToken: String,
        isMarketing: Boolean
    ): SignUpResponse
}
