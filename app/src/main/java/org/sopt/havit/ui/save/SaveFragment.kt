package org.sopt.havit.ui.save

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentSaveBinding
import org.sopt.havit.util.KeyBoardUtil


class SaveFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSaveBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.setOnShowListener {
            val behavior = BottomSheetBehavior.from<ConstraintLayout>(
                (dialog as BottomSheetDialog).findViewById(org.sopt.havit.R.id.design_bottom_sheet)!!
            )
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_save, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        KeyBoardUtil.openKeyBoard(requireContext(), binding.etSaveUrl)
        setListeners()
    }

    private fun setListeners() {
        binding.btnSaveClose.setOnClickListener {

        }
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }


    override fun onStart() {
        super.onStart()
        KeyBoardUtil.openKeyBoard(requireContext(), binding.etSaveUrl)
        if (dialog != null) {
            val bottomSheet: View = dialog!!.findViewById(R.id.design_bottom_sheet)
            bottomSheet.layoutParams.height = (resources.displayMetrics.heightPixels * 0.94).toInt()
        }
    }

}