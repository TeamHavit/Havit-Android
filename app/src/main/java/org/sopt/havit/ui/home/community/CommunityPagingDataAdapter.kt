package org.sopt.havit.ui.home.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.databinding.ItemCommunityBinding
import org.sopt.havit.domain.entity.CommunityPost
import org.sopt.havit.util.setOnSingleClickListener

class CommunityPagingDataAdapter(
    private val onSettingClick: (id: Int) -> Unit
) : PagingDataAdapter<CommunityPost, CommunityPagingDataAdapter.ViewHolder>(diffUtil) {

    class ViewHolder(
        private val binding: ItemCommunityBinding,
        private val onSettingClick: (id: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var item: CommunityPost

        init {
            binding.ivSetting.setOnSingleClickListener {
                if (::item.isInitialized) {
                    onSettingClick.invoke(item.id)
                }
            }
        }

        fun bind(data: CommunityPost) {
            item = data.also {
                binding.data = it
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCommunityBinding =
            ItemCommunityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onSettingClick)
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
