package org.sopt.havit.ui.contents

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.data.ContentsData
import org.sopt.havit.databinding.ItemContentsLinearMinBinding

class ContentsAdapter : RecyclerView.Adapter<ContentsAdapter.ContentsViewHolder>() {
    val contentsList = mutableListOf<ContentsData>()

    class ContentsViewHolder(private val binding: ItemContentsLinearMinBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ContentsData) {
            binding.content = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentsViewHolder {
        val binding = ItemContentsLinearMinBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ContentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContentsViewHolder, position: Int) {
        holder.onBind(contentsList[position])
    }

    override fun getItemCount(): Int = contentsList.size
}