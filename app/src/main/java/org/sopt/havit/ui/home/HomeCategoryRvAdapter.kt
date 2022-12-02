package org.sopt.havit.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.R
import org.sopt.havit.databinding.ItemHomeCategoryListBinding
import org.sopt.havit.domain.entity.Category
import org.sopt.havit.ui.category.CategoryFragment
import org.sopt.havit.ui.category.CategoryFragment.Companion.CATEGORY_ID
import org.sopt.havit.ui.category.CategoryFragment.Companion.CATEGORY_NAME
import org.sopt.havit.ui.contents.ContentsActivity

class HomeCategoryRvAdapter(page: Int) :
    RecyclerView.Adapter<HomeCategoryRvAdapter.HomeCategoryRvViewHolder>() {

    private lateinit var binding: ItemHomeCategoryListBinding
    val categoryList = mutableListOf<Category>()
    private var viewType = 1
    private val pagePosition = page

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    inner class HomeCategoryRvViewHolder(private val binding: ItemHomeCategoryListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: Category, position: Int) {
            binding.homeCategoryData = data
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
            binding.root.setOnClickListener {
                val intent = Intent(it.context, ContentsActivity::class.java)
                intent.putExtra(CATEGORY_ID, data.id)
                intent.putExtra(CATEGORY_NAME, data.title)
                intent.putExtra(CategoryFragment.CATEGORY_POSITION, pagePosition * 6 + position - 1)
                intent.putExtra(CategoryFragment.CATEGORY_IMAGE_ID, data.imageId)
                startActivity(it.context, intent, null)
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
    }

    override fun getItemCount(): Int = categoryList.size

    companion object {
        const val isFirst = 0
    }
}
