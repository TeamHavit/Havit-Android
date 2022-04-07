package org.sopt.havit.ui.contents_simple

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.R
import org.sopt.havit.data.remote.ContentsSearchResponse
import org.sopt.havit.data.remote.ContentsSimpleResponse
import org.sopt.havit.databinding.ItemContentsSimpleBinding
import org.sopt.havit.ui.contents.ContentsMoreFragment

class ContentsSimpleRvAdapter(
    contentsViewModel: ContentsSimpleViewModel,
    fragmentManager: FragmentManager,
    contentsType: String
) :
    RecyclerView.Adapter<ContentsSimpleRvAdapter.ContentsSimpleViewHolder>() {
    var contentsList = mutableListOf<ContentsSimpleResponse.ContentsSimpleData>()
    private var mFragmentManager: FragmentManager = fragmentManager
    private lateinit var itemClickListener: OnItemClickListener
    private var viewModel = contentsViewModel
    private var contentsType = contentsType

    // setItemClickListener로 설정한 함수 실행
    private lateinit var havitClickListener: OnHavitClickListener

    // 리스너 인터페이스
    interface OnHavitClickListener {
        fun onHavitClick()
    }

    // 외부에서 클릭 시 이벤트 설정
    fun setHavitClickListener(onItemClickListener: OnHavitClickListener) {
        this.havitClickListener = onItemClickListener
    }

    inner class ContentsSimpleViewHolder(private val binding: ItemContentsSimpleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ContentsSimpleResponse.ContentsSimpleData) {
            changeTimeFormat(data)      // 시간 형식 변경
            setIvHavit(data.isSeen)     // 해빗 버튼 초기 설정
            binding.content = data
        }

        private fun changeTimeFormat(data: ContentsSimpleResponse.ContentsSimpleData) {
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

        // havit 버튼 설정값 초기화
        private fun setIvHavit(isSeen: Boolean) {
            if (isSeen) {
                binding.ivHavit.tag = "seen"
                binding.ivHavit.setImageResource(R.drawable.ic_contents_read_2)
            } else {
                binding.ivHavit.tag = "unseen"
                binding.ivHavit.setImageResource(R.drawable.ic_contents_unread)
            }
        }

        fun onClick(
            data: ContentsSimpleResponse.ContentsSimpleData,
            pos: Int,
            contentsType: String
        ) {
            binding.ivHavit.setOnClickListener {
                if (!contentsList[pos].isSeen) {
                    havitClickListener.onHavitClick()
                }
                contentsList[pos].isSeen = !contentsList[pos].isSeen
                viewModel.setIsSeen(data.id)
                setIvHavit(binding.ivHavit.tag == "unseen")
            }

            binding.ivSetting.setOnClickListener {
                val dataMore = ContentsSearchResponse.Data(
                    data.createdAt,
                    data.description,
                    data.id,
                    data.image,
                    data.isNotified,
                    data.isSeen,
                    data.notificationTime,
                    data.title,
                    data.url
                )
                // 더보기 -> 삭제 클릭 시 수행될 삭제 함수
                val removeItem: (Int) -> Unit = {
                    notifyItemRemoved(it)
                    viewModel.requestContentsTaken(contentsType)
                }
                ContentsMoreFragment(dataMore, removeItem, pos).show(mFragmentManager, "setting")
            }
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
        holder.onBind(contentsList[position])
        holder.onClick(contentsList[position], position, contentsType)

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
            oldData[oldItemPosition] == newData[newItemPosition]
    }
}