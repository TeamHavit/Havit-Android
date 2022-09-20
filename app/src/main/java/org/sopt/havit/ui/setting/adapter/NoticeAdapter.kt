package org.sopt.havit.ui.setting.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.databinding.ItemNoticeBinding
import org.sopt.havit.domain.entity.Notice
import org.sopt.havit.util.setOnSingleClickListener

class NoticeAdapter(private val onNoticeClick: (String)/*url*/ -> Unit) :
    ListAdapter<Notice, NoticeAdapter.NoticeViewHolder>(NoticeDataComparator()) {

    class NoticeViewHolder(private val binding: ItemNoticeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(notice: Notice, onNoticeClick: (String) -> Unit) {
            binding.notice = notice
            binding.root.setOnSingleClickListener { onNoticeClick(notice.url) }
        }
    }

    private class NoticeDataComparator : DiffUtil.ItemCallback<Notice>() {
        override fun areItemsTheSame(oldItem: Notice, newItem: Notice) =
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: Notice, newItem: Notice) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemNoticeBinding.inflate(layoutInflater, parent, false)
        return NoticeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        val notice = currentList[position]
        holder.bind(notice, onNoticeClick)
    }
}