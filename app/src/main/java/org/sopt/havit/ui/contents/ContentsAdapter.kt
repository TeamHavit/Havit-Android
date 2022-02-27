package org.sopt.havit.ui.contents

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.R
import org.sopt.havit.data.remote.ContentsResponse
import org.sopt.havit.databinding.ItemContentsGridBinding
import org.sopt.havit.databinding.ItemContentsLinearMaxBinding
import org.sopt.havit.databinding.ItemContentsLinearMinBinding
import org.sopt.havit.util.ContentsDiffCallback

class ContentsAdapter : ListAdapter<ContentsResponse.ContentsData, RecyclerView.ViewHolder>(ContentsDiffCallback) {
    private lateinit var itemClickListener: OnItemClickListener
    private lateinit var itemSetClickListener: OnItemSetClickListener
    private lateinit var itemHavitClickListener: OnItemHavitClickListener

    private fun changeTimeFormat(data: ContentsResponse.ContentsData) {
        // 알림 예정 시각 형식 변경
        if (data.notificationTime.isNotEmpty() && data.notificationTime.length == 16) {
            val time = data.notificationTime
            val date = time.substring(0 until 10)
                .replace("-", ". ")
            val hour = time.substring(11 until 13)
            val minute = time.substring(14 until 16)
            if (hour < "12") {
                data.notificationTime = "$date 오전 ${hour}:${minute} "
            } else {
                data.notificationTime = "$date 오후 ${hour}:${minute} "
            }
        }

        // 글 생성 시각 형식 변경
        if (data.createdAt.length == 16) {
            data.createdAt = data.createdAt.substring(0 until 10)
                .replace("-", ". ")
        }
    }

    inner class LinearMinViewHolder(private val binding: ItemContentsLinearMinBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ContentsResponse.ContentsData) {
            changeTimeFormat(data)      // 시간 형식 변경
            binding.content = data
            if (data.isSeen) {
                Log.d("HavitButtonTest", "1. binding - seen ${data.title}")
                binding.ivHavit.tag = "seen"
                binding.ivHavit.setImageResource(R.drawable.ic_contents_read_2)
            } else {
                Log.d("HavitButtonTest", "1. binding - unseen ${data.title}")
                binding.ivHavit.tag = "unseen"
                binding.ivHavit.setImageResource(R.drawable.ic_contents_unread)
            }
        }
    }

    inner class GridViewHolder(private val binding: ItemContentsGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ContentsResponse.ContentsData) {
            changeTimeFormat(data)      // 시간 형식 변경
            binding.content = data
            if (data.isSeen) {
                Log.d("HavitButtonTest", "2. binding - seen ${data.title}")
                binding.ivHavit.tag = "seen"
                binding.ivHavit.setImageResource(R.drawable.ic_contents_read_2)
            } else {
                Log.d("HavitButtonTest", "2. binding - unseen ${data.title}")
                binding.ivHavit.tag = "unseen"
                binding.ivHavit.setImageResource(R.drawable.ic_contents_unread)
            }
        }
    }

    inner class LinearMaxViewHolder(private val binding: ItemContentsLinearMaxBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ContentsResponse.ContentsData) {
            changeTimeFormat(data)      // 시간 형식 변경
            binding.content = data
            if (data.isSeen) {
                Log.d("HavitButtonTest", "3. binding - seen ${data.title}")
                binding.ivHavit.tag = "seen"
                binding.ivHavit.setImageResource(R.drawable.ic_contents_read_2)
            } else {
                Log.d("HavitButtonTest", "3. binding - unseen ${data.title}")
                binding.ivHavit.tag = "unseen"
                binding.ivHavit.setImageResource(R.drawable.ic_contents_unread)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ContentsActivity.LINEAR_MIN_LAYOUT -> {
                val binding = ItemContentsLinearMinBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
                LinearMinViewHolder(binding)
            }
            ContentsActivity.GRID_LAYOUT -> {
                val binding = ItemContentsGridBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
                GridViewHolder(binding)
            }
            else -> {
                val binding = ItemContentsLinearMaxBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
                LinearMaxViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (ContentsActivity.layout) {
            ContentsActivity.LINEAR_MIN_LAYOUT -> {
                (holder as LinearMinViewHolder).onBind(getItem(position))
            }
            ContentsActivity.GRID_LAYOUT -> {
                (holder as GridViewHolder).onBind(getItem(position))
            }
            ContentsActivity.LINEAR_MAX_LAYOUT -> {
                (holder as LinearMaxViewHolder).onBind(getItem(position))
            }
        }
        // 아이템 전체 클릭 시 onWebClick() 호출
        holder.itemView.setOnClickListener {
            itemClickListener.onWebClick(it, position)
        }
        // 아이템의 더보기 클릭 시 onSetClick() 호출
        holder.itemView.findViewById<View>(R.id.iv_setting).setOnClickListener {
            itemSetClickListener.onSetClick(it, position)
        }
        // 아이템의 해빗 클릭 시 onHavitClick() 호출
        holder.itemView.findViewById<ImageView>(R.id.iv_havit).setOnClickListener {
            itemHavitClickListener.onHavitClick(holder.itemView.findViewById(R.id.iv_havit), position)
        }
    }

    // 아이템 전체 클릭 리스너 인터페이스
    interface OnItemClickListener {
        fun onWebClick(v: View, position: Int)
    }
    // 아이템 더보기 클릭 리스너 인터페이스
    interface OnItemSetClickListener{
        fun onSetClick(v: View, position: Int)
    }
    // 아이템 해빗 클릭 리스너 인터페이스
    interface OnItemHavitClickListener {
        fun onHavitClick(v: ImageView, position: Int)
    }

    // 외부에서 전체 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // 외부에서 더보기 클릭 시 이벤트 설정
    fun setItemSetClickListner(onItemSetClickListener: OnItemSetClickListener){
        this.itemSetClickListener = onItemSetClickListener
    }
    // 외부에서 해빗 클릭 시 이벤트 설정
    fun setHavitClickListener(onItemHavitClickListener: OnItemHavitClickListener) {
        this.itemHavitClickListener = onItemHavitClickListener
    }

    override fun getItemViewType(position: Int): Int {
        return ContentsActivity.layout
    }
}