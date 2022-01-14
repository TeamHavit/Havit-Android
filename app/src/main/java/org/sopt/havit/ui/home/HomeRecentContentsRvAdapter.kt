package org.sopt.havit.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.data.HomeContentsData
import org.sopt.havit.databinding.ItemHomeRecentContentsListBinding

class HomeContentsRvAdapter : RecyclerView.Adapter<HomeContentsRvAdapter.HomeContentsViewHolder>() {

    var contentsList = mutableListOf<HomeContentsData>()

    class HomeContentsViewHolder(private val binding: ItemHomeRecentContentsListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: HomeContentsData) {
            binding.dataHomeContents = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeContentsViewHolder {
        val binding = ItemHomeRecentContentsListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return HomeContentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeContentsViewHolder, position: Int) {
        holder.onBind(contentsList[position])
    }

    fun setList(list: List<HomeContentsData>) {
        contentsList = list as MutableList<HomeContentsData>
    }

    override fun getItemCount(): Int = contentsList.size
}