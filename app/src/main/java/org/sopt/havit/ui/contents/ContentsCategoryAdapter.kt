package org.sopt.havit.ui.contents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.databinding.ItemDialogContentsTopBinding
import org.sopt.havit.domain.entity.Category

class ContentsCategoryAdapter(private val categoryId: Int) :
    RecyclerView.Adapter<ContentsCategoryAdapter.ContentsCategoryViewHolder>() {
    val contentsCategoryList = mutableListOf<Category>()
    private lateinit var itemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentsCategoryViewHolder {
        val binding = ItemDialogContentsTopBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ContentsCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContentsCategoryViewHolder, position: Int) {
        holder.onBind(contentsCategoryList[position])

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
    fun setItemCategoryClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int = contentsCategoryList.size

    inner class ContentsCategoryViewHolder(private val binding: ItemDialogContentsTopBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: Category) {
            binding.category = data
            binding.currentCategory = data.id == categoryId
        }
    }
}
