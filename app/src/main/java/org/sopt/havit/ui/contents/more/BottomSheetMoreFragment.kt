package org.sopt.havit.ui.contents.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.havit.databinding.FragmentBottomSheetMoreBinding

class BottomSheetMoreFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSheetMoreBinding? = null
    private val binding get() = _binding!!
    private var viewType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewType = it.getString(VIEW_TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetMoreBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setBottomSheetHeight()
    }

    private fun setBottomSheetHeight() {
        (dialog as BottomSheetDialog).behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED // 높이 고정
            skipCollapsed = true // HALF_EXPANDED 안되게 설정
        }
        binding.bottomSheetMore.layoutParams.height =
            (resources.displayMetrics.heightPixels * 0.94).toInt()
    }

    companion object {
        @JvmStatic
        fun newInstance(param: String) =
            BottomSheetMoreFragment().apply {
                arguments = Bundle().apply {
                    putString(VIEW_TYPE, param)
                }
            }

        const val VIEW_TYPE = "VIEW_TYPE"
        const val MODIFY_TITLE = "EDIT_TITLE"
        const val MOVE_CATEGORY = "MOVE_CATEGORY"
        const val SET_ALARM = "SET_ALARM"
    }
}
