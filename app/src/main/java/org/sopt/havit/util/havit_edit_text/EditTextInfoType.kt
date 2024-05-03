package org.sopt.havit.util.havit_edit_text

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.sopt.havit.R


enum class EditTextInfoType(
    @ColorInt val color: Int,
    @StringRes val message: Int,
    val isVisible: Boolean = true,
    @DrawableRes val icon: Int? = null,
) {
    NONE(android.R.color.transparent, R.string.empty_value, false),
    INVALID_URL(R.color.havit_red, R.string.cannot_find_url_info, true, R.drawable.ic_notice_red),
    EXCEED_MAX_LENGTH_45(
        R.color.havit_red,
        R.string.exceed_max_length,
        true,
        R.drawable.ic_notice_red
    ),
}

