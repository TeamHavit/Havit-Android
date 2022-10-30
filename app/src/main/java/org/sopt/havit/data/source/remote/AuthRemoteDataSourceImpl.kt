package org.sopt.havit.data.source.remote

import org.sopt.havit.data.api.HavitApi
import org.sopt.havit.data.remote.SignInRequest
import org.sopt.havit.data.remote.SignInResponse
import org.sopt.havit.data.remote.SignUpRequest
import org.sopt.havit.data.remote.SignUpResponse
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(private val api: HavitApi) :
    AuthRemoteDataSource {
    override suspend fun postSignUp(
        email: String,
        nickName: String,
        age: Int,
        gender: String,
        fcmToken: String,
        kakaoToken: String,
        isMarketing: Boolean
    ): SignUpResponse {
        return api.postSignUp(SignUpRequest(age, email, fcmToken, gender, kakaoToken, nickName,isMarketing))
    }

    override suspend fun checkNewUser(fcmToken: String, kakaoToken: String): SignInResponse {
        return api.postSignIn(SignInRequest(fcmToken, kakaoToken))
    }
}
