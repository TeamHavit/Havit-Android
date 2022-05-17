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
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(ACCESS_TOKEN, Context.MODE_PRIVATE)

    fun setXAuthToken(token: String) {
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString(
            ACCESS_TOKEN,
            token
        )
        editor.apply()
    }

    fun getXAuthToken(): String = prefs.getString(ACCESS_TOKEN, "").toString()

    fun removeXAuthToken() = prefs.edit().clear().apply()

}