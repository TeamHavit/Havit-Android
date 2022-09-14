package org.sopt.havit.ui.share.add_category

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentEnterCategoryTitleBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.share.AddCategoryViewModel
import org.sopt.havit.util.KeyBoardUtil
import org.sopt.havit.util.KeyBoardUtil.setUpAsSoftKeyboard

@AndroidEntryPoint
class EnterCategoryTitleFragment :
    BaseBindingFragment<FragmentEnterCategoryTitleBinding>(R.layout.fragment_enter_category_title) {

    private val viewModel: AddCategoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getExistingCategoryList()
        initClickListener()
        setTextWatcher()
        setKeyBoardUp()
        toolbarClickListener()
        setUpAsSoftKeyboard(view) // 다음버튼 위/아래 움직이게
    }

    private fun getExistingCategoryList() {
        viewModel.getExistingCategoryList()
    }

    private fun setTextWatcher() {
        binding.etCategoryTitle.addTextChangedListener {
            val currentTitle = binding.etCategoryTitle.text.toString().trim()
            updateIsDuplicateState(currentTitle)
        }
    }

    private fun updateIsDuplicateState(currentTitle: String) {
        binding.isDuplicated = viewModel.isCategoryNameAlreadyExist(currentTitle)
    }

    private fun initClickListener() {
        binding.btnNext.setOnClickListener {
            viewModel.setCategoryTitle(binding.etCategoryTitle.text.toString())
            findNavController().navigate(
                R.id.action_enterCategoryTitleFragment_to_chooseIconFragment
            )
        }
        binding.ivDeleteText.setOnClickListener { binding.etCategoryTitle.text.clear() }
    }

    private fun toolbarClickListener() {
        binding.icBack.setOnClickListener { findNavController().popBackStack() }
        binding.icClose.setOnClickListener { requireActivity().finish() }
    }

    private fun setKeyBoardUp() =
        KeyBoardUtil.openKeyBoard(requireContext(), binding.etCategoryTitle)

    companion object {
        const val TAG = "AddCategoryFragment"
    }
}
