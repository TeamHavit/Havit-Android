package org.sopt.havit.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.databinding.ItemCategoryBinding
import org.sopt.havit.domain.entity.Category
import org.sopt.havit.util.setOnSingleClickListener

class CategoryAdapter(
    private val onItemClick: (category: Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    // id 기준 이전의 것과 같다면 onBindViewHolder 호출 제외 -> 깜빡임 사라짐
    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong() // or data id
    }

    val categoryList = mutableListOf<Category>()

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
}
