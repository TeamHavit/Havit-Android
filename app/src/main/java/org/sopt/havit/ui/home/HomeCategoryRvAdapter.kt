package org.sopt.havit.ui.home

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.data.HomeCategoryData
import org.sopt.havit.databinding.ItemHomeCategoryListBinding

class HomeCategoryRvAdapter :
    RecyclerView.Adapter<HomeCategoryRvAdapter.HomeCategoryRvViewHolder>() {

    private lateinit var binding: ItemHomeCategoryListBinding
    val categoryList = mutableListOf<HomeCategoryData>()
    private var categoryBackground: Drawable? = null
    private var categoryIcon: Int? = null

    class HomeCategoryRvViewHolder(private val binding: ItemHomeCategoryListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: HomeCategoryData) {
            binding.dataHomeCategory = data
        }
    }

    fun setCallbackChangeItemBackground(background: Drawable?, icon: Int) {
        categoryBackground = background
        categoryIcon = icon
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
        holder.onBind(categoryList[position])

//        if (position == 0) {
//            holder.itemView.background = categoryBackground
//            binding.ivIcon.visibility = categoryIcon!!
//        }
        // else
    }

    override fun getItemCount(): Int = categoryList.size
}