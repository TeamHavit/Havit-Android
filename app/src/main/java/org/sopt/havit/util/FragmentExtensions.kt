package org.sopt.havit.util

import androidx.fragment.app.Fragment
import org.sopt.havit.R

fun Fragment.getTopmostFragmentOrNull() =
    childFragmentManager.findFragmentById(R.id.fcv_share)?.childFragmentManager?.fragments?.firstOrNull()