package org.sopt.havit.ui.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.R
import org.sopt.havit.data.remote.NotificationResponse
import org.sopt.havit.databinding.ItemNotificationBinding

class NotificationRvAdapter :
    RecyclerView.Adapter<NotificationRvAdapter.NotificationViewHolder>() {

    val contentsList = mutableListOf<NotificationResponse.NotificationData>()
    private lateinit var itemClickListener: OnItemClickListener
    private lateinit var itemMoreClickListener: OnItemMoreClickListener
    private lateinit var itemHavitClickListener: OnItemHavitClickListener

    class NotificationViewHolder(val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: NotificationResponse.NotificationData, position: Int) {
            binding.content = data
            binding.option = NotificationActivity.option
            changeTimeFormat(data)
            setIvHavit(data.isSeen)
        }

        private fun setIvHavit(isSeen: Boolean) {
            binding.ivHavit.setImageResource(if (isSeen) R.drawable.ic_contents_read_2 else R.drawable.ic_contents_unread)
        }

        private fun changeTimeFormat(data: NotificationResponse.NotificationData) {
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
            if (data.createdAt.length == 10) {
                data.createdAt = data.createdAt.substring(0 until 10)
                    .replace("-", ". ")
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: NotificationViewHolder,
        position: Int
    ) {
        val safePosition = holder.layoutPosition
        holder.onBind(contentsList[safePosition], safePosition)

        // 아이템 전체 클릭 시 onWebClick() 호출
        holder.itemView.setOnClickListener {
            itemClickListener.onWebClick(it, safePosition)
        }
        // 아이템 더보기 클릭 시 onMoreClick() 호출
        holder.binding.ivMore.setOnClickListener {
            itemMoreClickListener.onMoreClick(it, safePosition)
        }
        // 아이템 해빗 클릭 시 onHavitClick() 호출
        holder.binding.ivHavit.setOnClickListener {
            itemHavitClickListener.onHavitClick(
                holder.binding.ivHavit,
                safePosition
            )
        }
    }

    //아이템 전체 클릭 리스터 인터페이스
    interface OnItemClickListener {
        fun onWebClick(v: View, position: Int)
    }

    // 아이템 더보기 클릭 리스너 인터페이스
    interface OnItemMoreClickListener {
        fun onMoreClick(v: View, position: Int)
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
    fun setItemMoreClickListner(onItemMoreClickListener: OnItemMoreClickListener) {
        this.itemMoreClickListener = onItemMoreClickListener
    }

    // 외부에서 해빗 클릭 시 이벤트 설정
    fun setHavitClickListener(onItemHavitClickListener: OnItemHavitClickListener) {
        this.itemHavitClickListener = onItemHavitClickListener
    }

    override fun getItemCount(): Int = contentsList.size

    fun updateList(items: List<NotificationResponse.NotificationData>?) {
        items?.let {
            val diffCallback = DiffUtilCallback(this.contentsList, items)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            this.contentsList.run {
                clear()
                addAll(items)
                diffResult.dispatchUpdatesTo(this@NotificationRvAdapter)
            }
        }
    }

    inner class DiffUtilCallback(
        private val oldData: List<NotificationResponse.NotificationData>,
        private val newData: List<NotificationResponse.NotificationData>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldData[oldItemPosition]
            val newItem = newData[newItemPosition]

            return oldItem.id == newItem.id
        }

        override fun getOldListSize(): Int = oldData.size

        override fun getNewListSize(): Int = newData.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldData[oldItemPosition].id == newData[newItemPosition].id &&
                    oldData[oldItemPosition].isSeen == newData[newItemPosition].isSeen
    }
}