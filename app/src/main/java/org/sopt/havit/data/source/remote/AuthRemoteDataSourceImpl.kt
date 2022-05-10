package org.sopt.havit.data.source.remote

import org.sopt.havit.data.api.HavitApi
import org.sopt.havit.data.remote.*
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(private val api: HavitApi) :
    AuthRemoteDataSource {
    override suspend fun postSignUp(
        email: String,
        nickName: String,
        age: Int,
        gender: String,
        fcmToken: String,
        kakaoToken: String
    ): SignUpResponse {
        return api.postSignUp(SignUpRequest(age, email, gender, nickName, fcmToken, kakaoToken))
    }

    override suspend fun checkNewUser(fcmToken: String, kakaoToken: String): SignInResponse {
        return api.postSignIn(SignInRequest(fcmToken, kakaoToken))
    }
}