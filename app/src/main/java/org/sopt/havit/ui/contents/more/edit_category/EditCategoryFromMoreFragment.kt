package org.sopt.havit.ui.contents.more.edit_category

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.data.remote.ContentsMoreData
import org.sopt.havit.databinding.FragmentEditCategoryFromMoreBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.contents.more.BottomSheetMoreFragment
import org.sopt.havit.ui.contents.more.edit_category.EditCategoryFromMoreViewModel.Companion.PATCH_CATEGORY
import org.sopt.havit.util.EventObserver
import org.sopt.havit.util.OnBackPressedHandler

@AndroidEntryPoint
class EditCategoryFromMoreFragment :
    BaseBindingFragment<FragmentEditCategoryFromMoreBinding>(R.layout.fragment_edit_category_from_more),
    OnBackPressedHandler {
    private val viewModel: EditCategoryFromMoreViewModel by viewModels()
    private lateinit var categoryAdapter: EditCategoryAdapter
    private lateinit var bottomSheetDialogFragment: BottomSheetDialogFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.initProperty(getBundleData() as ContentsMoreData)
        getDataFromServer()
        initRvAdapter()
        initRvList()
        initCompleteBtnClick()
        initBottomSheetDialogFragment()
        onCloseClicked()
    }

    private fun getDataFromServer() {
        viewModel.getCategoryListWithSelectedInfo()
    }

    private fun initRvAdapter() {
        binding.rvCategory.adapter =
            EditCategoryAdapter(::onCategoryClick).also { categoryAdapter = it }
    }

    private fun initRvList() {
        viewModel.categoryList.observe(viewLifecycleOwner) { list ->
            categoryAdapter.submitList(list)
        }
    }

    private fun onCategoryClick(position: Int) {
        viewModel.onCategoryClick(position)
        categoryAdapter.notifyItemChanged(position)
    }

    private fun getBundleData(): Parcelable? {
        arguments?.let { return it.getParcelable(BottomSheetMoreFragment.CONTENTS_DATA) }
        throw IllegalArgumentException()
    }

    private fun initBottomSheetDialogFragment() {
        bottomSheetDialogFragment = requireParentFragment() as BottomSheetDialogFragment
    }

    private fun initCompleteBtnClick() {
        binding.btnComplete.setOnClickListener {
            if (viewModel.isCategoryModified()) {
                viewModel.patchNewCategoryList()
                viewModel.isNetworkCorrespondenceEnd.observe( // ???????????? ????????? ?????? ??? dismiss
                    requireActivity(),
                    EventObserver {
                        if (it == PATCH_CATEGORY) dismissBottomSheet()
                    }
                )
            } else dismissBottomSheet()
        }
    }

    private fun onCloseClicked() {
        binding.icClose.setOnClickListener { dismissBottomSheet() }
    }

    override fun onBackPressed(): Boolean {
        if (viewModel.isCategoryModified())
            return true
        return false
    }

    private fun dismissBottomSheet() {
        bottomSheetDialogFragment.dismiss()
    }
}
