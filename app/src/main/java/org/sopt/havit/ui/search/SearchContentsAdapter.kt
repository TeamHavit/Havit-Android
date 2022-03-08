package org.sopt.havit.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.sopt.havit.R
import org.sopt.havit.data.remote.ContentsSearchResponse
import org.sopt.havit.databinding.ItemContentsSearchBinding

class SearchContentsAdapter :
    RecyclerView.Adapter<SearchContentsAdapter.SearchContentsViewHolder>() {

    private var searchContents = mutableListOf<ContentsSearchResponse.Data>()
    private var isRead = false

    private lateinit var itemClickListener: OnItemClickListener // 콘텐츠 아이템 클릭리스너
    private lateinit var itemSettingClickListener: OnItemSettingClickListener // 더보기 아이템 클릭리스너
    private lateinit var itemHavitClickListener: OnItemHavitClickListener // 해빗하기 클릭 리스너

    interface OnItemClickListener {
        fun onClick(v: View, data: ContentsSearchResponse.Data)
    }

    fun setItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    interface OnItemSettingClickListener {
        fun onSettingClick(v: View, data: ContentsSearchResponse.Data, pos: Int)
    }

    fun setItemSettingClickListener(listener: OnItemSettingClickListener) {
        this.itemSettingClickListener = listener
    }

    interface OnItemHavitClickListener {
        fun onHavitClick(v: View, data: ContentsSearchResponse.Data, isSeen: String)
    }

    fun setItemHavitClickListener(listener: OnItemHavitClickListener) {
        this.itemHavitClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchContentsViewHolder {
        val binding = ItemContentsSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchContentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchContentsViewHolder, position: Int) {
        holder.bind(position, searchContents[position])
    }

    override fun getItemCount(): Int = searchContents.size

    fun setItem(data: List<ContentsSearchResponse.Data>) {
        searchContents = data as MutableList<ContentsSearchResponse.Data>
        notifyDataSetChanged()
    }

    inner class SearchContentsViewHolder(
        val binding: ItemContentsSearchBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(position: Int, data: ContentsSearchResponse.Data) {
            binding.content = data
            contentsClick(position, data)
            isRead = data.isSeen
        }

        private fun contentsClick(position: Int, data: ContentsSearchResponse.Data) {
            binding.ivHavit.setOnClickListener {
                if (it.tag == "seen") {
                    itemHavitClickListener.onHavitClick(it, data, it.tag as String)
                    Glide.with(binding.ivHavit.context)
                        .load(R.drawable.ic_contents_unread)
                        .into(binding.ivHavit)
                    it.tag = "unseen"
                    isRead = false
                } else {
                    itemHavitClickListener.onHavitClick(it, data, it.tag as String)
                    Glide.with(binding.ivHavit.context)
                        .load(R.drawable.ic_contents_read_2)
                        .into(binding.ivHavit)
                    it.tag = "seen"
                    isRead = true
                }
            }
            binding.clSearchItem.setOnClickListener {
                itemClickListener.onClick(it, data)
            }
            binding.ivSetting.setOnClickListener {
                itemSettingClickListener.onSettingClick(it, data, position)
            }
        }
    }
}