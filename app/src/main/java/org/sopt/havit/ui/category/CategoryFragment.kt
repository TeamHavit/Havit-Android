package org.sopt.havit.ui.category

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import org.sopt.havit.R
import org.sopt.havit.data.remote.CategoryResponse
import org.sopt.havit.databinding.FragmentCategoryBinding
import org.sopt.havit.domain.entity.NetworkState
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.contents.ContentsActivity
import org.sopt.havit.util.MAX_CATEGORY_NUM_EXCEEDED_TOP_TYPE
import org.sopt.havit.util.ToastUtil
import org.sopt.havit.util.setOnSingleClickListener

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

        binding.lifecycleOwner = viewLifecycleOwner
        binding.categoryViewModel = categoryViewModel

        initAdapter()
        dataObserve()
        moveManage()
        clickBack()
        clickItemView()
        addCategory()
        refreshCategoryData()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        requestCategoryData()
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

    private fun requestCategoryData() {
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

            loadState.observe(viewLifecycleOwner) {
                // 서버 불러오는 중이라면 스켈레톤 화면 및 shimmer 효과를 보여줌
                if (it == NetworkState.LOADING) {
                    binding.sflCategory.startShimmer()
                    binding.sflCount.startShimmer()
                } else {
                    binding.sflCategory.stopShimmer()
                    binding.sflCount.stopShimmer()
                }
            }
        }
    }

    private fun moveManage() {
        binding.tvModify.setOnSingleClickListener {
            val intent = Intent(activity, CategoryOrderModifyActivity::class.java)
            val categoryItemList: ArrayList<CategoryResponse.AllCategoryData> =
                categoryAdapter.categoryList as ArrayList<CategoryResponse.AllCategoryData>
            intent.putParcelableArrayListExtra(CATEGORY_ITEM_LIST, categoryItemList)
            startActivity(intent)
        }
    }

    private fun clickBack() {
        val activityName = requireActivity().javaClass.simpleName.trim()
        if (activityName == "HomeCategoryAllActivity") { // HomeFragment->전체 보기 누른 경우
            Log.d("activity_check", "HomeCategory")
            binding.ivBack.setOnSingleClickListener {
                requireActivity().finish()
            }
        } else if (activityName == "ContentsFromMyPageActivity") {
            binding.ivBack.visibility = View.VISIBLE
            binding.ivBack.setOnSingleClickListener {
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
        binding.clAdd.setOnSingleClickListener {
            if (categoryViewModel.categoryCount.value == CATEGORY_MAX)
                ToastUtil(requireContext()).makeToast(MAX_CATEGORY_NUM_EXCEEDED_TOP_TYPE)
            else startActivity(Intent(requireActivity(), CategoryAddActivity::class.java))
        }
    }

    private fun refreshCategoryData() {
        binding.layoutNetworkError.ivRefresh.setOnSingleClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotation_refresh))
            requestCategoryData()
        }
    }

    companion object {
        const val CATEGORY_MAX = 23
        const val CATEGORY_ID = "categoryId"
        const val CATEGORY_NAME = "categoryName"
        const val CATEGORY_POSITION = "position"
        const val CATEGORY_IMAGE_ID = "imageId"
        const val CATEGORY_ITEM_LIST = "categoryItemList"
    }
}
