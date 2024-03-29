package org.sopt.havit.ui.contents_simple

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.R
import org.sopt.havit.data.remote.ContentsSimpleResponse
import org.sopt.havit.databinding.ItemContentsSimpleBinding
import org.sopt.havit.util.setOnSinglePostClickListener
import java.text.SimpleDateFormat
import java.util.*

class ContentsSimpleRvAdapter :
    RecyclerView.Adapter<ContentsSimpleRvAdapter.ContentsSimpleViewHolder>() {

    var contentsList = mutableListOf<ContentsSimpleResponse.ContentsSimpleData>()
    private lateinit var itemClickListener: OnItemClickListener
    private lateinit var itemMoreClickListener: OnItemMoreClickListener
    private lateinit var itemHavitClickListener: OnItemHavitClickListener

    inner class ContentsSimpleViewHolder(private val binding: ItemContentsSimpleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ContentsSimpleResponse.ContentsSimpleData) {
            binding.notificationOption = setNotificationOption(data)
            binding.content = data
            setIvHavit(data.isSeen) // 해빗 버튼 초기 설정
        }

        private fun setNotificationOption(data: ContentsSimpleResponse.ContentsSimpleData): String {
            // 현재 시간 형식에 맞게 가져오기
            val now = System.currentTimeMillis()
            val nowDate = Date(now)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val getTime: String = sdf.format(nowDate)

            return if (getTime >= data.notificationTime) "after"
            else "before"
        }

        // havit 버튼 설정값 초기화
        private fun setIvHavit(isSeen: Boolean) {
            binding.ivHavit.tag = if (isSeen) "seen" else "unseen"
            binding.ivHavit.setImageResource(if (isSeen) R.drawable.ic_contents_read_2 else R.drawable.ic_contents_unread)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContentsSimpleViewHolder {
        val binding = ItemContentsSimpleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return ContentsSimpleViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ContentsSimpleViewHolder,
        position: Int
    ) {
        holder.onBind(contentsList[holder.layoutPosition])

        // 아이템 전체 클릭 시 onWebClick() 호출
        holder.itemView.setOnClickListener {
            itemClickListener.onWebClick(it, holder.layoutPosition)
        }
        // 아이템 더보기 클릭 시 onMoreClick() 호출
        holder.itemView.findViewById<View>(R.id.iv_setting).setOnClickListener {
            itemMoreClickListener.onMoreClick(it, holder.layoutPosition)
        }
        // 아이템 해빗 클릭 시 onHavitClick() 호출
        holder.itemView.findViewById<View>(R.id.iv_havit).setOnSinglePostClickListener {
            itemHavitClickListener.onHavitClick(
                holder.itemView.findViewById(R.id.iv_havit),
                holder.layoutPosition
            )
        }
    }

    // 아이템 전체 클릭 리스터 인터페이스
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

    fun updateList(items: List<ContentsSimpleResponse.ContentsSimpleData>?) {
        items?.let {
            val diffCallback = DiffUtilCallback(this.contentsList, items)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            this.contentsList.run {
                clear()
                addAll(items)
                diffResult.dispatchUpdatesTo(this@ContentsSimpleRvAdapter)
            }
        }
    }

    inner class DiffUtilCallback(
        private val oldData: List<ContentsSimpleResponse.ContentsSimpleData>,
        private val newData: List<ContentsSimpleResponse.ContentsSimpleData>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldData[oldItemPosition]
            val newItem = newData[newItemPosition]

            return oldItem.id == newItem.id
        }

        override fun getOldListSize(): Int = oldData.size

        override fun getNewListSize(): Int = newData.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            (
                    (oldData[oldItemPosition].id == newData[newItemPosition].id) &&
                            (oldData[oldItemPosition].isSeen == newData[newItemPosition].isSeen) &&
                            (oldData[oldItemPosition].title == newData[newItemPosition].title) &&
                            (oldData[oldItemPosition].notificationTime == newData[newItemPosition].notificationTime)
                    )
    }
}
