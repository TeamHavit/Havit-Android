package org.sopt.havit.ui.share.select_category

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentSelectCategoryBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.contents.more.edit_category.SelectableCategoryAdapter
import org.sopt.havit.ui.share.ShareViewModel
import org.sopt.havit.util.setOnSingleClickListener

@AndroidEntryPoint
class SelectCategoryFragment :
    BaseBindingFragment<FragmentSelectCategoryBinding>(R.layout.fragment_select_category) {

    private val viewModel: ShareViewModel by activityViewModels()

    private lateinit var categoryAdapter: SelectableCategoryAdapter
    lateinit var clickCountList: Array<Boolean>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.selectCategory.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        getCategoryData()
        initAdapter()
        setCategoryDataOnAdapter()
        initListener()
        toolbarClickListener()
        onClickRefreshButtonOnNetworkError()
    }

    private fun getCategoryData() {
        viewModel.getCategoryData()
    }

    private fun toolbarClickListener() {
        binding.icClose.setOnSingleClickListener {
            requireActivity().finish()
        }
        binding.networkErrorLayout.ibClose.setOnSingleClickListener {
            requireActivity().finish()
        }
    }

    private fun onClickRefreshButtonOnNetworkError() {
        binding.networkErrorLayout.ivRefresh.setOnSingleClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotation_refresh))
            viewModel.getCategoryData()
        }
    }

    private fun initAdapter() {
        binding.selectCategory.rvCategory.adapter =
            SelectableCategoryAdapter(::onCategoryClick).also { categoryAdapter = it }

    }

    private fun onCategoryClick(position: Int) {
        viewModel.onCategoryClick(position)
        categoryAdapter.notifyItemChanged(position)
    }

    private fun setCategoryDataOnAdapter() {
        viewModel.categoryList.observe(viewLifecycleOwner) { list ->
            categoryAdapter.submitList(list)
        }
    }

    private fun initListener() {
        binding.selectCategory.btnNext.setOnClickListener {
            findNavController().navigate(
                SelectCategoryFragmentDirections.actionSelectCategoryFragmentToContentsSummeryFragment()
            )
        }

        // 카테고리 추가 버튼
        binding.selectCategory.ivCategoryAdd.setOnClickListener {
            if ((viewModel.categoryNum.value ?: 0) >= MAX_CATEGORY_NUM) showCategoryMaxToast()
            else findNavController().navigate(R.id.action_selectCategoryFragment_to_enterCategoryTitleFragment)
        }

        binding.noCategory.btnAddCategory.setOnClickListener {
            findNavController().navigate(R.id.action_selectCategoryFragment_to_enterCategoryTitleFragment)
        }
    }

    private fun showCategoryMaxToast() {
        val toast = Toast(requireContext())
        toast.view = layoutInflater.inflate(R.layout.toast_max_category, null)
        toast.show()
    }


    companion object {
        private const val MAX_CATEGORY_NUM = 23
    }
}
