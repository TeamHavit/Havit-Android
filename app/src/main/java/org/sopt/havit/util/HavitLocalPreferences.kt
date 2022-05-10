package org.sopt.havit.util

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class HavitLocalPreferences @Inject constructor(
    @ApplicationContext private val
    context: Context
) {
    companion object {
        private const val ACCESS_TOKEN = "ACCESS_TOKEN"
    }

    fun setXAuthToken(token: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(ACCESS_TOKEN, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString(
            ACCESS_TOKEN,
            token
        )
        editor.apply()
    }

    fun getXAuthToken(): String {
        val prefs: SharedPreferences =
            context.getSharedPreferences(ACCESS_TOKEN, Context.MODE_PRIVATE)
        return prefs.getString(ACCESS_TOKEN, "").toString()
    }

}