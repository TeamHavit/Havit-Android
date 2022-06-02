package org.sopt.havit.data.source.remote

import org.sopt.havit.data.remote.BasicResponse
import org.sopt.havit.data.remote.SignInResponse
import org.sopt.havit.data.remote.SignUpResponse

interface AuthRemoteDataSource {

    suspend fun postSignUp(email: String, nickName: String, age:Int, gender:String, fcmToken: String, kakaoToken: String):SignUpResponse

    suspend fun checkNewUser(fcmToken : String, kakaoToken: String) : SignInResponse
}