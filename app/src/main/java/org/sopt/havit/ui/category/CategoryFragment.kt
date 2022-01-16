package org.sopt.havit.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import org.sopt.havit.R
import org.sopt.havit.data.CategoryData
import org.sopt.havit.databinding.FragmentCategoryBinding
import org.sopt.havit.ui.base.BaseBindingFragment

class CategoryFragment : BaseBindingFragment<FragmentCategoryBinding>(R.layout.fragment_category) {
    private var _categoryAdapter: CategoryAdapter? = null
    private val categoryAdapter get() = _categoryAdapter ?: error("adapter error")

    private val categoryViewModel: CategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding.categoryViewModel = categoryViewModel

        initAdapter()
        setData()
        dataObserve()
        moveManage()
        clickBack()
        clickItemView()

        return binding.root
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
        val list = mutableListOf<CategoryData>()
        for (i in 1..15) {
            list.add(
                CategoryData(
                    "UX/UI 아티클",
                    "https://user-images.githubusercontent.com/68214704/149118495-e9cc9770-785d-4644-9956-9e17a6641180.png"
                )
            )
        }
        categoryViewModel.requestCategoryTaken(list, list.size.toString())
    }

    private fun dataObserve() {
        with(categoryViewModel) {
            categoryList.observe(viewLifecycleOwner) {
                categoryAdapter.categoryList.addAll(it)
                categoryAdapter.notifyDataSetChanged()
            }
            categoryCount.observe(viewLifecycleOwner) {
                binding.tvCount.text = it
            }
        }
    }

    private fun moveManage() {
        binding.tvModify.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_category_to_categoryOrderModifyFragment)
        }
    }

    private fun clickBack() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun clickItemView() {
        categoryAdapter.setItemClickListener(object : CategoryAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                // ContentsFragment -> ContentsActivity로 바꾸고 ContentsActivity로 이동
//                val intent = Intent(requireActivity(), ContentsActivity::class.java)
//                startActivity(intent)
            }
        })
    }
}