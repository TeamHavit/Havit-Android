package org.sopt.havit.ui.share

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.koin.android.ext.android.bind
import org.sopt.havit.R
import org.sopt.havit.data.CategorySelectableData
import org.sopt.havit.databinding.FragmentSelectCategoryBinding

class SelectCategoryFragment : Fragment() {
    private lateinit var categorySelectableAdapter: CategorySelectableAdapter
    private var _binding: FragmentSelectCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectCategoryBinding.inflate(layoutInflater, container, false)

        initAdapter()
        initListener()

        return binding.root
    }

    private fun initListener(){
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_selectCategoryFragment_to_contentsSummeryFragment)
        }
    }

    private fun initAdapter() {
        categorySelectableAdapter = CategorySelectableAdapter()
        binding.rvCategory.adapter = categorySelectableAdapter
        categorySelectableAdapter.categorySelectableList.addAll(
            listOf(
                CategorySelectableData(
                    "아키텍처 스터디", dummyImg,false
                ),
                CategorySelectableData(
                    "아키텍처 스터디2", dummyImg,true
                )
            )
        )
        categorySelectableAdapter.notifyDataSetChanged()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val dummyImg =
            "https://user-images.githubusercontent.com/68214704/149118495-e9cc9770-785d-4644-9956-9e17a6641180.png"
    }


}