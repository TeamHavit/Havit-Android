package org.sopt.havit.ui.save

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentSaveBinding
import org.sopt.havit.util.KeyBoardUtil


class SaveFragment(categoryName:String) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSaveBinding
    var categoryName=categoryName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.setOnShowListener {
            //openKeyBoard()
            openKeyBoard()
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            val bottomSheet: View = dialog!!.findViewById(R.id.design_bottom_sheet)
            bottomSheet.layoutParams.height = (resources.displayMetrics.heightPixels * 0.94).toInt()

            val behavior = BottomSheetBehavior.from<ConstraintLayout>(
                (dialog as BottomSheetDialog).findViewById(R.id.design_bottom_sheet)!!
            )
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_save, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
    }

    private fun openKeyBoard(){
        binding.etSaveUrl.requestFocus()
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyBoard(){
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    private fun setListeners() {
        binding.btnSaveClose.setOnClickListener {
            hideKeyBoard()
            dismiss()
        }
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onDestroy() {
        super.onDestroy()
        hideKeyBoard()
    }
    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            openKeyBoard()
        }
    }

}