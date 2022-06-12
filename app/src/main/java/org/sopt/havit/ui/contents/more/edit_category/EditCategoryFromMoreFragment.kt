package org.sopt.havit.ui.contents.more.edit_category

import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.data.remote.ContentsMoreData
import org.sopt.havit.databinding.FragmentEditCategoryFromMoreBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.contents.more.BottomSheetMoreFragment
import org.sopt.havit.util.DialogUtil
import org.sopt.havit.util.OnBackPressedHandler

@AndroidEntryPoint
class EditCategoryFromMoreFragment :
    BaseBindingFragment<FragmentEditCategoryFromMoreBinding>(R.layout.fragment_edit_category_from_more),
    OnBackPressedHandler {
    private lateinit var categoryAdapter: EditCategoryAdapter
    private val viewModel: EditCategoryFromMoreViewModel by viewModels()
    private lateinit var bottomSheetDialogFragment: BottomSheetDialogFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel

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
            Log.d(TAG, "onCategoryClick: observe")
            categoryAdapter.submitList(list)
        }
    }

    private fun onCategoryClick(position: Int) {
        viewModel.toggleItemSelected(position)
        categoryAdapter.submitList(viewModel.categoryList.value)
        Log.d(TAG, "onCategoryClick:$position ${categoryAdapter.currentList[position].isSelected}")
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
            if (viewModel.isCategoryModified())
                viewModel.patchNewCategoryList()
            else dismissBottomSheet()
        }
    }

    private fun onCloseClicked() {
        binding.icClose.setOnClickListener {
            if (viewModel.isCategoryModified())
                showEditCategoryWarningDialog()
            else dismissBottomSheet()
        }
    }

    override fun onBackPressed(): Boolean {
        if (viewModel.isCategoryModified()) {
            showEditCategoryWarningDialog()
            return true
        }
        return false
    }

    private fun showEditCategoryWarningDialog() {
        val dialog = DialogUtil(DialogUtil.CANCEL_EDIT_CATEGORY, ::dismissBottomSheet)
        dialog.show(requireActivity().supportFragmentManager, this.javaClass.name)
    }

    private fun dismissBottomSheet() {
        bottomSheetDialogFragment.dismiss()
    }
}
