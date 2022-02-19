package org.sopt.havit.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes

object CustomToast {

    fun showNoTitleCustomToast(
        context: Context,
        @LayoutRes layoutResId: Int,
    ) {
        val inflator = LayoutInflater.from(context)
        val toast = Toast(context)
        val view = inflator.inflate(layoutResId, null)
        toast.view = view
        toast.show()
    }

    @SuppressLint("ResourceType")
    fun showTitleCustomToast(
        context: Context,
        title: String,
        @LayoutRes layoutResId: Int,
    ) {
        val inflator = LayoutInflater.from(context)
        val toast = Toast(context)
        val view = inflator.inflate(layoutResId, null)
        val textView: TextView = view.findViewById(layoutResId)
        textView.text = title
        toast.view = view
        toast.show()

    }
}