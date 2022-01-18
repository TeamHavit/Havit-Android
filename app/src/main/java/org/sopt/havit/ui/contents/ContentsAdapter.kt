package org.sopt.havit.ui.contents

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.R
import org.sopt.havit.data.remote.ContentsResponse
import org.sopt.havit.databinding.ItemContentsGridBinding
import org.sopt.havit.databinding.ItemContentsLinearMaxBinding
import org.sopt.havit.databinding.ItemContentsLinearMinBinding

class ContentsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var contentsList = mutableListOf<ContentsResponse.ContentsData>()

    class LinearMinViewHolder(private val binding: ItemContentsLinearMinBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ContentsResponse.ContentsData) {
            binding.content = data
        }

        fun onClick() {
            binding.ivHavit.setOnClickListener {
                if (binding.ivHavit.tag == "unseen") {
                    binding.ivHavit.setImageResource(R.drawable.ic_contents_read_2)
                    binding.ivHavit.tag = "seen"
                } else if (binding.ivHavit.tag == "seen") {
                    binding.ivHavit.setImageResource(R.drawable.ic_contents_unread)
                    binding.ivHavit.tag = "unseen"
                }
            }
        }
    }

    class GridViewHolder(private val binding: ItemContentsGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ContentsResponse.ContentsData) {
            binding.content = data
        }

        fun onClick() {
            binding.ivHavit.setOnClickListener {
                if (binding.ivHavit.tag == "unseen") {
                    binding.ivHavit.setImageResource(R.drawable.ic_contents_read_2)
                    binding.ivHavit.tag = "seen"
                } else if (binding.ivHavit.tag == "seen") {
                    binding.ivHavit.setImageResource(R.drawable.ic_contents_unread)
                    binding.ivHavit.tag = "unseen"
                }
            }
        }
    }

    class LinearMaxViewHolder(private val binding: ItemContentsLinearMaxBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ContentsResponse.ContentsData) {
            binding.content = data
        }

        fun onClick() {
            binding.ivHavit.setOnClickListener {
                if (binding.ivHavit.tag == "unseen") {
                    binding.ivHavit.setImageResource(R.drawable.ic_contents_read_2)
                    binding.ivHavit.tag = "seen"
                } else if (binding.ivHavit.tag == "seen") {
                    binding.ivHavit.setImageResource(R.drawable.ic_contents_unread)
                    binding.ivHavit.tag = "unseen"
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
    }

    override fun getItemCount(): Int = contentsList.size

    override fun getItemViewType(position: Int): Int {
        return ContentsActivity.layout
    }
}