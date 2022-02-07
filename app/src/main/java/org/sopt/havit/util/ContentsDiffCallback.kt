package org.sopt.havit.util

import androidx.recyclerview.widget.DiffUtil
import org.sopt.havit.data.remote.ContentsResponse

object ContentsDiffCallback : DiffUtil.ItemCallback<ContentsResponse.ContentsData>() {
    override fun areItemsTheSame(
        oldItem: ContentsResponse.ContentsData,
        newItem: ContentsResponse.ContentsData
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ContentsResponse.ContentsData,
        newItem: ContentsResponse.ContentsData
    ): Boolean {
        return oldItem == newItem
    }
}