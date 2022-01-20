package org.sopt.havit.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.data.remote.CategoryResponse
import org.sopt.havit.databinding.ItemHomeCategoryRecyclerviewBinding
import org.sopt.havit.ui.home.HomeCategoryRvAdapter.Companion.isFirst

class HomeCategoryVpAdapter : RecyclerView.Adapter<HomeCategoryVpAdapter.HomeCategoryViewHolder>() {
    var categoryList = mutableListOf<List<CategoryResponse.AllCategoryData>>()

    class HomeCategoryViewHolder(private val binding: ItemHomeCategoryRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: List<CategoryResponse.AllCategoryData>, position: Int) {
            val rvCategoryAdapter = HomeCategoryRvAdapter()
            binding.rvCategory.adapter = rvCategoryAdapter
            rvCategoryAdapter.categoryList.addAll(
                data
            )
            if (position == isFirst)
                rvCategoryAdapter.setItemViewType(isFirst)
            rvCategoryAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCategoryViewHolder {
        val binding = ItemHomeCategoryRecyclerviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return HomeCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeCategoryViewHolder, position: Int) {
        holder.onBind(categoryList[position], position)
    }

    override fun getItemCount(): Int = categoryList.size
}