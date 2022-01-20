package org.sopt.havit.ui.share

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.R
import org.sopt.havit.data.CategorySelectableData
import org.sopt.havit.data.remote.CategoryResponse
import org.sopt.havit.databinding.ItemCategorySelectableBinding

class CategorySelectableAdapter :
    RecyclerView.Adapter<CategorySelectableAdapter.CategorySelectableViewHolder>() {
    val categorySelectableList = mutableListOf<CategoryResponse.AllCategoryData>()

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): CategorySelectableViewHolder {
        val binding: ItemCategorySelectableBinding =
            DataBindingUtil.inflate( LayoutInflater.from(parent.context),
                R.layout.item_category_selectable,
                parent,
                false )

        return CategorySelectableViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategorySelectableViewHolder, position: Int) {
        holder.onBind(categorySelectableList[position])
    }

    override fun getItemCount(): Int = categorySelectableList.size

    class CategorySelectableViewHolder(private val binding: ItemCategorySelectableBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: CategoryResponse.AllCategoryData) {
            binding.selectableCategory = data
        }
    }
}
