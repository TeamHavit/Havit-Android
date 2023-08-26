package org.sopt.havit.data.source.local

interface AuthLocalDataSource {
    fun saveAccessToken(token: String)
    fun getAccessToken(): String
    fun saveRefreshToken(token: String)
    fun saveKakaoToken(kakaoToken: String)
    fun getKakaoToken(): String
    fun getUserAge():Int
    fun getUserGender():String
    fun getUserEmail():String
    fun getUserNickName():String
    fun removeHavitAuthToken()
}
