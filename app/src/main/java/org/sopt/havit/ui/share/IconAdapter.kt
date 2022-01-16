package org.sopt.havit.ui.share

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.R
import org.sopt.havit.databinding.ItemCategoryIconBinding

class IconAdapter : RecyclerView.Adapter<IconAdapter.IconViewHolder>() {
    val iconList = mutableListOf<String>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        val binding: ItemCategoryIconBinding =
            DataBindingUtil.inflate( LayoutInflater.from(parent.context),
                R.layout.item_category_icon,
                parent,
                false
            )

        return IconViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) =
        holder.onBind(iconList[position])

    override fun getItemCount(): Int = iconList.size

    class IconViewHolder(private val binding: ItemCategoryIconBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: String) {
            binding.categoryIcon = data
        }

    }
}