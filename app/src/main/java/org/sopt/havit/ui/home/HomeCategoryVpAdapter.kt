package org.sopt.havit.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.data.HomeCategoryListData
import org.sopt.havit.databinding.ItemHomeCategoryRecyclerviewBinding

class HomeCategoryVpAdapter : RecyclerView.Adapter<HomeCategoryVpAdapter.HomeCategoryViewHolder>() {
    val categoryList = mutableListOf<HomeCategoryListData>()

    class HomeCategoryViewHolder(private val binding: ItemHomeCategoryRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: HomeCategoryListData, position: Int) {
            val rvCategoryAdapter = HomeCategoryRvAdapter()
            binding.rvCategory.adapter = rvCategoryAdapter
            rvCategoryAdapter.categoryList.addAll(
                data.categoryListData
            )
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