package org.sopt.havit.ui.contents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.R
import org.sopt.havit.data.remote.ContentsResponse
import org.sopt.havit.databinding.ItemContentsGridBinding
import org.sopt.havit.databinding.ItemContentsLinearMaxBinding
import org.sopt.havit.databinding.ItemContentsLinearMinBinding

class ContentsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var contentsList = mutableListOf<ContentsResponse.ContentsData>()
    private lateinit var itemClickListener: ContentsAdapter.OnItemClickListener

    class LinearMinViewHolder(private val binding: ItemContentsLinearMinBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ContentsResponse.ContentsData) {
            binding.content = data
            if(data.isSeen){
                binding.ivHavit.setImageResource(R.drawable.ic_contents_read_2)
            }
            else{
                binding.ivHavit.setImageResource(R.drawable.ic_contents_unread)
            }
        }

        fun onClick() {
            binding.ivHavit.setOnClickListener {
                if(binding.content?.isSeen == false){
                    binding.ivHavit.setImageResource(R.drawable.ic_contents_read_2)
                }
                else{
                    binding.ivHavit.setImageResource(R.drawable.ic_contents_unread)
                }
            }
        }

//        fun onClick() {
//            binding.ivHavit.setOnClickListener {
//                if (binding.ivHavit.tag == "unseen") {
//                    binding.ivHavit.setImageResource(R.drawable.ic_contents_read_2)
//                    binding.ivHavit.tag = "seen"
//                } else if (binding.ivHavit.tag == "seen") {
//                    binding.ivHavit.setImageResource(R.drawable.ic_contents_unread)
//                    binding.ivHavit.tag = "unseen"
//                }
//            }
//        }
    }

    class GridViewHolder(private val binding: ItemContentsGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ContentsResponse.ContentsData) {
            binding.content = data
            if(data.isSeen){
                binding.ivHavit.setImageResource(R.drawable.ic_contents_read_2)
            }
            else{
                binding.ivHavit.setImageResource(R.drawable.ic_contents_unread)
            }
        }

        fun onClick() {
            binding.ivHavit.setOnClickListener {
                if(binding.content?.isSeen == false){
                    binding.ivHavit.setImageResource(R.drawable.ic_contents_read_2)
                }
                else{
                    binding.ivHavit.setImageResource(R.drawable.ic_contents_unread)
                }
            }
        }
    }

    class LinearMaxViewHolder(private val binding: ItemContentsLinearMaxBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ContentsResponse.ContentsData) {
            binding.content = data
            if(data.isSeen){
                binding.ivHavit.setImageResource(R.drawable.ic_contents_read_2)
            }
            else{
                binding.ivHavit.setImageResource(R.drawable.ic_contents_unread)
            }
        }

        fun onClick() {
            binding.ivHavit.setOnClickListener {
                if(binding.content?.isSeen == false){
                    binding.ivHavit.setImageResource(R.drawable.ic_contents_read_2)
                }
                else{
                    binding.ivHavit.setImageResource(R.drawable.ic_contents_unread)
                }
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
                (holder as LinearMinViewHolder).onBind(contentsList[position])
                holder.onClick()
            }
            ContentsActivity.GRID_LAYOUT -> {
                (holder as GridViewHolder).onBind(contentsList[position])
                holder.onClick()
            }
            ContentsActivity.LINEAR_MAX_LAYOUT -> {
                (holder as LinearMaxViewHolder).onBind(contentsList[position])
                holder.onClick()
            }
        }
        // (1) 리스트 내 항목 클릭 시 onClick() 호출
        holder.itemView.setOnClickListener {
            itemClickListener.onWebClick(it, position)
        }
    }

    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onWebClick(v: View, position: Int)
    }

    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }


    override fun getItemCount(): Int = contentsList.size

    override fun getItemViewType(position: Int): Int {
        return ContentsActivity.layout
    }
}