package org.sopt.havit.util

import androidx.fragment.app.Fragment

fun Fragment.getTopmostFragmentOrNull() = childFragmentManager.fragments.firstOrNull()