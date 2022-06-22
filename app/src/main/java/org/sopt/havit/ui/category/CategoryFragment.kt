package org.sopt.havit.ui.category

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.sopt.havit.R
import org.sopt.havit.data.remote.CategoryResponse
import org.sopt.havit.databinding.FragmentCategoryBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.contents.ContentsActivity
import org.sopt.havit.util.CustomToast

class CategoryFragment : BaseBindingFragment<FragmentCategoryBinding>(R.layout.fragment_category) {
    private var _categoryAdapter: CategoryAdapter? = null
    private val categoryAdapter get() = _categoryAdapter ?: error("adapter error")
    private val categoryViewModel: CategoryViewModel by lazy { CategoryViewModel(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding.categoryViewModel = categoryViewModel

        initAdapter()
        dataObserve()
        moveManage()
        clickBack()
        clickItemView()
        addCategory()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("onViewCreatedStart", "okay")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _categoryAdapter = null
    }

    private fun initAdapter() {
        _categoryAdapter = CategoryAdapter()
        binding.rvContents.adapter = categoryAdapter
    }

    private fun setData() {
        categoryViewModel.requestCategoryTaken()
    }

    private fun dataObserve() {
        with(categoryViewModel) {
            categoryList.observe(viewLifecycleOwner) {
                // 리싸이클러뷰 업데이트하는 코드
                categoryAdapter.categoryList.clear()
                categoryAdapter.categoryList.addAll(it)
                categoryAdapter.notifyDataSetChanged()
            }
            categoryCount.observe(viewLifecycleOwner) {
                with(binding) {
                    if (it < 0) {
                        sflCategory.startShimmer()
                        sflCount.startShimmer()
                    } else {
                        sflCategory.stopShimmer()
                        sflCount.stopShimmer()
                    }
                    categoryCount = it
                }
            }
        }
    }

    private fun moveManage() {
        binding.tvModify.setOnClickListener {
            val intent = Intent(activity, CategoryOrderModifyActivity::class.java)
            val categoryItemList: ArrayList<CategoryResponse.AllCategoryData> =
                categoryAdapter.categoryList as ArrayList<CategoryResponse.AllCategoryData>
            intent.putParcelableArrayListExtra("categoryItemList", categoryItemList)
            startActivity(intent)
        }
    }

    private fun clickBack() {
        val activityName = requireActivity().javaClass.simpleName.trim()
        if (activityName == "HomeCategoryAllActivity") { // HomeFragment->전체 보기 누른 경우
            Log.d("activity_check", "HomeCategory")
            binding.ivBack.setOnClickListener {
                requireActivity().finish()
            }
        } else { // MainActivity
            binding.ivBack.visibility = View.GONE
            Log.d("activity_check", "Main")
        }
    }

    private fun clickItemView() {
        categoryAdapter.setItemClickListener(object : CategoryAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val intent = Intent(requireActivity(), ContentsActivity::class.java)
                categoryViewModel.categoryList.value?.get(position)
                    ?.let {
                        intent.putExtra(CATEGORY_ID, it.id)
                        intent.putExtra(CATEGORY_NAME, it.title)
                        intent.putExtra(CATEGORY_POSITION, position)
                        intent.putExtra(CATEGORY_IMAGE_ID, it.imageId)
                    }
                startActivity(intent)
            }
        })
    }

    private fun addCategory() {
        binding.clAdd.setOnClickListener {
            if (categoryViewModel.categoryCount.value == CATEGORY_MAX) {
                CustomToast.showTextToast(
                    requireContext(),
                    resources.getString(R.string.max_category)
                )
            } else {
                val intent = Intent(requireActivity(), CategoryAddActivity::class.java)
                startActivity(intent)
            }
        }
    }

    companion object {
        const val CATEGORY_MAX = 23
        const val CATEGORY_ID = "categoryId"
        const val CATEGORY_NAME = "categoryName"
        const val CATEGORY_POSITION = "position"
        const val CATEGORY_IMAGE_ID = "imageId"
    }
}
