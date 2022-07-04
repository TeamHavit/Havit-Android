package org.sopt.havit.util

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import org.sopt.havit.R
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

    /* 과거 시간으로는 알림설정을 할 수 없습니다
    *  bottom sheet 위로 위치 지정을 해야하는 속성 때문에 메소드 분리
    *  */
    fun showPastTImeToast(
        context: Context,
        yOffset: Int,
    ) {
        val inflater = LayoutInflater.from(context)
        val toast = Toast(context)
        val view = inflater.inflate(R.layout.toast_text, null)
        val textView: TextView = view.findViewById(R.id.tv_toast)
        textView.text = context.getString(R.string.cannot_set_notification_time_on_past)
        toast.view = view
        toast.setGravity(Gravity.BOTTOM, 0, yOffset)
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
