package org.sopt.havit.ui.home.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.databinding.ItemCommunityBinding
import org.sopt.havit.domain.entity.CommunityPost

class CommunityPagingDataAdapter :
    PagingDataAdapter<CommunityPost, CommunityPagingDataAdapter.ViewHolder>(diffUtil) {

    class ViewHolder(private val binding: ItemCommunityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CommunityPost) {
            binding.data = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCommunityBinding =
            ItemCommunityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    companion object {

        val diffUtil = object : DiffUtil.ItemCallback<CommunityPost>() {

            override fun areItemsTheSame(
                oldItem: CommunityPost,
                newItem: CommunityPost
            ): Boolean {
                return (oldItem.id == newItem.id)
            }

            override fun areContentsTheSame(
                oldItem: CommunityPost,
                newItem: CommunityPost
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}
