package org.sopt.havit.util.havit_edit_text

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import org.sopt.havit.databinding.LayoutHavitEditTextBinding

@SuppressLint("ViewConstructor")
class HavitEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var lifecycleOwner: LifecycleOwner? = null

    fun setLifecycleOwner(owner: LifecycleOwner) {
        lifecycleOwner = owner
    }

    private val binding: LayoutHavitEditTextBinding by lazy {
        LayoutHavitEditTextBinding.inflate(LayoutInflater.from(context), this, false)
    }

    init {
        initializeView()
    }

    fun bindEditTextData(editTextData: MutableLiveData<EditTextData>) {
        binding.editTextData = editTextData.value
        binding.lifecycleOwner = lifecycleOwner
    }

    fun setMaxLine(lines: Int) {
        binding.editText.maxLines = lines
    }

    fun setMinLine(lines: Int) {
        binding.editText.isSingleLine = false
        binding.editText.minLines = lines
    }

    fun setGravity(gravity: Int) {
        binding.editText.gravity = gravity
    }

    private fun initializeView() {
        addView(binding.root)
        showClearButton()
    }

    private fun showClearButton() {
        binding.apply {
            editText.addTextChangedListener {
                ivClear.visibility = if (editText.text.isNotEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    fun bindStateEditTextData(editTextData: EditTextData?) {
        binding.editTextData = editTextData
        binding.lifecycleOwner = lifecycleOwner
    }

    fun onTextChangedListener(listener: (String) -> Unit) {
        binding.editText.addTextChangedListener {
            listener(it.toString())
        }
    }

    fun onFocusChangedListener(listener: (Boolean) -> Unit) {
        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            listener(hasFocus)
        }
    }
}