package org.sopt.havit.data.repository

import com.google.firebase.messaging.FirebaseMessaging
import org.sopt.havit.data.remote.SignInResponse
import org.sopt.havit.data.remote.SignUpResponse
import org.sopt.havit.data.source.local.AuthLocalDataSource
import org.sopt.havit.data.source.remote.AuthRemoteDataSource
import org.sopt.havit.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authLocalDataSource: AuthLocalDataSource
) : AuthRepository {

    override fun getFcmToken(getFcmToken: (String) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            getFcmToken(requireNotNull(task.result))
        }
    }

    override fun saveAccessToken(accessToken: String) {
        authLocalDataSource.saveAccessToken(accessToken)
    }

    override fun saveKakaoToken(kakaoToken: String) {
        authLocalDataSource.saveKakaoToken(kakaoToken)
    }

    override fun getAccessToken(): String = authLocalDataSource.getAccessToken()

    override fun getKakaoToken(): String = authLocalDataSource.getKakaoToken()

    override fun removeHavitAuthToken() = authLocalDataSource.removeHavitAuthToken()

    override fun getUserNickName(): String = authLocalDataSource.getUserNickName()

    override fun getUserAge(): Int = authLocalDataSource.getUserAge()

    override fun getUserGender(): String = authLocalDataSource.getUserGender()

    override fun getUserEmail(): String = authLocalDataSource.getUserEmail()

    override suspend fun getSignIn(fcmToken: String, kakaoToken: String): SignInResponse {
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
