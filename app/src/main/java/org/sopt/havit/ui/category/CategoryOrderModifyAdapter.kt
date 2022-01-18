package org.sopt.havit.ui.category

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
        holder.onClick(position)
    }

    override fun getItemCount(): Int = categoryList.size

    class CategoryOrderModifyViewHolder(private val binding: ItemCategoryModifyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: CategoryData) {
            binding.category = data
            Glide.with(binding.ivCategoryIc.context)
                .load(data.categoryImage)
                .into(binding.ivCategoryIc)
        }

        fun onClick(position: Int) {
            binding.clCategoryList.setOnClickListener {
                val intent = Intent(it.context, CategoryContentModifyActivity::class.java)
                intent.putExtra("categoryPos", position)
                it.context.startActivity(intent)
            }
        }
    }
}