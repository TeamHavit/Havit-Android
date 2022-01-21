package org.sopt.havit.ui.save

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.havit.R
import org.sopt.havit.ShareActivity
import org.sopt.havit.databinding.FragmentSaveBinding


class SaveFragment(categoryName:String) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSaveBinding
    private val saveViewModel: SaveViewModel by viewModels()
    var categoryName=categoryName


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_save, container, false)
        binding.vm = saveViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            openKeyBoard()
            //activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
             val behavior = BottomSheetBehavior.from<ConstraintLayout>(
                 (dialog as BottomSheetDialog).findViewById(R.id.design_bottom_sheet)!!
             )
             behavior.state = BottomSheetBehavior.STATE_EXPANDED
             behavior.skipCollapsed = true
            binding.clSaveBottom.layoutParams.height = (resources.displayMetrics.heightPixels * 0.94).toInt()
        setListeners()
    }

    private fun openKeyBoard() {
        binding.etSaveUrl.requestFocus()
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.SHOW_IMPLICIT
        )
    }

    private fun hideKeyBoard() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    private fun setListeners() {
        binding.btnSaveClose.setOnClickListener {
            hideKeyBoard()
            dismiss()
        }
        binding.btnSaveNext.setOnClickListener {
            var intent = Intent(requireContext(), ShareActivity::class.java)
            intent.putExtra("url", binding.etSaveUrl.text.toString())
            startActivity(intent)
        }
        binding.etSaveUrl.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(c: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.etSaveUrl.text.isNotEmpty()) {
                    saveViewModel.setClick(true)
                } else {
                    saveViewModel.setClick(false)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onDestroy() {
        super.onDestroy()
        hideKeyBoard()
    }

}