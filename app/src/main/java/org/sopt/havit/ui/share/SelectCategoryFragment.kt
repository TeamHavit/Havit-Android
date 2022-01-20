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
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.CategoryResponse
import org.sopt.havit.databinding.FragmentSelectCategoryBinding
import org.sopt.havit.util.MySharedPreference

class SelectCategoryFragment : Fragment() {
    private lateinit var categorySelectableAdapter: CategorySelectableAdapter
    private var _binding: FragmentSelectCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var clickedCategory :Array<Boolean>
    lateinit var categoryData: List<CategoryResponse.AllCategoryData>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectCategoryBinding.inflate(layoutInflater, container, false)

        initView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initView() {

        lifecycleScope.launch {
            try {
                // 서버 통신
                val response =
                    RetrofitObject.provideHavitApi(MySharedPreference.getXAuthToken(requireContext()))
                        .getCategoryNum()
                categoryData = response.data

                Log.d("SelectCategoryFragment", categoryData.toString())
                Log.d("SelectCategoryFragment_len", categoryData.size.toString())

                // Adapter 설정
                categorySelectableAdapter = CategorySelectableAdapter()
                binding.rvCategory.adapter = categorySelectableAdapter
                categorySelectableAdapter.categorySelectableList.addAll(categoryData)
                categorySelectableAdapter.notifyDataSetChanged()

                clickedCategory = Array(categoryData.size+1) { _ -> false }
                Log.d("SelectCategoryFragment_array_size", clickedCategory.size.toString())

                categorySelectableAdapter.setItemClickListener(object :
                    CategorySelectableAdapter.OnItemClickListener {
                    override fun onClick(v: View, position: Int) {
                        Log.d("categorySelectableAdapter", "$position clicked in Fragment")
                        Log.d("SelectCategoryFragment_array_size", clickedCategory.size.toString())
                        clickedCategory[position + 1] = !clickedCategory[position + 1]

                        clickedCategory.forEach{
                            Log.d("SelectCategoryFragment_for","$it")
                        }
                    }
                })
            } catch (e: Exception) {
                // 서버 통신 실패 시
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}