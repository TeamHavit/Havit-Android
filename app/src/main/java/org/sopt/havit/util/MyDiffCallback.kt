package org.sopt.havit.util

import androidx.recyclerview.widget.DiffUtil
import org.sopt.havit.data.remote.CategoryResponse

object MyDiffCallback : DiffUtil.ItemCallback<CategoryResponse.AllCategoryData>() {
    override fun areItemsTheSame(
        oldItem: CategoryResponse.AllCategoryData,
        newItem: CategoryResponse.AllCategoryData
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: CategoryResponse.AllCategoryData,
        newItem: CategoryResponse.AllCategoryData
    ): Boolean {
        return oldItem == newItem
    }

}