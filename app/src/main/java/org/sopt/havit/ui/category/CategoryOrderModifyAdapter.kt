package org.sopt.havit.ui.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.sopt.havit.R
import org.sopt.havit.data.CategoryData
import org.sopt.havit.databinding.ItemCategoryModifyBinding
import java.util.*

class CategoryOrderModifyAdapter :
    RecyclerView.Adapter<CategoryOrderModifyAdapter.CategoryOrderModifyViewHolder>() {
    val categoryList = mutableListOf<CategoryData>()

    fun removeData(position: Int) {
        categoryList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun swapData(fromPosition: Int, toPosition: Int) {
        Collections.swap(categoryList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryOrderModifyViewHolder {
        val binding = ItemCategoryModifyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return CategoryOrderModifyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryOrderModifyViewHolder, position: Int) {
        holder.onBind(categoryList[position])
        holder.onClick()
    }

    override fun getItemCount(): Int = categoryList.size

    class CategoryOrderModifyViewHolder(private val binding: ItemCategoryModifyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: CategoryData) {
            binding.category = data
            Glide.with(binding.ivCategoryIc.context)
                .load(data.icon)
                .into(binding.ivCategoryIc)
        }

        fun onClick() {
            binding.clCategoryList.setOnClickListener {
                Navigation.findNavController(binding.clCategoryList)
                    .navigate(R.id.action_categoryOrderModifyFragment_to_categoryContentModifyFragment)
            }
        }
    }
}