package org.sopt.havit.ui.contents.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.havit.R
import org.sopt.havit.data.remote.ContentsMoreData
import org.sopt.havit.databinding.FragmentBottomSheetMoreBinding

class BottomSheetMoreFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSheetMoreBinding? = null
    private val binding get() = _binding!!
    private var viewType: String? = null
    var contents: ContentsMoreData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewType = it.getString(VIEW_TYPE)
            contents = it.getParcelable(CONTENTS_DATA)
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
        setDesignatedFragment()
    }

    private fun setDesignatedFragment() {
        if (contents != null) {
            when (viewType) {
                Edit_TITLE -> setFragmentWith<EditTitleFromMoreFragment>()
                MOVE_CATEGORY -> setFragmentWith<EditTitleFromMoreFragment>() // 수정할거임
                SET_ALARM -> setFragmentWith<EditTitleFromMoreFragment>() // 수정할거임
            }
        }
    }

    private inline fun <reified T : Fragment> setFragmentWith() {
        val bundle = Bundle().apply {
            putParcelable(CONTENTS_DATA, contents)
        }
        childFragmentManager.commit {
            replace<T>(containerViewId = R.id.fcv_more, args = bundle)
        }
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
        fun newInstance(param: String, contents: ContentsMoreData) =
            BottomSheetMoreFragment().apply {
                arguments = Bundle().apply {
                    putString(VIEW_TYPE, param)
                    putParcelable(CONTENTS_DATA, contents)
                }
            }

        const val CONTENTS_DATA = "CONTENTS_DATA"
        const val VIEW_TYPE = "VIEW_TYPE"
        const val Edit_TITLE = "EDIT_TITLE"
        const val MOVE_CATEGORY = "MOVE_CATEGORY"
        const val SET_ALARM = "SET_ALARM"
    }
}
