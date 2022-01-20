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
    private var clickedPosition : Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        val binding: ItemCategoryIconBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_category_icon,
                parent,
                false
            )

        return IconViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        holder.onBind(iconList[position])
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)

            Log.d("IconAdapter", "$position clicked in Adapter")

            // TODO : Check Position variable
            val tempPosition = position
            if (clickedPosition == position)
                return@setOnClickListener
            else clickedPosition = tempPosition

            for (i in 0..15) {
                Log.d("Update", "$i")
                if (i == position) continue
                notifyItemChanged(i)
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

    override fun getItemCount(): Int = iconList.size

    class IconViewHolder(private val binding: ItemCategoryIconBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: Int) {
            binding.categoryIcon = data
            binding.clIcon.setBackgroundResource(R.drawable.oval_gray)
        }


    }
}