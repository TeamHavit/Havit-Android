package org.sopt.havit.ui.contents.more.edit_category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.databinding.ItemCategoryToggleBinding
import org.sopt.havit.domain.entity.CategoryWithSelected

class SelectableCategoryAdapter(private val onCategoryClick: (Int) -> Unit) :
    ListAdapter<CategoryWithSelected, SelectableCategoryAdapter.CategoryViewHolder>(
        CategoryDataComparator()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryToggleBinding.inflate(layoutInflater, parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = currentList[position]
        holder.bind(position, category, onCategoryClick)
    }

    class CategoryViewHolder(private val binding: ItemCategoryToggleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            position: Int,
            category: CategoryWithSelected,
            onCategoryClick: (Int) -> Unit
        ) {
            binding.category = category
            binding.root.setOnClickListener { onCategoryClick(position) }
        }
    }

    private class CategoryDataComparator : DiffUtil.ItemCallback<CategoryWithSelected>() {
        override fun areItemsTheSame(oldItem: CategoryWithSelected, newItem: CategoryWithSelected) =
            true

        override fun areContentsTheSame(
            oldItem: CategoryWithSelected,
            newItem: CategoryWithSelected
        ) = oldItem == newItem
    }
}
