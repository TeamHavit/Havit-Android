package org.sopt.havit.ui.share

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.sopt.havit.R
import org.sopt.havit.data.CategoryData
import org.sopt.havit.data.CategorySelectableData
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.CategoryResponse
import org.sopt.havit.databinding.FragmentSelectCategoryBinding
import org.sopt.havit.util.MySharedPreference

class SelectCategoryFragment : Fragment() {
    private lateinit var categorySelectableAdapter: CategorySelectableAdapter
    private var _binding: FragmentSelectCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectCategoryBinding.inflate(layoutInflater, container, false)

//        initAdapter()
        getCategoryData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun getCategoryData() {
        lateinit var categoryData: List<CategoryResponse.AllCategoryData>
        lifecycleScope.launch {
            try {
                val response =
                    RetrofitObject.provideHavitApi(MySharedPreference.getXAuthToken(requireContext()))
                        .getCategoryNum()
                categoryData = response.data
                Log.d("SelectCategoryFragment", categoryData.toString())
                Log.d("SelectCategoryFragment_len", categoryData.size.toString())

                categorySelectableAdapter = CategorySelectableAdapter()
                binding.rvCategory.adapter = categorySelectableAdapter
                categorySelectableAdapter.categorySelectableList.addAll(categoryData)
                categorySelectableAdapter.notifyDataSetChanged()
            } catch (e: Exception) {
            }
        }
    }

    private fun initListener() {
        // 하단 다음 버튼
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_selectCategoryFragment_to_contentsSummeryFragment)
        }

        // 상단 카테고리 추가 버튼
        binding.ivCategoryAdd.setOnClickListener {
            findNavController().navigate(R.id.action_selectCategoryFragment_to_addCategoryFragment)
        }
    }

    private fun initAdapter() {

//        val categoryData: List<CategoryResponse.AllCategoryData> = getCategoryData()
//        var categorySelectableData = mutableListOf<CategorySelectableData>()
//
//        for (i in 0..categoryData.size) {
//            categorySelectableData[i].categoryName = categoryData[i].title
//            categorySelectableData[i].icon = categoryData[i].url
//            categorySelectableData[i].id = categoryData[i].id
//            categorySelectableData[i].isSelect = false
//        }
//
//
//        categorySelectableAdapter = CategorySelectableAdapter()
//        binding.rvCategory.adapter = categorySelectableAdapter
//        categorySelectableAdapter.categorySelectableList.addAll(categorySelectableData)
//        categorySelectableAdapter.notifyDataSetChanged()

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