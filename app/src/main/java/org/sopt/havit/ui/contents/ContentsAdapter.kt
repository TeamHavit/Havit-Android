package org.sopt.havit.ui.contents

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.data.ContentsData
import org.sopt.havit.databinding.ItemContentsGridBinding
import org.sopt.havit.databinding.ItemContentsLinearMaxBinding
import org.sopt.havit.databinding.ItemContentsLinearMinBinding

class ContentsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var contentsList = mutableListOf<ContentsData>()

    class LinearMinViewHolder(private val binding: ItemContentsLinearMinBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ContentsData) {
            binding.content = data
        }
    }

    class GridViewHolder(private val binding: ItemContentsGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ContentsData) {
            binding.content = data
        }
    }

    class LinearMaxViewHolder(private val binding: ItemContentsLinearMaxBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ContentsData) {
            binding.content = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ContentsFragment.LINEAR_MIN_LAYOUT -> {
                val binding = ItemContentsLinearMinBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
                LinearMinViewHolder(binding)
            }
            ContentsFragment.GRID_LAYOUT -> {
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
        when (ContentsFragment.layout) {
            ContentsFragment.LINEAR_MIN_LAYOUT -> {
                (holder as LinearMinViewHolder).onBind(contentsList[position])
            }
            ContentsFragment.GRID_LAYOUT -> {
                (holder as GridViewHolder).onBind(contentsList[position])
            }
            ContentsFragment.LINEAR_MAX_LAYOUT -> {
                (holder as LinearMaxViewHolder).onBind(contentsList[position])
            }
        }
    }

    override fun getItemCount(): Int = contentsList.size

    override fun getItemViewType(position: Int): Int {
        return ContentsFragment.layout
    }
}