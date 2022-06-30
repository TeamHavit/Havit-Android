package org.sopt.havit.util

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import org.sopt.havit.R
import org.sopt.havit.util.ToastCase.Companion.findClassByToastType
import kotlin.math.roundToInt

object ToastUtil {

    private lateinit var toast: Toast
    private lateinit var toastCase: ToastCase
    lateinit var context: Context

    fun makeToast(
        toastType: Int,
        ctx: Context,
        category: String? = ""
    ) {

        context = ctx
        toastCase = findClassByToastType(toastType) ?: throw IllegalStateException()

        toast = Toast(context)
        val view = LayoutInflater.from(context).inflate(getView(), null)
        toast.view = view

        if (toastType != ADD_CONTENT_TYPE) {
            val textView: TextView = view.findViewById(R.id.tv_toast)
            textView.text =
                if (toastType == ADD_CATEGORY_TOP_TYPE || toastType == ADD_CATEGORY_DOWN_TYPE)
                    category else getTitle()
        }

        toast.setGravity(toastCase.gravity, 0, convertDPtoPX(toastCase.yOffsetDp))
        toast.show()
    }

    private fun getView() = toastCase.view
    private fun getTitle() = context.getString(toastCase.text)
    private fun convertDPtoPX(dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp.toFloat() * density).roundToInt()
    }
}

enum class ToastCase(
    val view: Int,
    val text: Int,
    private val viewType: Int,
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
        MARGIN_ABOVE
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
        MARGIN_ABOVE
    ),
    ADD_CATEGORY_DOWN(
        R.layout.toast_category_added,
        R.string.add_category,
        ADD_CATEGORY_DOWN_TYPE,
        MARGIN_BOTTOM
    ),
    PAST_TIME(
        R.layout.toast_text,
        R.string.cannot_set_notification_time_on_past,
        PAST_TIME_TYPE,
        MARGIN_CENTER
    ),
    ADD_CONTENT(
        R.layout.toast_contents_added,
        NULL,
        ADD_CONTENT_TYPE,
        Gravity.TOP,
        MARGIN_TOP
    ),
    SERVICE_PREPARING(
        R.layout.toast_text,
        R.string.service_preparing,
        SERVICE_PREPARING_TYPE,
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

const val MARGIN_TOP = 54
const val MARGIN_BOTTOM = 78
const val MARGIN_ABOVE = 152
const val MARGIN_CENTER = 328
