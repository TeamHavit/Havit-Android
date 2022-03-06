package org.sopt.havit.ui.save

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.havit.R
import org.sopt.havit.ShareActivity
import org.sopt.havit.databinding.FragmentSaveBinding
import org.sopt.havit.util.KeyBoardHeightProvider
import org.sopt.havit.util.KeyBoardUtil
import java.net.URL


class SaveFragment(categoryName: String) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSaveBinding
    private val saveViewModel: SaveViewModel by viewModels()
    private var categoryName = categoryName
    private var isFirstKeyBoard = true
    private lateinit var clipboard: ClipboardManager
    private lateinit var clipData: ClipData


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
        setUrlPaste()
        setListeners()
    }

    // url 붙여넣기 팝업 생성
    private fun setUrlPaste() {
        clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipData = clipboard.primaryClip!!

        if (clipboard.hasPrimaryClip()) { // 클립보드에 내용이 있으면 팝업을 보여줌.
            binding.clPasteClipBoard.isVisible = true
            clipData.apply {
                val textToPaste: String =
                    this.getItemAt(0).text.toString().trim() // 클립보드에 저장된 첫번째 데이터
                binding.tvSaveUrl.text = textToPaste
            }
        }
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
        KeyBoardUtil.openKeyBoard(requireContext(), binding.etSaveUrl)
    }

    private fun hideKeyBoard() {
        KeyBoardUtil.hideKeyBoard(requireActivity())
    }

    private fun setListeners() {
        binding.clPasteClipBoard.setOnClickListener { // url 붙여넣기 팝업 클릭시 editText에 url 보여주기
            clipData.apply {
                val textToPaste: String = this.getItemAt(0).text.toString().trim()
                binding.etSaveUrl.setText(textToPaste)
            }
            binding.clPasteClipBoard.isVisible = false
        }
        binding.ivSaveUrlDelete.setOnClickListener { // 팝업창 닫기
            binding.clPasteClipBoard.isVisible = false
        }
        binding.ivSaveUrlTextDelete.setOnClickListener { // 사용자가 작성한 url 지우기
            binding.etSaveUrl.setText("")
        }
        binding.btnSaveClose.setOnClickListener {
            hideKeyBoard()
            dismiss()
        }
        binding.btnSaveNext.setOnClickListener {
            if (isFullPath(binding.etSaveUrl.text.toString())) {
                hideKeyBoard()
                dismiss()
                val intent = Intent(requireContext(), ShareActivity::class.java).apply {
                    putExtra("url", binding.etSaveUrl.text.toString())
                }
                startActivity(intent)
            } else {
                binding.clSaveUrlValid.isVisible = true
            }

        }
        binding.etSaveUrl.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(c: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.etSaveUrl.text.isNotEmpty()) {
                    saveViewModel.setClick(true)
                    binding.ivSaveUrlTextDelete.isVisible = true
                } else {
                    saveViewModel.setClick(false)
                    with(binding) {
                        ivSaveUrlTextDelete.isVisible = false
                        clSaveUrlValid.isVisible = false
                    }
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