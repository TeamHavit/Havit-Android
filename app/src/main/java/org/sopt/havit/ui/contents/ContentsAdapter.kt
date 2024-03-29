package org.sopt.havit.ui.contents

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.R
import org.sopt.havit.databinding.ItemContentsGridBinding
import org.sopt.havit.databinding.ItemContentsLinearMaxBinding
import org.sopt.havit.databinding.ItemContentsLinearMinBinding
import org.sopt.havit.domain.entity.Contents
import org.sopt.havit.util.ContentsDiffCallback
import org.sopt.havit.util.setOnSingleClickListener
import java.text.SimpleDateFormat
import java.util.*

class ContentsAdapter :
    ListAdapter<Contents, RecyclerView.ViewHolder>(ContentsDiffCallback) {
    private lateinit var itemClickListener: OnItemClickListener
    private lateinit var itemSetClickListener: OnItemSetClickListener
    private lateinit var itemHavitClickListener: OnItemHavitClickListener

    private fun setNotificationOption(data: Contents): String {
        // 현재 시간 형식에 맞게 가져오기
        val now = System.currentTimeMillis()
        val nowDate = Date(now)
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val getTime: String = sdf.format(nowDate)

        return if (data.isNotified && getTime >= data.notificationTime) "after" else if (data.isNotified) "before" else "none"
    }

    inner class LinearMinViewHolder(private val binding: ItemContentsLinearMinBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: Contents) {
            binding.notificationOption = setNotificationOption(data) // 시간 형식 변경
            with(binding) {
                Log.d(TAG, "onBind: $data")
                content = data
                ivHavit.tag = if (data.isSeen) "seen" else "unseen"
                ivHavit.setImageResource(if (data.isSeen) R.drawable.ic_contents_read_2 else R.drawable.ic_contents_unread)
                ivAlarm.visibility = if (data.isNotified) VISIBLE else INVISIBLE
            }
        }
    }

    inner class GridViewHolder(private val binding: ItemContentsGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: Contents) {
            binding.notificationOption = setNotificationOption(data) // 시간 형식 변경
            with(binding) {
                content = data
                ivHavit.tag = if (data.isSeen) "seen" else "unseen"
                ivHavit.setImageResource(if (data.isSeen) R.drawable.ic_contents_read_2 else R.drawable.ic_contents_unread)
                ivAlarm.visibility = if (data.isNotified) VISIBLE else INVISIBLE
            }
        }
    }

    inner class LinearMaxViewHolder(private val binding: ItemContentsLinearMaxBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: Contents) {
            binding.notificationOption = setNotificationOption(data) // 시간 형식 변경
            with(binding) {
                content = data
                ivHavit.tag = if (data.isSeen) "seen" else "unseen"
                ivHavit.setImageResource(if (data.isSeen) R.drawable.ic_contents_read_2 else R.drawable.ic_contents_unread)
                ivAlarm.visibility = if (data.isNotified) VISIBLE else INVISIBLE
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
        when (holder.itemViewType) {
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

        // item 삭제 시 position이 업데이트 되지 않고 초기 position으로 남아있기에 holder.layoutPosition으로 위치를 넘겨준다.
        // 아이템 전체 클릭 시 onWebClick() 호출
        holder.itemView.setOnSingleClickListener {
            itemClickListener.onWebClick(it, holder.layoutPosition)
        }
        // 아이템의 더보기 클릭 시 onSetClick() 호출
        holder.itemView.findViewById<View>(R.id.iv_setting).setOnSingleClickListener {
            itemSetClickListener.onSetClick(it, holder.layoutPosition)
        }
        // 아이템의 해빗 클릭 시 onHavitClick() 호출
        holder.itemView.findViewById<ImageView>(R.id.iv_havit).setOnSingleClickListener {
            itemHavitClickListener.onHavitClick(
                holder.itemView.findViewById(R.id.iv_havit),
                holder.layoutPosition
            )
        }
    }

    // 아이템 전체 클릭 리스너 인터페이스
    interface OnItemClickListener {
        fun onWebClick(v: View, position: Int)
    }

    // 아이템 더보기 클릭 리스너 인터페이스
    interface OnItemSetClickListener {
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
    fun setItemSetClickListner(onItemSetClickListener: OnItemSetClickListener) {
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
