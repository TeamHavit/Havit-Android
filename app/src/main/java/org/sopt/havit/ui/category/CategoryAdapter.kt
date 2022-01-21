package org.sopt.havit.ui.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.data.remote.CategoryResponse
import org.sopt.havit.databinding.ItemCategoryBinding

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    val categoryList = mutableListOf<CategoryResponse.AllCategoryData>()
    private lateinit var itemClickListener: OnItemClickListener
    var POSITION: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.onBind(categoryList[position])

        // (1) 리스트 내 항목 클릭 시 onClick() 호출
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int = categoryList.size

    class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: CategoryResponse.AllCategoryData) {
            binding.category = data
        }
    }

    fun updateList(items: List<CategoryResponse.AllCategoryData>?) {
        items?.let {
            val diffCallback = DiffUtilCallback(this.categoryList, items)
            val diffResult = DiffUtil.calculateDiff(diffCallback)

            this.categoryList.run {
                clear()
                addAll(items)
                diffResult.dispatchUpdatesTo(this@CategoryAdapter)
            }
        }
    }

    inner class DiffUtilCallback(
        private val oldData: List<CategoryResponse.AllCategoryData>,
        private val newData: List<CategoryResponse.AllCategoryData>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldData[oldItemPosition]
            val newItem = newData[newItemPosition]

            return oldItem.id == newItem.id
        }

        override fun getOldListSize(): Int = oldData.size

        override fun getNewListSize(): Int = newData.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldData[oldItemPosition] == newData[newItemPosition]
    }
}