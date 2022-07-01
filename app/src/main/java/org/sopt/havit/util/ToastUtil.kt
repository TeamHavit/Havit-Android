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
        view = LayoutInflater.from(context).inflate(toastCase.view, null)
        toast.view = view
    }

    private fun setText(categoryName: String) {
        when (toastCase.viewType) {
            ADD_CONTENT_TYPE, CONTENT_CHECK_COMPLETE_TYPE -> return
            ADD_CATEGORY_TYPE -> {
                val textView: TextView = view.findViewById(R.id.tv_toast)
                textView.text = categoryName
            }
            else -> {
                val textView: TextView = view.findViewById(R.id.tv_toast)
                textView.text = getTitle(context)
            }
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
    val yOffsetDp: Int = MARGIN_NORMAL
) {
    CONTENT_DELETE(
        R.layout.toast_text,
        R.string.delete_content,
        CONTENT_DELETE_TYPE,
    ),
    SET_ALARM(
        R.layout.toast_text,
        R.string.setting_alarm,
        SET_ALARM_DOWN_TYPE,
    ),
    DELETE_CATEGORY(
        R.layout.toast_text,
        R.string.delete_category,
        DELETE_CATEGORY_TOP_TYPE,
    ),
    MAX_CATEGORY_NUM_EXCEEDED(
        R.layout.toast_text,
        R.string.max_category,
        MAX_CATEGORY_NUM_EXCEEDED_TOP_TYPE,
    ),
    ADD_CATEGORY(
        R.layout.toast_category_added,
        R.string.add_category,
        ADD_CATEGORY_TYPE,
    ),
    PAST_TIME(
        R.layout.toast_text,
        R.string.cannot_set_notification_time_on_past,
        PAST_TIME_TYPE,
        yOffsetDp = MARGIN_PAST_TIME
    ),
    ADD_CONTENT(
        R.layout.toast_contents_added,
        NULL,
        ADD_CONTENT_TYPE,
        Gravity.TOP,
        yOffsetDp = MARGIN_CONTENT_ADDED
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
    ),
    CANNOT_SEND_MAIL(
        R.layout.toast_text,
        R.string.cannot_send_mail,
        CANNOT_SEND_MAIL_TYPE
    ),
    ERROR_OCCUR(
        R.layout.toast_text,
        R.string.error_occur,
        ERROR_OCCUR_TYPE
    ),
    CATEGORY_MODIFY_COMPLETE(
        R.layout.toast_text,
        R.string.category_modify_complete,
        CATEGORY_MODIFY_COMPLETE_TYPE
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
const val MAX_CATEGORY_NUM_EXCEEDED_TOP_TYPE = 4
const val ADD_CATEGORY_TYPE = 6
const val PAST_TIME_TYPE = 8
const val ADD_CONTENT_TYPE = 9
const val SERVICE_PREPARING_TYPE = 10
const val CONTENT_CHECK_COMPLETE_TYPE = 11
const val CANNOT_SEND_MAIL_TYPE = 12
const val ERROR_OCCUR_TYPE = 13
const val CATEGORY_MODIFY_COMPLETE_TYPE = 14

const val MARGIN_CONTENT_ADDED = 30
const val MARGIN_HAVIT_COMPLETE = 40
const val MARGIN_NORMAL = 106
const val MARGIN_PAST_TIME = 345
