package org.sopt.havit.util

import android.content.Context
import android.content.SharedPreferences

object MySharedPreference {

    private const val STORAGE_KEY = "HAVIT"
    private const val X_AUTH_TOKEN_1 = "TOKEN1" // 정아
    private const val X_AUTH_TOKEN_2 = "TOKEN2" // 효식
    private const val CONTENTS_TITLE = "TITLE"
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

    fun setNotificationTime(context: Context, time: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(NOTI_TIME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString(NOTI_TIME, time)
        editor.apply()
    }

    fun getNotificationTime(context: Context): String {
        val pref: SharedPreferences =
            context.getSharedPreferences(NOTI_TIME, Context.MODE_PRIVATE)
        return pref.getString(NOTI_TIME, "").toString()
    }

    fun clearNotificationTime(context: Context) {
        val pref: SharedPreferences =
            context.getSharedPreferences(NOTI_TIME, Context.MODE_PRIVATE)
        pref.edit().clear().apply()
    }

    fun setXAuthToken(context: Context) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(X_AUTH_TOKEN_1, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString(
            X_AUTH_TOKEN_1,
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjEsImlkRmlyZWJhc2UiOjEsImlhdCI6MTY0NjU2NjQxNSwiZXhwIjoxNjQ5MTU4NDE1LCJpc3MiOiJoYXZpdCJ9.rfUcnbUB_qORiASHRzfZ7ETjAuUluSdpa772H-cZCwI"
        )
        editor.apply()
    }

    fun getXAuthToken(context: Context): String {
        val prefs: SharedPreferences =
            context.getSharedPreferences(X_AUTH_TOKEN_1, Context.MODE_PRIVATE)
        return prefs.getString(X_AUTH_TOKEN_1, "").toString()
    }

}