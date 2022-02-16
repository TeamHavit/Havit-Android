package org.sopt.havit.ui.share

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
    lateinit var categoryData: List<CategoryResponse.AllCategoryData>
    private var categoryNum: Int = 0
    lateinit var clickCountList: Array<Boolean>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectCategoryBinding.inflate(layoutInflater, container, false)

        initView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        toolbarClickListener()
    }

    private fun toolbarClickListener() {
        binding.icClose.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun initView() {

        lifecycleScope.launch {
            try {
                // 서버 통신
                val response =
                    RetrofitObject.provideHavitApi(
                        MySharedPreference.getXAuthToken(requireContext())
                    ).getCategoryNum()
                categoryData = response.data
                categoryNum = categoryData.size

                Log.d("SelectCategoryFragment", categoryData.toString())
                Log.d("SelectCategoryFragment_len", categoryData.size.toString())

                // Adapter 설정
                categorySelectableAdapter = CategorySelectableAdapter()
                binding.rvCategory.adapter = categorySelectableAdapter
                categorySelectableAdapter.categorySelectableList.addAll(categoryData)
                categorySelectableAdapter.notifyDataSetChanged()

                // Adapter 내의 clickCategory 초기화
                categorySelectableAdapter.clickedCategory = Array(categoryData.size) { _ -> false }
                clickCountList = Array(categoryData.size) { _ -> false }
                Log.d("clickCountList", categoryData.size.toString())

                categorySelectableAdapter.setItemClickListener(object :
                    CategorySelectableAdapter.OnItemClickListener {
                    override fun onClick(v: View, position: Int) {

                        clickCountList[position] = !clickCountList[position]

                        var booleanLog = clickCountList[position]
                        Log.d("clickCountList", booleanLog.toString())

                        if (isSelectedLeastOneCategory()) {
                            binding.btnNext.isEnabled = true
                            binding.btnNext.setBackgroundResource(R.drawable.rectangle_purple)
                        } else {
                            binding.btnNext.isEnabled = false
                            binding.btnNext.setBackgroundResource(R.drawable.rectangle_gray_2)
                        }
                    }
                })
            } catch (e: Exception) {
                // 서버 통신 실패 시
            }
        }
    }

    private fun isSelectedLeastOneCategory(): Boolean {

        val cateSize = clickCountList.size
        Log.d("isSelectedLeastOneCategory_size", cateSize.toString())
        var isMoreThanOne = false
        for (i in 0 until cateSize) {
            if (clickCountList[i]) {
                isMoreThanOne = true
                Log.d("isSelectedLeastOneCategory_for", "check for statement")
                break
            }
        }
        Log.d("isSelectedLeastOneCategory_return", isMoreThanOne.toString())
        return isMoreThanOne
    }

    private fun initListener() {
        // 하단 다음 버튼
        binding.btnNext.setOnClickListener {
            Log.d("getSelectedCategoryNum", getSelectedCategoryNum())
            // 카테고리 리스트 스트링으로 변경하여 전송
            findNavController().navigate(
                SelectCategoryFragmentDirections.actionSelectCategoryFragmentToContentsSummeryFragment(
                    getSelectedCategoryNum()
                )
            )
        }

        // 카테고리 추가 버튼
        binding.ivCategoryAdd.setOnClickListener {
            if (categoryNum >= MAX_CATEGORY_NUM)       // 카테고리 추가 불가 Toast
                showCategoryMaxToast()
            else                                       // 카테고리 추가 뷰로 이동
                findNavController().navigate(R.id.action_selectCategoryFragment_to_addCategoryFragment)
        }
    }

    private fun showCategoryMaxToast() {
        val toast = Toast(requireContext())
        toast.view = layoutInflater.inflate(R.layout.toast_max_category, null)
        toast.show()
    }

    private fun getSelectedCategoryNum(): String {
        var selectedCategory = ""
        for (i in clickCountList.indices) {
            if (clickCountList[i]) {
                val cateId = categoryData[i].id
                selectedCategory += ("$cateId ")
            }
        }
        return selectedCategory
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val MAX_CATEGORY_NUM = 23
    }
}