package org.sopt.havit.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.sopt.havit.data.CategoryData
import org.sopt.havit.databinding.FragmentCategoryOrderModifyBinding

class CategoryOrderModifyFragment : Fragment() {
    private var _binding: FragmentCategoryOrderModifyBinding? = null
    private val binding get() = _binding!!

    private var _categoryOrderModifyAdapter: CategoryOrderModifyAdapter? = null
    private val categoryOrderModifyAdapter get() = _categoryOrderModifyAdapter ?: error("adapter error")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryOrderModifyBinding.inflate(inflater, container, false)

        initAdapter()
        setData()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAdapter() {
        _categoryOrderModifyAdapter = CategoryOrderModifyAdapter()
        binding.rvContents.adapter = categoryOrderModifyAdapter
    }

    private fun setData() {
        for (i in 1..15) {
            categoryOrderModifyAdapter.categoryList.add(
                CategoryData(
                    "UX/UI 아티클 * ",
                    "https://user-images.githubusercontent.com/68214704/149118495-e9cc9770-785d-4644-9956-9e17a6641180.png"
                )
            )
        }
    }
}