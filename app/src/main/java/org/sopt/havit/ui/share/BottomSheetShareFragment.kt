package org.sopt.havit.ui.share

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentBottomSheetShareBinding

class BottomSheetShareFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSheetShareBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetShareBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setBottomSheetHeight()
    }

    private fun setBottomSheetHeight() {
        (dialog as BottomSheetDialog).behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED      // 높이 고정
            skipCollapsed = true                            // HALF_EXPANDED 안되게 설정
        }

        // 휴대폰 화면의 0.94배 높이
        binding.bottomSheetShare.layoutParams.height =
            (resources.displayMetrics.heightPixels * 0.94).toInt()
    }

    // 안드로이드 자체 뒤로가기 눌렀을 때 BackPressed 감지
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme) {
            override fun onBackPressed() {
                val initialView = R.id.selectCategoryFragment
                if (binding.fcvShare.findNavController().currentDestination?.id == initialView)
                    requireActivity().finish()
                binding.fcvShare.findNavController().popBackStack()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        requireActivity().finish()
    }
}