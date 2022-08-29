package org.sopt.havit.util

import android.content.Context
import android.content.SharedPreferences

object MySharedPreference {

    private const val STORAGE_KEY = "HAVIT"
    private const val X_AUTH_TOKEN_1 = "TOKEN" // 정아
    private const val CONTENTS_TITLE = "TITLE"
    private const val FIRST_USER = "FIRST_USER"
    private const val NOTI_TIME = "notification_time"

    fun setTitle(context: Context, title: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(CONTENTS_TITLE, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString(CONTENTS_TITLE, title)
        editor.apply()
    }

    fun getTitle(context: Context): String {
        val pref: SharedPreferences =
            context.getSharedPreferences(CONTENTS_TITLE, Context.MODE_PRIVATE)
        return pref.getString(CONTENTS_TITLE, "").toString()
    }

    fun clearTitle(context: Context) {
        val pref: SharedPreferences =
            context.getSharedPreferences(CONTENTS_TITLE, Context.MODE_PRIVATE)
        pref.edit().clear().apply()
    }

    fun setXAuthToken(context: Context, xAuthToken: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(X_AUTH_TOKEN_1, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString(
            X_AUTH_TOKEN_1,
            xAuthToken
        )
        editor.apply()
    }

    fun getXAuthToken(context: Context): String {
        val prefs: SharedPreferences =
            context.getSharedPreferences(X_AUTH_TOKEN_1, Context.MODE_PRIVATE)
        return prefs.getString(X_AUTH_TOKEN_1, "").toString()
    }

    fun clearXAuthToken(context: Context) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(X_AUTH_TOKEN_1, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }

    fun saveFirstEnter(context: Context) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(FIRST_USER, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putBoolean(FIRST_USER, false)
        editor.apply()
    }

    fun isFirstEnter(context: Context): Boolean {
        val pref: SharedPreferences =
            context.getSharedPreferences(FIRST_USER, Context.MODE_PRIVATE)
        return pref.getBoolean(FIRST_USER, true)
    }

}
