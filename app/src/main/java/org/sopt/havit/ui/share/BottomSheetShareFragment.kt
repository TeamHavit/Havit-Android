package org.sopt.havit.ui.share

import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentBottomSheetShareBinding


class BottomSheetShareFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSheetShareBinding? = null
    private val binding get() = _binding!!

    // Round Corner
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetShareBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        requireView().post {
            val parent = view.parent as View
            val params = parent.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior
            val bottomSheetBehavior = behavior as BottomSheetBehavior<*>?
            bottomSheetBehavior?.peekHeight = getTargetHeight()
            parent.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    private fun getTargetHeight(): Int {
        // 휴대폰 화면의 0.94배 높이
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            return (requireActivity().windowManager.currentWindowMetrics.bounds.height() * 0.94).toInt()
        return (resources.displayMetrics.heightPixels  * 0.94).toInt()
    }

}