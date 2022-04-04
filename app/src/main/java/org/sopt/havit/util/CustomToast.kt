package org.sopt.havit.util

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import org.sopt.havit.R
import org.sopt.havit.databinding.ToastCategoryAddedBinding
import org.sopt.havit.databinding.ToastContentsAddedBinding

object CustomToast {

    fun showTextToast(
        context: Context,
        title: String,
    ) {
        val inflater = LayoutInflater.from(context)
        val toast = Toast(context)
        val view = inflater.inflate(R.layout.toast_text, null)
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

    fun contentsAddedToast(context: Context) {
        val toast = Toast(context)
        val binding = ToastContentsAddedBinding.inflate(LayoutInflater.from(context))
        toast.setGravity(Gravity.TOP, 0, 54)
        toast.view = binding.root
        toast.show()
    }

    fun showDesignatedToast(
        context: Context,
        @LayoutRes layoutResId: Int,
    ) {
        val inflater = LayoutInflater.from(context)
        val toast = Toast(context)
        val view = inflater.inflate(layoutResId, null)
        toast.view = view
        toast.show()
    }
}