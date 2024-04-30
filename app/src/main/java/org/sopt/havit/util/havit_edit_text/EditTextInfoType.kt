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
}

