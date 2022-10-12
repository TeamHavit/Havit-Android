package org.sopt.havit.util

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class HavitSharedPreference @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private const val STORAGE_KEY = "HAVIT"
        private const val X_AUTH_TOKEN_1 = "TOKEN" // 정아
        private const val CONTENTS_TITLE = "TITLE"
        private const val FIRST_USER = "FIRST_USER"
        private const val NOTI_TIME = "notification_time"
    }

    fun setTitle(title: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(CONTENTS_TITLE, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString(CONTENTS_TITLE, title)
        editor.apply()
    }

    fun getTitle(): String {
        val pref: SharedPreferences =
            context.getSharedPreferences(CONTENTS_TITLE, Context.MODE_PRIVATE)
        return pref.getString(CONTENTS_TITLE, "").toString()
    }

    fun clearTitle() {
        val pref: SharedPreferences =
            context.getSharedPreferences(CONTENTS_TITLE, Context.MODE_PRIVATE)
        pref.edit().clear().apply()
    }

    fun setXAuthToken(xAuthToken: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(X_AUTH_TOKEN_1, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString(
            X_AUTH_TOKEN_1,
            xAuthToken
        )
        editor.apply()
    }

    fun getXAuthToken(): String {
        val prefs: SharedPreferences =
            context.getSharedPreferences(X_AUTH_TOKEN_1, Context.MODE_PRIVATE)
        return prefs.getString(X_AUTH_TOKEN_1, "").toString()
    }

    fun clearXAuthToken() {
        val prefs: SharedPreferences =
            context.getSharedPreferences(X_AUTH_TOKEN_1, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }

    fun saveFirstEnter() {
        val prefs: SharedPreferences =
            context.getSharedPreferences(FIRST_USER, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putBoolean(FIRST_USER, false)
        editor.apply()
    }

    fun isFirstEnter(): Boolean {
        val pref: SharedPreferences =
            context.getSharedPreferences(FIRST_USER, Context.MODE_PRIVATE)
        return pref.getBoolean(FIRST_USER, true)
    }
}
