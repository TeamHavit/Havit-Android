package org.sopt.havit.ui.share

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentAddCategoryBinding
import org.sopt.havit.util.KeyBoardUtil

class AddCategoryFragment : Fragment() {

    private var _binding: FragmentAddCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddCategoryBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListener()
        setTextWatcher()
        setKeyBoardUp()

        // 키보드에 맞게 뷰 조절 (다음 버튼 키보드 위 배치)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private fun setTextWatcher() {
        binding.etCategoryTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setBtnColor()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setBtnColor()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    private fun setBtnColor() {
        if (isTitleNull()) {
            binding.btnNext.setBackgroundColor(resources.getColor(R.color.gray_2))
            binding.btnNext.isEnabled = false
        } else {
            binding.btnNext.setBackgroundColor(resources.getColor(R.color.havit_purple))
            binding.btnNext.isEnabled = true
        }
    }

    private fun initClickListener() {
        binding.btnNext.setOnClickListener { findNavController().navigate(R.id.action_addCategoryFragment_to_chooseIconFragment) }
    }

    private fun isTitleNull(): Boolean = binding.etCategoryTitle.text.isEmpty()

    private fun setKeyBoardUp() =
        KeyBoardUtil.openKeyBoard(requireContext(), binding.etCategoryTitle)
}