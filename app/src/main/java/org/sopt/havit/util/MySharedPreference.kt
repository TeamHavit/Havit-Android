package org.sopt.havit.util

import android.content.Context
import android.content.SharedPreferences

object MySharedPreference {

    fun setHavit(context: Context,isHavit:Boolean){
        val prefs : SharedPreferences = context.getSharedPreferences("HAVIT",Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putBoolean("isHavit",isHavit)
        editor.apply()
    }

    fun getHavit(context: Context):Boolean{
        val prefs:SharedPreferences = context.getSharedPreferences("HAVIT",Context.MODE_PRIVATE)
        return prefs.getBoolean("isHavit",false)
    }
}