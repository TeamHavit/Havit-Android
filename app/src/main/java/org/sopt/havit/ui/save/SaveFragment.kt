package org.sopt.havit.ui.save

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.InputMethodManager
import android.webkit.URLUtil
import android.widget.PopupWindow
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.havit.R
import org.sopt.havit.ShareActivity
import org.sopt.havit.databinding.FragmentSaveBinding
import java.net.URL


class SaveFragment(categoryName: String) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSaveBinding
    private val saveViewModel: SaveViewModel by viewModels()
    private var categoryName = categoryName
    private var isFirstKeyBoard = false

    override fun onDestroyView() {
        super.onDestroyView()
        hideKeyBoard()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_save, container, false)
        binding.vm = saveViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        HeightProvider(requireActivity()).init()
            .setHeightListener(object : HeightProvider.HeightListener {
                override fun onHeightChanged(height: Int) {
                    onKeyboardShown(height)
                }
            })
        return binding.root
    }

    fun isFullPath(potentialUrl: String): Boolean {
        try {
            URL(potentialUrl).toURI()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun onKeyboardShown(keyboardSize: Int) {
        if (!isFirstKeyBoard) {
            openKeyBoard()
            isFirstKeyBoard = true
        }
        Log.d("KEYBOARD", keyboardSize.toString())

        val param = binding.btnSaveNext.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(10, 10, 10, keyboardSize)
        binding.btnSaveNext.layoutParams = param
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (dialog as BottomSheetDialog).behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED      // 높이 고정
            skipCollapsed = true                            // HALF_EXPANDED 안되게 설정
        }
        // behavior.state = BottomSheetBehavior.STATE_EXPANDED
        //  behavior.skipCollapsed = true
        binding.clSaveBottom.layoutParams.height =
            (resources.displayMetrics.heightPixels * 0.94).toInt()
        (dialog as BottomSheetDialog).setOnDismissListener {
            hideKeyBoard()
        }
        setListeners()
    }

    private fun setCustomToast() {
        val toast = Toast(requireContext())
        val view = layoutInflater.inflate(R.layout.toast_url_unavailable, null)
        toast.view = view
        toast.show()
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
            if(isFullPath(binding.etSaveUrl.text.toString())){
                val intent = Intent(requireContext(), ShareActivity::class.java).apply {
                    putExtra("url", binding.etSaveUrl.text.toString())
                }
                startActivity(intent)
                dismiss()
            }else{
                setCustomToast()
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

    override fun onDestroy() {
        super.onDestroy()
        hideKeyBoard()
    }


    class HeightProvider(private val mActivity: Activity) : PopupWindow(
        mActivity
    ),
        OnGlobalLayoutListener {
        private val rootView: View = View(mActivity)
        private var listener: HeightListener? = null
        private var heightMax // Record the maximum height of the pop content area
                = 0

        fun init(): HeightProvider {
            if (!isShowing) {
                val view = mActivity.window.decorView
                // Delay loading popupwindow, if not, error will be reported
                view.post { showAtLocation(view, Gravity.NO_GRAVITY, 0, 0) }
            }
            return this
        }

        fun setHeightListener(listener: HeightListener?): HeightProvider {
            this.listener = listener
            return this
        }

        override fun onGlobalLayout() {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            if (rect.bottom > heightMax) {
                heightMax = rect.bottom
            }

            // The difference between the two is the height of the keyboard
            val keyboardHeight = heightMax - rect.bottom
            if (listener != null) {
                listener!!.onHeightChanged(keyboardHeight)
            }
        }

        interface HeightListener {
            fun onHeightChanged(height: Int)
        }

        init {

            // Basic configuration
            contentView = rootView

            // Monitor global Layout changes
            rootView.viewTreeObserver.addOnGlobalLayoutListener(this)
            setBackgroundDrawable(ColorDrawable(0))

            // Set width to 0 and height to full screen
            width = 0
            height = ConstraintLayout.LayoutParams.MATCH_PARENT

            // Set keyboard pop-up mode
            softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
            inputMethodMode = INPUT_METHOD_NEEDED
        }
    }

}