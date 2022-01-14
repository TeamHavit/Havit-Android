package org.sopt.havit.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.data.CategoryData
import org.sopt.havit.databinding.FragmentCategoryOrderModifyBinding

class CategoryOrderModifyFragment : Fragment() {
    private var _binding: FragmentCategoryOrderModifyBinding? = null
    private val binding get() = _binding!!

    private var _categoryOrderModifyAdapter: CategoryOrderModifyAdapter? = null
    private val categoryOrderModifyAdapter
        get() = _categoryOrderModifyAdapter ?: error("adapter error")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryOrderModifyBinding.inflate(inflater, container, false)

        initAdapter()
        setData()
        clickBack()
        initDrag()
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
                    "UX/UI 아티클 $i ",
                    "https://user-images.githubusercontent.com/68214704/149118495-e9cc9770-785d-4644-9956-9e17a6641180.png"
                )
            )
        }
    }

    private fun clickBack() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initDrag() {
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition: Int = viewHolder.adapterPosition
                val toPosition: Int = target.adapterPosition
                categoryOrderModifyAdapter.swapData(fromPosition, toPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                categoryOrderModifyAdapter.removeData((viewHolder.layoutPosition))
            }
        }
        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(binding.rvContents)
    }
}