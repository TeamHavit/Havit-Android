package org.sopt.havit.ui.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.sopt.havit.R
import org.sopt.havit.data.CategoryData
import org.sopt.havit.data.remote.CategoryResponse
import org.sopt.havit.databinding.ItemCategoryBinding

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    val categoryList = mutableListOf<CategoryResponse.AllCategoryData>()
    private lateinit var itemClickListener: OnItemClickListener

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
            data.url = "https://user-images.githubusercontent.com/68214704/149118495-e9cc9770-785d-4644-9956-9e17a6641180.png"
            binding.category = data
//            Glide.with(binding.ivCategoryIc.context)
//                .load(data.url)
//                .placeholder(R.drawable.ic_category_sample)
//                .into(binding.ivCategoryIc)
        }
    }
}