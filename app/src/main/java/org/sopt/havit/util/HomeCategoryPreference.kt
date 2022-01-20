package org.sopt.havit.util

import android.content.Context

object HomeCategoryPreference {
    private const val FINAL_POSITION = "FINAL_POSITION"
    private const val VP_POSITION = "VIEWPAGER_POSITION"
    private const val RV_POSITION = "RECYCLERVIEW_POSITION"


    fun setViewPagerPosition(context: Context, position: Int) {
        val preferences = context.getSharedPreferences(FINAL_POSITION, Context.MODE_PRIVATE)
        preferences.edit()
            .putInt("VP_POSITION", position)
            .apply()
    }

    fun getViewPagerPosition(context: Context) : Int {
        val preferences = context.getSharedPreferences(FINAL_POSITION, Context.MODE_PRIVATE)
        return preferences.getInt(VP_POSITION, 0)
    }
}