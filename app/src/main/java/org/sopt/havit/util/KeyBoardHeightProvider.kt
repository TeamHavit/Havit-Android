package org.sopt.havit.util

import android.app.Activity
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout

class KeyBoardHeightProvider(private val mActivity: Activity) : PopupWindow(
    mActivity
),
    ViewTreeObserver.OnGlobalLayoutListener {
    private val rootView: View = View(mActivity)
    private var listener: HeightListener? = null
    private var heightMax = 0

    fun init(): KeyBoardHeightProvider {
        if (!isShowing) {
            val view = mActivity.window.decorView
            view.post { showAtLocation(view, Gravity.NO_GRAVITY, 0, 0) }
        }
        return this
    }

    fun setHeightListener(listener: HeightListener?): KeyBoardHeightProvider {
        this.listener = listener
        return this
    }

    override fun onGlobalLayout() {
        val rect = Rect()
        rootView.getWindowVisibleDisplayFrame(rect)
        if (rect.bottom > heightMax) {
            heightMax = rect.bottom
        }

        val keyboardHeight = heightMax - rect.bottom
        if (listener != null) {
            listener!!.onHeightChanged(keyboardHeight)
        }
    }

    interface HeightListener {
        fun onHeightChanged(height: Int)
    }

    init {
        contentView = rootView

        rootView.viewTreeObserver.addOnGlobalLayoutListener(this)
        setBackgroundDrawable(ColorDrawable(0))

        width = 0
        height = ConstraintLayout.LayoutParams.MATCH_PARENT

        softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        inputMethodMode = INPUT_METHOD_NEEDED
    }
}