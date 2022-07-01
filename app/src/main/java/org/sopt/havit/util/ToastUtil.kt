package org.sopt.havit.util

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import org.sopt.havit.R
import org.sopt.havit.util.ToastCase.Companion.findClassByToastType
import javax.inject.Inject
import kotlin.math.roundToInt

class ToastUtil @Inject constructor(@ApplicationContext private val context: Context) {

    private lateinit var toastCase: ToastCase
    private lateinit var toast: Toast
    private lateinit var view: View

    fun makeToast(
        toastType: Int,
        categoryName: String = ""
    ) {
        toastCase = findClassByToastType(toastType) ?: throw IllegalStateException()

        toast = Toast(context)
        setView()
        setGravity()
        setText(categoryName)
        toast.show()
    }

    private fun setView() {
        view = LayoutInflater.from(context).inflate(getView(), null)
        toast.view = view
    }

    private fun getView() = toastCase.view

    private fun setText(categoryName: String) {
        val textView: TextView = view.findViewById(R.id.tv_toast)
        textView.text = when (toastCase.viewType) {
            ADD_CONTENT_TYPE, MARGIN_HAVIT_COMPLETE -> return
            ADD_CATEGORY_TOP_TYPE, ADD_CATEGORY_DOWN_TYPE -> categoryName
            else -> getTitle(context)
        }
    }

    private fun setGravity() {
        toast.setGravity(toastCase.gravity, 0, convertDPtoPX(toastCase.yOffsetDp))
    }

    private fun getTitle(context: Context) = context.getString(toastCase.text)

    private fun convertDPtoPX(dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp.toFloat() * density).roundToInt()
    }
}

enum class ToastCase(
    @LayoutRes val view: Int,
    @StringRes val text: Int,
    val viewType: Int,
    val gravity: Int = Gravity.BOTTOM,
    val yOffsetDp: Int = MARGIN_BOTTOM
) {
    CONTENT_DELETE_DOWN(
        R.layout.toast_text,
        R.string.delete_content,
        CONTENT_DELETE_TYPE,
    ),
    SET_ALARM_DOWN(
        R.layout.toast_text,
        R.string.setting_alarm,
        SET_ALARM_DOWN_TYPE,
    ),
    DELETE_CATEGORY_TOP(
        R.layout.toast_text,
        R.string.delete_category,
        DELETE_CATEGORY_TOP_TYPE,
    ),
    DELETE_CATEGORY_DOWN(
        R.layout.toast_text,
        R.string.delete_category,
        DELETE_CATEGORY_DOWN_TYPE,
    ),
    MAX_CATEGORY_NUM_EXCEEDED_TOP(
        R.layout.toast_text,
        R.string.max_category,
        MAX_CATEGORY_NUM_EXCEEDED_TOP_TYPE,
        yOffsetDp = MARGIN_ABOVE
    ),
    MAX_CATEGORY_NUM_EXCEEDED_DOWN(
        R.layout.toast_text,
        R.string.max_category,
        MAX_CATEGORY_NUM_EXCEEDED_DOWN_TYPE,
    ),
    ADD_CATEGORY_TOP(
        R.layout.toast_category_added,
        R.string.add_category,
        ADD_CATEGORY_TOP_TYPE,
        yOffsetDp = MARGIN_ABOVE
    ),
    ADD_CATEGORY_DOWN(
        R.layout.toast_category_added,
        R.string.add_category,
        ADD_CATEGORY_DOWN_TYPE,
        yOffsetDp = MARGIN_BOTTOM
    ),
    PAST_TIME(
        R.layout.toast_text,
        R.string.cannot_set_notification_time_on_past,
        PAST_TIME_TYPE,
        yOffsetDp = MARGIN_CENTER
    ),
    ADD_CONTENT(
        R.layout.toast_contents_added,
        NULL,
        ADD_CONTENT_TYPE,
        Gravity.TOP,
        yOffsetDp = MARGIN_TOP
    ),
    SERVICE_PREPARING(
        R.layout.toast_text,
        R.string.service_preparing,
        SERVICE_PREPARING_TYPE,
    ),
    CONTENT_CHECK_COMPLETE(
        R.layout.toast_havit_complete,
        NULL,
        CONTENT_CHECK_COMPLETE_TYPE,
        yOffsetDp = MARGIN_HAVIT_COMPLETE
    );

    companion object {
        fun findClassByToastType(viewType: Int): ToastCase? =
            values().find { it.viewType == viewType }
    }
}

const val NULL = -1
const val CONTENT_DELETE_TYPE = 0
const val SET_ALARM_DOWN_TYPE = 1
const val DELETE_CATEGORY_TOP_TYPE = 2
const val DELETE_CATEGORY_DOWN_TYPE = 3
const val MAX_CATEGORY_NUM_EXCEEDED_TOP_TYPE = 4
const val MAX_CATEGORY_NUM_EXCEEDED_DOWN_TYPE = 5
const val ADD_CATEGORY_TOP_TYPE = 6
const val ADD_CATEGORY_DOWN_TYPE = 7
const val PAST_TIME_TYPE = 8
const val ADD_CONTENT_TYPE = 9
const val SERVICE_PREPARING_TYPE = 10
const val CONTENT_CHECK_COMPLETE_TYPE = 11

const val MARGIN_TOP = 54
const val MARGIN_HAVIT_COMPLETE = 40
const val MARGIN_BOTTOM = 37
const val MARGIN_ABOVE = 106
const val MARGIN_CENTER = 328
