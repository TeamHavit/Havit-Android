package org.sopt.havit.data.source.local

import org.sopt.havit.data.local.HavitAuthLocalPreferences
import javax.inject.Inject

class AuthLocalDataSourceImpl @Inject constructor(private val havitLocalPreferences: HavitAuthLocalPreferences) :
    AuthLocalDataSource {
    override fun saveAccessToken(token: String) {
        havitLocalPreferences.setXAuthToken(token)
    }

    override fun getAccessToken(): String {
        return havitLocalPreferences.getXAuthToken()
    }

    override fun saveRefreshToken(token: String) {
        havitLocalPreferences.setRefreshToken(token)
    }

    override fun getRefreshToken(): String {
        return havitLocalPreferences.getRefreshToken()
    }

    override fun saveKakaoToken(kakaoToken: String) =
        havitLocalPreferences.setKakaoToken(kakaoToken)

    override fun getKakaoToken(): String = havitLocalPreferences.getKakaoToken()

    override fun getUserAge(): Int = havitLocalPreferences.userAge

    override fun getUserGender(): String = havitLocalPreferences.userGender

    override fun getUserEmail(): String = havitLocalPreferences.userEmail

    override fun getUserNickName(): String = havitLocalPreferences.userNickName

    override fun removeHavitAuthToken() {
        havitLocalPreferences.removeHavitAuthLocalToken()
    }
}
