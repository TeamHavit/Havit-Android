package org.sopt.havit.ui.contents_simple

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.data.remote.ContentsSimpleResponse
import org.sopt.havit.databinding.ItemContentsSimpleBinding

class ContentsSimpleRvAdapter :
    RecyclerView.Adapter<ContentsSimpleRvAdapter.ContentsSimpleViewHolder>() {

    var contentsList = mutableListOf<ContentsSimpleResponse.ContentsSimpleData>()
    private lateinit var itemClickListener: OnItemClickListener

    inner class ContentsSimpleViewHolder(private val binding: ItemContentsSimpleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ContentsSimpleResponse.ContentsSimpleData) {
            if (data.notificationTime.isNotEmpty()) {
                data.notificationTime = setTime(data.notificationTime)
            }
            data.createdAt = data.createdAt.substring(0 until 10)
                .replace("-", ". ")
            binding.content = data
        }

        private fun setTime(time: String): String {
            val date = time.substring(0 until 10)
                .replace("-", ". ")
            val hour = time.substring(11 until 13)
            val minute = time.substring(14 until 16)
            return if (hour < "12") {
                "$date 오전 ${hour}:${minute} "
            } else {
                "$date 오후 ${hour}:${minute} "
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
}