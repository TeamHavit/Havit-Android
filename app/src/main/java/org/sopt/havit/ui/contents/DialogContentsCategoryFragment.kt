package org.sopt.havit.ui.contents

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.havit.R
import org.sopt.havit.data.remote.CategoryResponse
import org.sopt.havit.databinding.FragmentDialogContentsCategoryBinding
import org.sopt.havit.ui.category.CategoryFragment.Companion.CATEGORY_ID
import org.sopt.havit.ui.category.CategoryFragment.Companion.CATEGORY_ITEM_LIST

class DialogContentsCategoryFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentDialogContentsCategoryBinding? = null
    val binding get() = _binding!!
    private var _contentsCategoryAdapter: ContentsCategoryAdapter? = null
    private val contentsCategoryAdapter get() = _contentsCategoryAdapter ?: error("adapter error")
    private lateinit var categoryList: ArrayList<CategoryResponse.AllCategoryData>
    private var categoryId = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dialog_contents_category,
            container,
            false
        )

        categoryList = requireArguments().getParcelableArrayList<CategoryResponse.AllCategoryData>(
            CATEGORY_ITEM_LIST
        ) as ArrayList<CategoryResponse.AllCategoryData>
        categoryId = requireArguments().getInt(CATEGORY_ID)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAdapter()
        setCategoryItemListData()
        setRecyclerViewDecoration()
        setCategoryItemClickListener()
        setBottomSheetHeight()
    }

    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme

    private fun setBottomSheetHeight() {
        (dialog as BottomSheetDialog).behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED // 높이 고정
            skipCollapsed = true // HALF_EXPANDED 안되게 설정
        }

        // 휴대폰 화면의 0.94배 높이
        binding.clBottomSheet.layoutParams.height =
            (resources.displayMetrics.heightPixels * 0.94).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAdapter() {
        _contentsCategoryAdapter = ContentsCategoryAdapter(categoryId)
        binding.rvCategoryList.adapter = contentsCategoryAdapter
    }

    private fun setCategoryItemListData() {
        contentsCategoryAdapter.contentsCategoryList.addAll(categoryList)
        contentsCategoryAdapter.notifyDataSetChanged()
    }

    private fun setRecyclerViewDecoration() {
        val dividerItemDecoration = DividerItemDecoration(
            context,
            LinearLayoutManager.VERTICAL
        )
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.line_gray_0
            )!!
        )

        binding.rvCategoryList.addItemDecoration(dividerItemDecoration)
    }

    // 클릭한 카테고리로 이동
    private fun setCategoryItemClickListener() {
        contentsCategoryAdapter.setItemCategoryClickListener(object :
                ContentsCategoryAdapter.OnItemClickListener {
                override fun onClick(v: View, position: Int) {
                    setCategoryItemBackGroundColor(v)
                    moveSelectedCategoryContents(position)
                }
            })
    }

    private fun setCategoryItemBackGroundColor(view: View) {
        view.findViewById<View>(R.id.cl_contents_dialog_top).isSelected = true
    }

    private fun moveSelectedCategoryContents(position: Int) {
        val intent = Intent(requireActivity(), ContentsActivity::class.java)
        contentsCategoryAdapter.contentsCategoryList[position]
            .let {
                intent.putExtra("categoryId", it.id)
                intent.putExtra("categoryName", it.title)
            }
        startActivity(intent)
        requireActivity().finish()
    }
}
