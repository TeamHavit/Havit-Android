package org.sopt.havit.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.R
import org.sopt.havit.data.remote.CategoryResponse
import org.sopt.havit.databinding.ItemHomeCategoryListBinding
import org.sopt.havit.ui.contents.ContentsActivity

class HomeCategoryRvAdapter(page: Int) :
    RecyclerView.Adapter<HomeCategoryRvAdapter.HomeCategoryRvViewHolder>() {

    private lateinit var binding: ItemHomeCategoryListBinding
    val categoryList = mutableListOf<CategoryResponse.AllCategoryData>()
    private var viewType = 1
    private val pagePosition = page

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    class HomeCategoryRvViewHolder(private val binding: ItemHomeCategoryListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: CategoryResponse.AllCategoryData, position: Int) {
            binding.dataHomeCategory = data
            binding.tvTitle.text = data.title
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
    ): HomeCategoryRvViewHolder {
        binding = ItemHomeCategoryListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return HomeCategoryRvViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HomeCategoryRvViewHolder,
        position: Int
    ) {
        holder.onBind(categoryList[position], position)

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, ContentsActivity::class.java)
            intent.putExtra("categoryId", categoryList[position].id)
            intent.putExtra("categoryName", categoryList[position].title)
            intent.putExtra("position", pagePosition * 6 + position - 1)
            startActivity(holder.itemView.context, intent, null)
        }
    }

    override fun getItemCount(): Int = categoryList.size

    companion object {
        const val isFirst = 0
    }
}