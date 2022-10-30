package org.sopt.havit.data.local

import android.content.Context
import android.content.SharedPreferences
import com.kakao.sdk.navi.Constants.USER_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class HavitAuthLocalPreferences @Inject constructor(
    @ApplicationContext private val
    context: Context
) {
    companion object {
        private const val ACCESS_TOKEN = "ACCESS_TOKEN"
        private const val KAKAO_TOKEN = "KAKAO_TOKEN"
        private const val USER_EMAIL = "USER_EMAIL"
        private const val USER_AGE = "USER_AGE"
        private const val USER_GENDER = "USER_GENDER"
        private const val USER_NICKNAME = "USER_NICKNAME"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(ACCESS_TOKEN, Context.MODE_PRIVATE)

    fun setXAuthToken(token: String) = prefs.edit().putString(ACCESS_TOKEN, token).apply()

    fun getXAuthToken(): String = prefs.getString(ACCESS_TOKEN, "").toString()

    fun setKakaoToken(token: String) = prefs.edit().putString(KAKAO_TOKEN, token).apply()

    fun getKakaoToken(): String = prefs.getString(KAKAO_TOKEN, "").toString()

    fun removeHavitAuthLocalToken() = prefs.edit().clear().apply()


    var userEmail: String
        set(value) = prefs.edit().putString(USER_EMAIL, value).apply()
        get() = prefs.getString(USER_EMAIL, "") ?: ""

    var userAge: Int
        set(value) = prefs.edit().putInt(USER_AGE, value).apply()
        get() = prefs.getInt(USER_AGE, 0)

    var userGender: String
        set(value) = prefs.edit().putString(USER_GENDER, value).apply()
        get() = prefs.getString(USER_GENDER, "") ?: ""

    var userNickName: String
        set(value) = prefs.edit().putString(USER_NICKNAME, value).apply()
        get() = prefs.getString(USER_NICKNAME, "") ?: ""
}
