package org.sopt.havit.ui.share

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
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

        initTransaction()
        setBottomSheetHeight()

    }


    private fun initTransaction() {

        // 이미 생성된 카테고리가 있으면 카테고리 선택 Fragment 로 이동
        if (getCategoryNum() != 0){
            childFragmentManager.beginTransaction()
                .replace(R.id.fcv_share, SelectCategoryFragment())
                .commit()
        }

    }

    private fun getCategoryNum() : Int {
        return 0
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

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().finish()
    }
}