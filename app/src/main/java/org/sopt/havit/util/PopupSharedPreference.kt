package org.sopt.havit.util

import android.content.Context
import android.content.SharedPreferences

object PopupSharedPreference {
    private const val DELETE_POPUP_TIME = "delete_popup_time"
    private const val IS_POPUP = "is_popup"
    private const val REACH_LEVEL = "reach_level"

    fun setReachLevel(context: Context, level: Int) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(REACH_LEVEL, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putInt(
            REACH_LEVEL, level
        ).apply()
    }

    fun getReachLevel(context: Context): Int {
        val prefs: SharedPreferences =
            context.getSharedPreferences(REACH_LEVEL, Context.MODE_PRIVATE)
        return prefs.getInt(REACH_LEVEL, 0)
    }

    fun setDeletePopupTime(context: Context, time: Long) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(DELETE_POPUP_TIME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putLong(
            DELETE_POPUP_TIME,
            time
        ).apply()
    }

    fun getDeletePopupTime(context: Context): Long {
        val prefs: SharedPreferences =
            context.getSharedPreferences(DELETE_POPUP_TIME, Context.MODE_PRIVATE)
        return prefs.getLong(DELETE_POPUP_TIME, 0)
    }

    fun setIsPopup(context: Context, isPopup: Boolean) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(IS_POPUP, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putBoolean(
            IS_POPUP,
            isPopup
        ).apply()
    }

    fun getIsPopup(context: Context): Boolean {
        val prefs: SharedPreferences =
            context.getSharedPreferences(IS_POPUP, Context.MODE_PRIVATE)
        return prefs.getBoolean(IS_POPUP, true)
    }
}