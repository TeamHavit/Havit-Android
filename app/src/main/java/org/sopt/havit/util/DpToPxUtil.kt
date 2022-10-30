package org.sopt.havit.util

import android.view.View
import androidx.annotation.Px
import kotlin.math.roundToInt

object DpToPxUtil {

    @Px
    fun View.px(dp: Int) = (dp * resources.displayMetrics.density).roundToInt()
}