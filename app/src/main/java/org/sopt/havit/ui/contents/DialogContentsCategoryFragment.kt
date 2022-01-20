package org.sopt.havit.ui.contents

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentDialogContentsCategoryBinding
import org.sopt.havit.ui.category.CategoryAdapter
import org.sopt.havit.ui.web.WebActivity

class DialogContentsCategoryFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentDialogContentsCategoryBinding? = null
    val binding get() = _binding!!
    private var _contentsCategoryAdapter: ContentsCategoryAdapter? = null
    private val contentsCategoryAdapter get() = _contentsCategoryAdapter ?: error("adapter error")

    private val contentsCategoryViewModel: ContentsCategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_contents_category, container, false)
        binding.contentsCategoryViewModel = contentsCategoryViewModel

        initAdapter()
        setData()
        dataObserve()
        decorationView()
        clickCategory()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAdapter() {
        _contentsCategoryAdapter = ContentsCategoryAdapter()
        binding.rvCategoryList.adapter = contentsCategoryAdapter
    }

    private fun setData() {
        contentsCategoryViewModel.requestCategoryTaken()
    }

    private fun dataObserve() {
        with(contentsCategoryViewModel) {
            contentsCategoryList.observe(viewLifecycleOwner) {
                contentsCategoryAdapter.contentsCategoryList.addAll(it)
                contentsCategoryAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun decorationView() {
        binding.rvCategoryList.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun clickCategory(){
        contentsCategoryAdapter.setItemCategoryClickListener(object : ContentsCategoryAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val intent = Intent(requireActivity(), ContentsActivity::class.java)
                contentsCategoryViewModel.contentsCategoryList.value?.get(position)
                    ?.let {
                        intent.putExtra("categoryId", it.id)
                        intent.putExtra("categoryName", it.title)
                    }
                startActivity(intent)
                requireActivity().finish()
            }
        })
    }
}