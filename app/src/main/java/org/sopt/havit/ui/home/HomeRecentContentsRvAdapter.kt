package org.sopt.havit.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.data.remote.ContentsSimpleResponse
import org.sopt.havit.databinding.ItemHomeRecentContentsListBinding
import org.sopt.havit.ui.contents.ContentsAdapter

class HomeRecentContentsRvAdapter :
    RecyclerView.Adapter<HomeRecentContentsRvAdapter.HomeContentsViewHolder>() {

    var contentsList = mutableListOf<ContentsSimpleResponse.ContentsSimpleData>()
    private lateinit var itemClickListener: OnItemClickListener

    inner class HomeContentsViewHolder(private val binding: ItemHomeRecentContentsListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ContentsSimpleResponse.ContentsSimpleData) {
            data.createdAt = data.createdAt.substring(0 until 10)
                .replace("-", ". ")
            data.description = data.description.replace(" ", "\u00a0") // tvHeader 단어 자동줄바꿈 막는 코드
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

        holder.itemView.setOnClickListener {
            itemClickListener.onWebClick(it, position)
        }
    }

    interface OnItemClickListener {
        fun onWebClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int = contentsList.size
}