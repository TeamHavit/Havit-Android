package org.sopt.havit.ui.category

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentCategoryBinding
import org.sopt.havit.domain.entity.Category
import org.sopt.havit.domain.entity.NetworkState
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.contents.ContentsActivity
import org.sopt.havit.util.MAX_CATEGORY_NUM_EXCEEDED_TOP_TYPE
import org.sopt.havit.util.ToastUtil
import org.sopt.havit.util.setOnSingleClickListener

@AndroidEntryPoint
class CategoryFragment : BaseBindingFragment<FragmentCategoryBinding>(R.layout.fragment_category) {
    private val viewModel by viewModels<CategoryViewModel>()
    private val adapter by lazy {
        CategoryAdapter(
            onItemClick = { category -> startContentsActivity(category) }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        initView()
        observe()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllCategories()
    }

    private fun initView() {
        binding.rvContents.adapter = adapter

        binding.ivBack.isGone = requireActivity().javaClass.simpleName.trim() == MAIN_ACTIVITY
        binding.ivBack.setOnSingleClickListener {
            requireActivity().finish()
        }

        binding.clAdd.setOnSingleClickListener {
            if (viewModel.categoryCount.value == CATEGORY_MAX)
                ToastUtil(requireContext()).makeToast(MAX_CATEGORY_NUM_EXCEEDED_TOP_TYPE)
            else
                startActivity(Intent(requireActivity(), CategoryAddActivity::class.java))
        }

        binding.layoutNetworkError.ivRefresh.setOnSingleClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotation_refresh))
            viewModel.getAllCategories()
        }

        binding.tvModify.setOnSingleClickListener {
            startCategoryOrderModifyActivity()
        }
    }

    private fun observe() {
        with(viewModel) {
            categoryList.observe(viewLifecycleOwner) {
                // 리싸이클러뷰 업데이트하는 코드
                adapter.categoryList.clear()
                adapter.categoryList.addAll(it)
                adapter.notifyDataSetChanged()
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

    private fun startContentsActivity(category: Category) {
        val intent = Intent(requireActivity(), ContentsActivity::class.java).apply {
            putExtra(CATEGORY_ID, category.id)
            putExtra(CATEGORY_NAME, category.title)
            putExtra(CATEGORY_POSITION, category.orderIndex)
            putExtra(CATEGORY_IMAGE_ID, category.imageId)
        }
        startActivity(intent)
    }

    private fun startCategoryOrderModifyActivity() {
        val intent = Intent(requireActivity(), CategoryOrderModifyActivity::class.java)
        val categoryItemList = adapter.categoryList as ArrayList<Category>
        intent.putParcelableArrayListExtra(CATEGORY_ITEM_LIST, categoryItemList)
        startActivity(intent)
    }

    companion object {
        const val CATEGORY_MAX = 23
        const val CATEGORY_ID = "categoryId"
        const val CATEGORY_NAME = "categoryName"
        const val CATEGORY_POSITION = "position"
        const val CATEGORY_IMAGE_ID = "imageId"
        const val CATEGORY_ITEM_LIST = "categoryItemList"
        const val MAIN_ACTIVITY = "MainActivity"
    }
}
