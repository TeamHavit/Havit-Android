package org.sopt.havit.data.repository

import org.sopt.havit.MyFirebaseMessagingService
import org.sopt.havit.data.remote.SignInResponse
import org.sopt.havit.data.remote.SignUpResponse
import org.sopt.havit.data.source.remote.AuthRemoteDataSource
import org.sopt.havit.data.source.local.AuthLocalDataSource
import org.sopt.havit.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authLocalDataSource: AuthLocalDataSource
) :
    AuthRepository {

    override fun getFcmToken(getFcmToken: (String) -> Unit) {
        return MyFirebaseMessagingService.getDeviceToken()
    }

    override fun saveAccessToken(token: String) {
        authLocalDataSource.saveAccessToken(token)
    }

    override fun getAccessToken(): String = authLocalDataSource.getAccessToken()

    override suspend fun checkNewUser(fcmToken: String, kakaoToken: String): SignInResponse {
        return authRemoteDataSource.checkNewUser(fcmToken, kakaoToken)
    }

    override suspend fun postSignUp(
        email: String,
        nickName: String,
        age: Int,
        gender: String,
        fcmToken: String,
        kakaoToken: String
    ): SignUpResponse {
        return authRemoteDataSource.postSignUp(email, nickName, age, gender, fcmToken, kakaoToken)
    }
}