package org.sopt.havit.ui.contents_simple

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.R
import org.sopt.havit.data.remote.ContentsSimpleResponse
import org.sopt.havit.databinding.ItemContentsSimpleBinding

class ContentsSimpleRvAdapter(contentsViewModel: ContentsSimpleViewModel) :
    RecyclerView.Adapter<ContentsSimpleRvAdapter.ContentsSimpleViewHolder>() {

    var contentsList = mutableListOf<ContentsSimpleResponse.ContentsSimpleData>()
    private lateinit var itemClickListener: OnItemClickListener
    private var viewModel = contentsViewModel

    inner class ContentsSimpleViewHolder(private val binding: ItemContentsSimpleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ContentsSimpleResponse.ContentsSimpleData) {
            if (data.notificationTime.isNotEmpty()) {
                data.notificationTime = setTime(data.notificationTime)
            }
            data.createdAt = data.createdAt.substring(0 until 10)
                .replace("-", ". ")
            setIvHavit(data.isSeen)
            binding.content = data
        }

        fun setIvHavit(isSeen: Boolean) {
            if (isSeen) {
                binding.ivHavit.tag = "seen"
                binding.ivHavit.setImageResource(R.drawable.ic_contents_read_2)
            } else {
                binding.ivHavit.tag = "unseen"
                binding.ivHavit.setImageResource(R.drawable.ic_contents_unread)
            }
        }

        fun onHavitClick(data: ContentsSimpleResponse.ContentsSimpleData) {
            binding.ivHavit.setOnClickListener {
                viewModel.setIsSeen(data.id)
                setIvHavit(binding.ivHavit.tag == "unseen")
            }
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
        holder.onHavitClick(contentsList[position])

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