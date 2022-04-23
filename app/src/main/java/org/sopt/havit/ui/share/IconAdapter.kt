package org.sopt.havit.ui.share

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.R
import org.sopt.havit.databinding.ItemCategoryIconBinding

class IconAdapter(private val onIconClick: (Int) -> Unit) :
    RecyclerView.Adapter<IconAdapter.IconViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryIconBinding.inflate(layoutInflater, parent, false)
        return IconViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        holder.onBind(position, onIconClick)
    }

    override fun getItemCount(): Int = iconList.size

    class IconViewHolder(private val binding: ItemCategoryIconBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int, onIconClick: (Int) -> Unit) {
            binding.categoryIcon = iconList[position]
            binding.root.setOnClickListener { onIconClick(position) }
            binding.clIcon.setBackgroundResource(
                if (isClickedIcon(position)) R.drawable.oval_gray_stroke_2
                else R.drawable.oval_gray
            )
        }

        private fun isClickedIcon(position: Int): Boolean = clickedPosition == position
    }

    companion object {
        var clickedPosition = 0
        val iconList = mutableListOf(
            R.drawable.ic_category1, R.drawable.ic_category2, R.drawable.ic_category3,
            R.drawable.ic_category4, R.drawable.ic_category5, R.drawable.ic_category6,
            R.drawable.ic_category7, R.drawable.ic_category8, R.drawable.ic_category9,
            R.drawable.ic_category10, R.drawable.ic_category11, R.drawable.ic_category12,
            R.drawable.ic_category13, R.drawable.ic_category14, R.drawable.ic_category15
        )
    }
}
