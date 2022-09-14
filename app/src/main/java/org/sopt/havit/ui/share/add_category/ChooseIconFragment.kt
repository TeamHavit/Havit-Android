package org.sopt.havit.ui.share.add_category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentChooseIconBinding
import org.sopt.havit.domain.model.NetworkStatus
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.category.CategoryAddActivity
import org.sopt.havit.ui.share.AddCategoryViewModel
import org.sopt.havit.ui.share.add_category.IconAdapter.Companion.clickedPosition
import org.sopt.havit.util.ADD_CATEGORY_TYPE
import org.sopt.havit.util.ToastUtil
import org.sopt.havit.util.setOnSinglePostClickListener

@AndroidEntryPoint
class ChooseIconFragment :
    BaseBindingFragment<FragmentChooseIconBinding>(R.layout.fragment_choose_icon) {

    private val viewModel: AddCategoryViewModel by activityViewModels()
    private lateinit var iconAdapter: IconAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initClickNext()
        toolbarClickListener()
        observeNetworkStatus()
    }

    private fun toolbarClickListener() {
        binding.icBack.setOnClickListener { findNavController().popBackStack() }
        binding.icClose.setOnClickListener { requireActivity().finish() }
    }

    private fun initAdapter() {
        clickedPosition = viewModel.selectedIconPosition.value ?: 0
        binding.rvIcon.adapter = IconAdapter(::onIconClick).also { iconAdapter = it }
    }

    private fun onIconClick(position: Int) {
        val previousPosition = clickedPosition
        clickedPosition = position
        iconAdapter.notifyItemChanged(previousPosition)
        iconAdapter.notifyItemChanged(clickedPosition)
        viewModel.setSelectedIconPosition(position)
    }

    private fun initClickNext() {
        binding.btnNext.setOnSinglePostClickListener {
            viewModel.addCategory()
        }
    }

    private fun observeNetworkStatus() {
        viewModel.addCategoryViewState.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkStatus.Success -> {
                    showCustomToast()
                    navigateView()
                }
                is NetworkStatus.Error -> {} // todo show error view
                else -> return@observe
            }
        }
    }

    private fun navigateView() {
        val currentParent = requireActivity()::class.java.simpleName
        val categoryActivity = CategoryAddActivity::class.java.simpleName
        if (currentParent == categoryActivity)
            requireActivity().finish()
        findNavController().navigate(R.id.action_chooseIconFragment_to_selectCategoryFragment)
    }

    private fun showCustomToast() {
        ToastUtil(requireContext()).makeToast(
            toastType = ADD_CATEGORY_TYPE,
            categoryName = viewModel.categoryTitle.value ?: throw IllegalStateException()
        )
    }
}
