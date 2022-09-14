package org.sopt.havit.ui.share.add_category

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentEnterCategoryTitleBinding
import org.sopt.havit.domain.model.NetworkStatus
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.share.AddCategoryViewModel
import org.sopt.havit.util.KeyBoardUtil
import org.sopt.havit.util.KeyBoardUtil.setUpAsSoftKeyboard
import org.sopt.havit.util.setOnSingleClickListener

@AndroidEntryPoint
class EnterCategoryTitleFragment :
    BaseBindingFragment<FragmentEnterCategoryTitleBinding>(R.layout.fragment_enter_category_title) {

    private val viewModel: AddCategoryViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        getExistingCategoryList()
        initNormalViewClickListener()
        initErrorViewClickListener()
        setTextWatcher()
        setKeyBoardState()
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

    private fun initNormalViewClickListener() {
        binding.btnNext.setOnClickListener {
            viewModel.setCategoryTitle(binding.etCategoryTitle.text.toString())
            findNavController().navigate(
                R.id.action_enterCategoryTitleFragment_to_chooseIconFragment
            )
        }
        binding.ivDeleteText.setOnClickListener { binding.etCategoryTitle.text.clear() }
    }

    private fun initErrorViewClickListener() {
        with(binding.layoutNetworkError) {
            ibClose.setOnSingleClickListener { requireActivity().finish() }
            ivRefresh.setOnSingleClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotation_refresh))
                getExistingCategoryList()
            }
        }
    }

    private fun toolbarClickListener() {
        binding.icBack.setOnSingleClickListener { findNavController().popBackStack() }
        binding.icClose.setOnSingleClickListener { requireActivity().finish() }
    }

    private fun setKeyBoardState() =
        viewModel.enterCategoryNameViewState.observe(viewLifecycleOwner) {
            if (it == NetworkStatus.Success())
                KeyBoardUtil.openKeyBoard(requireContext(), binding.etCategoryTitle)
            else KeyBoardUtil.hideKeyBoard(requireActivity())
        }
}
