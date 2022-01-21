package org.sopt.havit.util

import android.content.Context
import android.content.SharedPreferences

object MySharedPreference {

    private const val STORAGE_KEY = "HAVIT"
    private const val X_AUTH_TOKEN_1 = "TOKEN1" // 정아
    private const val X_AUTH_TOKEN_2 = "TOKEN2" // 효식
    private const val CONTENTS_TITLE = "TITLE"
    fun setHavit(context: Context, isHavit: Boolean) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(STORAGE_KEY, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putBoolean("isHavit", isHavit)
        editor.apply()
    }

    fun getHavit(context: Context): Boolean {
        val prefs: SharedPreferences =
            context.getSharedPreferences(STORAGE_KEY, Context.MODE_PRIVATE)
        return prefs.getBoolean("isHavit", false)
    }

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

    fun setXAuthToken(context: Context) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(X_AUTH_TOKEN_1, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString(
            X_AUTH_TOKEN_1,
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiaWRGaXJlYmFzZSI6IiIsImlhdCI6MTY0MTk5ODM0MCwiZXhwIjoxNjQ0NTkwMzQwLCJpc3MiOiJoYXZpdCJ9.w1hhe2g29wGzF5nokiil8KFf_c3qqPCXdVIU-vZt7Wo"
        )
        editor.apply()
    }

    fun getXAuthToken(context: Context): String {
        val prefs: SharedPreferences =
            context.getSharedPreferences(X_AUTH_TOKEN_1, Context.MODE_PRIVATE)
        return prefs.getString(X_AUTH_TOKEN_1, "").toString()
    }

}