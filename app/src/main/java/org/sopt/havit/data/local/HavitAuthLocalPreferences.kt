package org.sopt.havit.data.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class HavitAuthLocalPreferences @Inject constructor(
    @ApplicationContext private val
    context: Context
) {
    companion object {
        private const val ACCESS_TOKEN = "ACCESS_TOKEN"
        private const val KAKAO_TOKEN = "KAKAO_TOKEN"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(ACCESS_TOKEN, Context.MODE_PRIVATE)

    fun setXAuthToken(token: String) = prefs.edit().putString(ACCESS_TOKEN, token).apply()

    fun getXAuthToken(): String = prefs.getString(ACCESS_TOKEN, "").toString()

    fun setKakaoToken(token: String) = prefs.edit().putString(KAKAO_TOKEN, token).apply()

    fun getKakaoToken(): String = prefs.getString(KAKAO_TOKEN, "").toString()

    fun removeHavitAuthLocalToken() = prefs.edit().clear().apply()

}