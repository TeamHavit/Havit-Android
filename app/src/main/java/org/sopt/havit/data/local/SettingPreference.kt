package org.sopt.havit.data.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SettingPreference @Inject constructor(@ApplicationContext context: Context) {

    private val preference: SharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    var isContentsNotiActivated: Boolean
        set(value) = preference.edit().putBoolean(CONTENTS_NOTI, value).apply()
        get() = preference.getBoolean(CONTENTS_NOTI, true)

    var isTotalNoticeActivated: Boolean
        set(value) = preference.edit().putBoolean(TOTAL_NOTI, value).apply()
        get() = preference.getBoolean(TOTAL_NOTI, true)

    companion object {
        private const val CONTENTS_NOTI = "CONTENTS_NOTI"
        private const val TOTAL_NOTI = "TOTAL_NOTI"
    }
}
