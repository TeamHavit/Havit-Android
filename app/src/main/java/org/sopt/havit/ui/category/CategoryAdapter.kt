package org.sopt.havit.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.databinding.ItemCategoryBinding
import org.sopt.havit.domain.entity.Category
import org.sopt.havit.util.setOnSingleClickListener

class CategoryAdapter(
    private val onItemClick: (category: Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    val categoryList = mutableListOf<Category>()

    fun replaceItems(newCategoryList: List<Category>) {
        val diffCallback = DiffCallback(categoryList, newCategoryList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        categoryList.clear()
        categoryList.addAll(newCategoryList)

        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return CategoryViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        // 서버에서 불러온 orderIndex 값이 항상 update 되지 않기 때문에 adapterPosition으로 변경
        categoryList[position].orderIndex = holder.adapterPosition
        holder.onBind(categoryList[position])
    }

    override fun getItemCount(): Int = categoryList.size

    class CategoryViewHolder(
        private val binding: ItemCategoryBinding,
        private val onItemClick: (category: Category) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var item: Category

        init {
            binding.clCategoryList.setOnSingleClickListener {
                if (::item.isInitialized) {
                    onItemClick.invoke(item)
                }
            }
        }

        fun onBind(data: Category) {
            item = data.also {
                binding.category = it
            }
        }
    }

    inner class DiffCallback(
        private val oldList: List<Category>,

        private val newList: List<Category>

    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}
