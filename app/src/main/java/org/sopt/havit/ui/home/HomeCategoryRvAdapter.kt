package org.sopt.havit.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.R
import org.sopt.havit.data.HomeCategoryData
import org.sopt.havit.databinding.ItemHomeCategoryListBinding

class HomeCategoryRvAdapter :
    RecyclerView.Adapter<HomeCategoryRvAdapter.HomeCategoryRvViewHolder>() {

    private lateinit var binding: ItemHomeCategoryListBinding
    val categoryList = mutableListOf<HomeCategoryData>()
    private var viewType = 1

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    class HomeCategoryRvViewHolder(private val binding: ItemHomeCategoryListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: HomeCategoryData, position: Int) {
            binding.dataHomeCategory = data

            if (position == isFirst) {
                when (itemViewType) {
                    isFirst -> {
                        binding.clCategoryItem.setBackgroundResource(R.drawable.bg_main_card_category_all_img)
                        binding.ivIcon.visibility = View.INVISIBLE
                    }
                    else -> {
                        binding.clCategoryItem.setBackgroundResource(R.drawable.rectangle_purple_category_radius_6)
                        binding.ivIcon.visibility = View.VISIBLE
                    }

                }
            }
        }
    }

    fun setItemViewType(type: Int) {
        viewType = type
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeCategoryRvAdapter.HomeCategoryRvViewHolder {
        binding = ItemHomeCategoryListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return HomeCategoryRvViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HomeCategoryRvAdapter.HomeCategoryRvViewHolder,
        position: Int
    ) {
        holder.onBind(categoryList[position], position)
    }

    override fun getItemCount(): Int = categoryList.size

    companion object {
        const val isFirst = 0
    }
}