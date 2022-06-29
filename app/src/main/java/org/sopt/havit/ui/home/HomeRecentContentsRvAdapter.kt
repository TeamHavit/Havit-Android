package org.sopt.havit.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.data.remote.ContentsSimpleResponse
import org.sopt.havit.databinding.ItemHomeRecentContentsListBinding

class HomeRecentContentsRvAdapter :
    RecyclerView.Adapter<HomeRecentContentsRvAdapter.HomeContentsViewHolder>() {

    var contentsList = mutableListOf<ContentsSimpleResponse.ContentsSimpleData>()
    private lateinit var itemClickListener: OnItemClickListener

    inner class HomeContentsViewHolder(private val binding: ItemHomeRecentContentsListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ContentsSimpleResponse.ContentsSimpleData) {
            if (data.createdAt.length == 16) {
                changeTimeFormat(data) // 시간 형식 변경
            }
            data.description = data.description.replace(" ", "\u00a0") // tvHeader 단어 자동줄바꿈 막는 코드
            binding.homeRecentData = data
        }

        private fun changeTimeFormat(data: ContentsSimpleResponse.ContentsSimpleData) {
            data.createdAt = data.createdAt.substring(0 until 10)
                .replace("-", ". ")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeContentsViewHolder {
        val binding = ItemHomeRecentContentsListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return HomeContentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeContentsViewHolder, position: Int) {
        holder.onBind(contentsList[position])

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

    // diffUtil을 이용해 변경사항 적용
    fun updateList(items: List<ContentsSimpleResponse.ContentsSimpleData>?) {
        items?.let {
            val diffCallback = DiffUtilCallback(this.contentsList, items)
            val diffResult = DiffUtil.calculateDiff(diffCallback)

            this.contentsList.run {
                clear()
                addAll(items)
                diffResult.dispatchUpdatesTo(this@HomeRecentContentsRvAdapter)
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
            (oldData[oldItemPosition].id == newData[newItemPosition].id) &&
                (oldData[oldItemPosition].title == newData[newItemPosition].title)
    }
}
