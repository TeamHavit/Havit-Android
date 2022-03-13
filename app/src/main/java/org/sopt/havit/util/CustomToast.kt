package org.sopt.havit.util

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import org.sopt.havit.R
import org.sopt.havit.databinding.ToastCategoryAddedBinding

object CustomToast {

    fun showTextToast(
        context: Context,
        title: String,
    ) {
        val inflator = LayoutInflater.from(context)
        val toast = Toast(context)
        val view = inflator.inflate(R.layout.toast_text, null)
        val textView: TextView = view.findViewById(R.id.tv_toast)
        textView.text = title
        toast.view = view
        toast.show()
    }

    fun categoryAddedToast(context: Context, title: String) {
        val toast = Toast(context)
        val binding = ToastCategoryAddedBinding.inflate(LayoutInflater.from(context))
        binding.categoryName = title
        toast.view = binding.root
        toast.show()
    }

    fun showDesignatedToast(
        context: Context,
        @LayoutRes layoutResId: Int,
    ) {
        val inflator = LayoutInflater.from(context)
        val toast = Toast(context)
        val view = inflator.inflate(layoutResId, null)
        toast.view = view
        toast.show()
    }
}