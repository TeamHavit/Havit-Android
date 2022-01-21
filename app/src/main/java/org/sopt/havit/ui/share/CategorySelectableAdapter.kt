package org.sopt.havit.ui.share

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.R
import org.sopt.havit.data.remote.CategoryResponse
import org.sopt.havit.databinding.ItemCategorySelectableBinding

class CategorySelectableAdapter :
    RecyclerView.Adapter<CategorySelectableAdapter.CategorySelectableViewHolder>() {
    val categorySelectableList = mutableListOf<CategoryResponse.AllCategoryData>()

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): CategorySelectableViewHolder {
        val binding: ItemCategorySelectableBinding =
            DataBindingUtil.inflate( LayoutInflater.from(parent.context),
                R.layout.item_category_selectable,
                parent,
                false )

        return CategorySelectableViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategorySelectableViewHolder, position: Int) {
        holder.onBind(categorySelectableList[position])
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
            Log.d("CategoryAdapter", "$position clicked in Adapter")
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

    // setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener: OnItemClickListener

    override fun getItemCount(): Int = categorySelectableList.size

    class CategorySelectableViewHolder(private val binding: ItemCategorySelectableBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: CategoryResponse.AllCategoryData) {
            binding.selectableCategory = data
        }
    }
}
