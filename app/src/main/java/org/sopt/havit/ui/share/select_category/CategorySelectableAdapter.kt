package org.sopt.havit.ui.share.select_category

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
    lateinit var clickedCategory: Array<Boolean>
//    var clickedCategory = Array(categorySelectableList.size) { _ -> false }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategorySelectableViewHolder {
        val binding: ItemCategorySelectableBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_category_selectable,
                parent,
                false
            )

        return CategorySelectableViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategorySelectableViewHolder, position: Int) {

        holder.onBind(categorySelectableList[position], position)
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
            Log.d("CategoryAdapter_", "$position clicked in Adapter")
            clickedCategory[position] = !clickedCategory[position] // 0부터 시작
            notifyItemChanged(position)

            clickedCategory.forEach {
                Log.d("CategoryAdapter_for", it.toString())
            }
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

    inner class CategorySelectableViewHolder(private val binding: ItemCategorySelectableBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: CategoryResponse.AllCategoryData, position: Int) {

            binding.selectableCategory = data

            if (clickedCategory[position]) {
                binding.ivCheck.visibility = View.VISIBLE
                binding.clCategoryList.setBackgroundResource(R.drawable.rectangle_purple_2_radius_6)
            } else {
                binding.ivCheck.visibility = View.GONE
                binding.clCategoryList.setBackgroundResource(R.drawable.rectangle_purple_category_radius_6)
            }
        }
    }
}
