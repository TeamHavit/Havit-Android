package org.sopt.havit.ui.category

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentCategoryBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.contents.ContentsActivity

class CategoryFragment : BaseBindingFragment<FragmentCategoryBinding>(R.layout.fragment_category) {
    private var _categoryAdapter: CategoryAdapter? = null
    private val categoryAdapter get() = _categoryAdapter ?: error("adapter error")

    private val categoryViewModel: CategoryViewModel by lazy { CategoryViewModel(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding.categoryViewModel = categoryViewModel

        initAdapter()
        //setData()
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
                with(binding) {
                    if (it.isEmpty()) {
                        Log.d("visibility", " success")
                        rvContents.visibility = View.GONE
                        clEmpty.visibility = View.VISIBLE
                    } else {
                        rvContents.visibility = View.VISIBLE
                        clEmpty.visibility = View.GONE
                        Log.d("visibility", " fail")
                    }
                }
                // 리싸이클러뷰 업데이트하는 코드
                categoryAdapter.submitList(it.toList())

            }
            categoryCount.observe(viewLifecycleOwner) {
                binding.tvCount.text = it.toString()
            }
        }
    }

    private fun moveManage() {
        binding.tvModify.setOnClickListener {
            Log.d("setClick", " success")
            val intent = Intent(activity, CategoryOrderModifyActivity::class.java)
            intent.putExtra("dataSet", true)
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
        } else {  // MainActivity
            binding.ivBack.visibility = View.GONE
            Log.d("activity_check", "Main")
        }
    }

    private fun clickItemView() {
        categoryAdapter.setItemClickListener(object : CategoryAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                // ContentsFragment -> ContentsActivity로 바꾸고 ContentsActivity로 이동
                val intent = Intent(requireActivity(), ContentsActivity::class.java)
                categoryViewModel.categoryList.value?.get(position)
                    ?.let {
                        intent.putExtra("categoryId", it.id)
                        intent.putExtra("categoryName", it.title)
                    }
                startActivity(intent)
            }
        })
    }

    private fun addCategory(){
        binding.clAdd.setOnClickListener {
            val intent = Intent(requireActivity(), CategoryAddActivity::class.java)
            startActivity(intent)
        }
    }
}