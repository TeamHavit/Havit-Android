package org.sopt.havit.ui.save

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.havit.R
import org.sopt.havit.ShareActivity
import org.sopt.havit.databinding.FragmentSaveBinding
import org.sopt.havit.util.CustomToast
import org.sopt.havit.util.KeyBoardHeightProvider
import java.net.URL


class SaveFragment(categoryName: String) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSaveBinding
    private val saveViewModel: SaveViewModel by viewModels()
    private var categoryName = categoryName
    private var isFirstKeyBoard = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_save, container, false)
        binding.vm = saveViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        getKeyBoardHeight()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBottomSheetShow()
        setListeners()
    }

    private fun getKeyBoardHeight() {
        KeyBoardHeightProvider(requireActivity()).init()
            .setHeightListener(object : KeyBoardHeightProvider.HeightListener {
                override fun onHeightChanged(height: Int) {
                    onKeyboardShown(height)
                }
            })
    }

    // url 유효성 검사
    private fun isFullPath(potentialUrl: String): Boolean {
        try {
            URL(potentialUrl).toURI()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    // 키보드가 올라왔을 때 실행되는 함수
    fun onKeyboardShown(keyboardSize: Int) {
        if (isFirstKeyBoard) { // 맨 처음에만 키보드 오픈
            openKeyBoard()
            isFirstKeyBoard = false
        }
        Log.d("KEYBOARD", keyboardSize.toString())

        val param = binding.btnSaveNext.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0, 0, 0, keyboardSize)
        binding.btnSaveNext.layoutParams = param
    }

    private fun setBottomSheetShow() {
        (dialog as BottomSheetDialog).behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED      // 높이 고정
            skipCollapsed = true                            // HALF_EXPANDED 안되게 설정
        }
        binding.clSaveBottom.layoutParams.height =
            (resources.displayMetrics.heightPixels * 0.94).toInt()
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
            if (isFullPath(binding.etSaveUrl.text.toString())) {
                val intent = Intent(requireContext(), ShareActivity::class.java).apply {
                    putExtra("url", binding.etSaveUrl.text.toString())
                }
                startActivity(intent)
                dismiss()
            } else {
                CustomToast.showTextToast(requireContext(),getString(R.string.url_unavailable))
            }

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

    override fun onDestroyView() {
        super.onDestroyView()
        hideKeyBoard()
    }


}