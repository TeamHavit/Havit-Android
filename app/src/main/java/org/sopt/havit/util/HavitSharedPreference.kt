package org.sopt.havit.util

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class HavitSharedPreference @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private const val X_AUTH_TOKEN = "ACCESS_TOKEN"
        private const val FIRST_USER = "FIRST_USER"
    }

    fun setXAuthToken(xAuthToken: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(X_AUTH_TOKEN, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString(
            X_AUTH_TOKEN,
            xAuthToken
        )
        editor.apply()
    }

    fun getXAuthToken(): String {
        val prefs: SharedPreferences =
            context.getSharedPreferences(X_AUTH_TOKEN, Context.MODE_PRIVATE)
        return prefs.getString(X_AUTH_TOKEN, "").toString()
    }

    fun clearXAuthToken() {
        val prefs: SharedPreferences =
            context.getSharedPreferences(X_AUTH_TOKEN, Context.MODE_PRIVATE)
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
