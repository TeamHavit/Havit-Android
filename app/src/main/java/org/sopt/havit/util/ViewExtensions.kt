package org.sopt.havit.util

import android.util.Log
import android.view.View

inline fun View.setOnSingleClickListener(
    delay: Long = 500L,
    crossinline block: (View) -> Unit
) {
    var previousClickedTime = 0L
    setOnClickListener { view ->
        val clickedTime = System.currentTimeMillis()
        if (clickedTime - previousClickedTime >= delay) {
            block(view)
            Log.d("setOnSingleClickListener", (clickedTime - previousClickedTime).toString())
            previousClickedTime = clickedTime
        }
    }
}

/** Retrofit Object 에서 설정해둔 writeTimeOut 이 5초이므로,
 * single click interval 도 5초로 설정함 */
inline fun View.setOnSinglePostClickListener(
    delay: Long = 5_000L,
    crossinline block: (View) -> Unit
) {
    var previousClickedTime = 0L
    setOnClickListener { view ->
        val clickedTime = System.currentTimeMillis()
        if (clickedTime - previousClickedTime >= delay) {
            block(view)
            Log.d("setOnSingleClickListener", (clickedTime - previousClickedTime).toString())
            previousClickedTime = clickedTime
        }
    }
}