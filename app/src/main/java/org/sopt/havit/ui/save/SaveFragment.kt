package org.sopt.havit.ui.save

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentSaveBinding
import org.sopt.havit.ui.share.ShareActivity
import org.sopt.havit.util.KeyBoardHeightProvider
import org.sopt.havit.util.KeyBoardUtil
import org.sopt.havit.util.KeyBoardUtil.openKeyBoard
import java.net.URL

class SaveFragment(categoryName: String) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSaveBinding
    private val saveViewModel: SaveViewModel by viewModels()
    private val clipboard: ClipboardManager by lazy {
        activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }
    private val clipData by lazy {
        clipboard.primaryClip?.getItemAt(0)?.text?.toString()?.trim()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_save, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = saveViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setBottomSheetShow()
        setClipBoardUrl()
        setListeners()
        setDialogKeyBoard()
    }

    private fun setDialogKeyBoard() {
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        dialog?.setOnShowListener {
            setOpenKeyBoard()
            getKeyBoardHeight()
        }
    }

    private fun setClipBoardUrl() {
        if (clipboard.hasPrimaryClip()) { // 클립보드에 내용이 있으면 팝업을 보여줌.
            if (isFullPath(clipData ?: "")) saveViewModel.clipDataUrl.value = clipData
        }
    }

    private fun isFullPath(potentialUrl: String): Boolean { // url 유효성 검사
        try {
            URL(potentialUrl).toURI()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun getKeyBoardHeight() {
        KeyBoardHeightProvider(requireActivity()).init()
            .setHeightListener(object : KeyBoardHeightProvider.HeightListener {
                override fun onHeightChanged(height: Int) {
                    onKeyboardShown(height)
                }
            })
    }

    fun onKeyboardShown(keyboardSize: Int) { // 키보드가 올라왔을 때 실행되는 함수
        val param = binding.btnSaveNext.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0, 0, 0, keyboardSize)
        binding.btnSaveNext.layoutParams = param
    }

    private fun setBottomSheetShow() {
        (dialog as BottomSheetDialog).behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED // 높이 고정
            skipCollapsed = true // HALF_EXPANDED 안되게 설정
            isHideable = false
            isDraggable = false
        }
        binding.clSaveBottom.layoutParams.height =
            (resources.displayMetrics.heightPixels * 0.94).toInt()
    }

    private fun setOpenKeyBoard() {
        openKeyBoard(requireContext(), binding.etSaveUrl)
    }

    private fun hideKeyBoard() {
        KeyBoardUtil.hideKeyBoard(requireActivity())
    }

    private fun startShareActivity() {
        val intent = Intent(requireContext(), ShareActivity::class.java).apply {
            putExtra("url", binding.etSaveUrl.text.toString())
        }
        startActivity(intent)
    }

    private fun setListeners() {
        binding.clPasteClipBoard.setOnClickListener { // url 붙여넣기 팝업 클릭시 editText에 url 보여주기
            saveViewModel.setUrlData(clipData!!)
            binding.etSaveUrl.setText(clipData)
            binding.etSaveUrl.setSelection(clipData!!.length)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                clipboard.clearPrimaryClip()
            }
            saveViewModel.setClipDataClear()
        }
        binding.btnSaveClose.setOnClickListener {
            hideKeyBoard()
            dismiss()
        }
        binding.btnSaveNext.setOnClickListener {
            if (isFullPath(binding.etSaveUrl.text.toString())) {
                startShareActivity()
                hideKeyBoard()
                dismiss()
            } else {
                binding.clSaveUrlValid.isVisible = true
            }
        }
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }
}
