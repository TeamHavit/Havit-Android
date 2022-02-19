package org.sopt.havit.ui.share

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.R
import org.sopt.havit.databinding.ItemCategoryIconBinding

class IconAdapter : RecyclerView.Adapter<IconAdapter.IconViewHolder>() {
    val iconList = mutableListOf<Int>()
    var clickedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        val binding: ItemCategoryIconBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_category_icon, parent, false
            )
        return IconViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        holder.onBind(iconList[position], position)
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
            clickedPosition = position
            for (i in 0..iconList.size) notifyItemChanged(i)
        }
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    // 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    // setItemClickListener 로 설정한 함수 실행
    private lateinit var itemClickListener: OnItemClickListener

    inner class IconViewHolder(private val binding: ItemCategoryIconBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: Int, position: Int) {
            binding.categoryIcon = data
            binding.clIcon.setBackgroundResource(
                if (isDefaultIcon(position) || isClickedIcon(position)) R.drawable.oval_gray_stroke_2
                else R.drawable.oval_gray
            )
        }
    }

    private fun isDefaultIcon(position: Int): Boolean = (clickedPosition == -1) && (position == 0)

    private fun isClickedIcon(position: Int): Boolean = clickedPosition == position

    override fun getItemCount(): Int = iconList.size
}