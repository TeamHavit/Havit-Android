package org.sopt.havit.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.databinding.ItemContentsSearchBinding
import org.sopt.havit.domain.entity.Contents
import java.text.SimpleDateFormat
import java.util.*

class SearchContentsAdapter :
    RecyclerView.Adapter<SearchContentsAdapter.SearchContentsViewHolder>() {

    var searchContents = mutableListOf<Contents>()
    private var isRead = false

    private lateinit var itemClickListener: OnItemClickListener // 콘텐츠 아이템 클릭리스너
    private lateinit var itemSettingClickListener: OnItemSettingClickListener // 더보기 아이템 클릭리스너
    private lateinit var itemHavitClickListener: OnItemHavitClickListener // 해빗하기 클릭 리스너

    private fun setNotificationOption(data: Contents): String {
        // 현재 시간 형식에 맞게 가져오기
        val now = System.currentTimeMillis()
        val nowDate = Date(now)
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val getTime: String = sdf.format(nowDate)

        return if (data.isNotified && getTime >= data.notificationTime) "after" else if (data.isNotified) "before" else "none"
    }

    interface OnItemClickListener {
        fun onClick(v: View, data: Contents)
    }

    fun setItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    interface OnItemSettingClickListener {
        fun onSettingClick(v: View, data: Contents, pos: Int)
    }

    fun setItemSettingClickListener(listener: OnItemSettingClickListener) {
        this.itemSettingClickListener = listener
    }

    interface OnItemHavitClickListener {
        fun onHavitClick(v: View, data: Contents, pos: Int)
    }

    fun setItemHavitClickListener(listener: OnItemHavitClickListener) {
        this.itemHavitClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchContentsViewHolder {
        val binding =
            ItemContentsSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchContentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchContentsViewHolder, position: Int) {
        holder.bind(position, searchContents[position])
    }

    override fun getItemCount(): Int = searchContents.size

    fun setItem(data: List<Contents>) {
        searchContents = data as MutableList<Contents>
        notifyDataSetChanged()
    }

    inner class SearchContentsViewHolder(
        val binding: ItemContentsSearchBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(position: Int, data: Contents) {
            binding.content = data
            binding.notiView = setNotificationOption(data)
            contentsClick(position, data)
            isRead = data.isSeen
        }

        private fun contentsClick(position: Int, data: Contents) {
            binding.ivHavit.setOnClickListener {
                itemHavitClickListener.onHavitClick(it, data, position)
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
