package org.sopt.havit.ui.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.data.remote.CategoryResponse
import org.sopt.havit.databinding.ItemCategoryModifyBinding
import org.sopt.havit.util.setOnSingleClickListener
import java.util.*

class CategoryOrderModifyAdapter :
    RecyclerView.Adapter<CategoryOrderModifyAdapter.CategoryOrderModifyViewHolder>() {
    var categoryList = mutableListOf<CategoryResponse.AllCategoryData>()
    private lateinit var itemClickListener: OnItemClickListener

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
        // 리스트 내 항목 클릭 시 호출
        holder.itemView.setOnSingleClickListener {
            itemClickListener.onClick(it, holder.layoutPosition)
        }
    }

    // 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    // 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int = categoryList.size

    class CategoryOrderModifyViewHolder(private val binding: ItemCategoryModifyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: CategoryResponse.AllCategoryData) {
            binding.category = data
        }
    }
}
