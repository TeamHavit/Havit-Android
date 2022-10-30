package org.sopt.havit.util

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object KeyBoardUtil {
    private var screenMaxHeight = 0
    private var keyboardHeight = 0

    fun openKeyBoard(context: Context, editText: EditText) {
        editText.requestFocus()
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    fun hideKeyBoard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        activity.currentFocus?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
            it.clearFocus()
        }
    }

    fun setUpAsSoftKeyboard(view: View) {
        view.viewTreeObserver.addOnGlobalLayoutListener {
            val targetViewHeight = getTargetViewHeight(view)

            // screenMaxHeight, keyboardHeight 초기화
            if (targetViewHeight > screenMaxHeight) screenMaxHeight = targetViewHeight
            else keyboardHeight = screenMaxHeight - targetViewHeight

            // 키보드 표시 여부에 따라 margin 조정
            val param = view.layoutParams as ViewGroup.MarginLayoutParams
            param.bottomMargin = if (screenMaxHeight == targetViewHeight) 0 else keyboardHeight
            view.layoutParams = param
        }
    }

    private fun getTargetViewHeight(view: View): Int {
        val targetView = Rect()
        view.getWindowVisibleDisplayFrame(targetView)
        return targetView.bottom
    }
}