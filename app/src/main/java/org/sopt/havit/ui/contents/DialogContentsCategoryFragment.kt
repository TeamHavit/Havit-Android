package org.sopt.havit.ui.contents

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.havit.R
import org.sopt.havit.data.remote.CategoryResponse
import org.sopt.havit.databinding.FragmentDialogContentsCategoryBinding


class DialogContentsCategoryFragment(
    private val categoryList: ArrayList<CategoryResponse.AllCategoryData>,
    private val categoryName: String,
    private val categoryId: Int
) : BottomSheetDialogFragment() {
    private var _binding: FragmentDialogContentsCategoryBinding? = null
    val binding get() = _binding!!
    private var _contentsCategoryAdapter: ContentsCategoryAdapter? = null
    private val contentsCategoryAdapter get() = _contentsCategoryAdapter ?: error("adapter error")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dialog_contents_category,
            container,
            false
        )

        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAdapter()
        setData()
        decorationView()
        clickCategory()
        setScrollTouchArea()
        setBottomSheetHeight()
    }

    private fun setBottomSheetHeight() {
        (dialog as BottomSheetDialog).behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED      // 높이 고정
            skipCollapsed = true                            // HALF_EXPANDED 안되게 설정
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

    private fun setData() {
        binding.categoryCount = categoryList.size
        contentsCategoryAdapter.contentsCategoryList.addAll(categoryList)
        contentsCategoryAdapter.notifyDataSetChanged()
    }

    private fun decorationView() {
        binding.rvCategoryList.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    // 클릭한 카테고리로 이동
    private fun clickCategory() {
        contentsCategoryAdapter.setItemCategoryClickListener(object :
            ContentsCategoryAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val intent = Intent(requireActivity(), ContentsActivity::class.java)
                contentsCategoryAdapter.contentsCategoryList[position]
                    .let {
                        intent.putExtra("categoryId", it.id)
                        intent.putExtra("categoryName", it.title)
                    }
                startActivity(intent)
                requireActivity().finish()
            }
        })
    }

    private fun setScrollTouchArea() {
        binding.rvCategoryList.setOnTouchListener { v, event ->
            binding.clBottomSheet.requestDisallowInterceptTouchEvent(true)
            false
        }
    }
}