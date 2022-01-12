package org.sopt.havit.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.sopt.havit.data.CategoryData
import org.sopt.havit.databinding.ItemCategoryBinding

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    val categoryList = mutableListOf<CategoryData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.onBind(categoryList[position])
    }

    override fun getItemCount(): Int = categoryList.size

    class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: CategoryData) {
            binding.category = data
            Glide.with(binding.ivCategoryIc.context)
                .load(data.icon)
                .into(binding.ivCategoryIc)
        }
    }
}