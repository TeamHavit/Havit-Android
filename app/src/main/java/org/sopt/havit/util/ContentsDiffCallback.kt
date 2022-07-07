package org.sopt.havit.util

import androidx.recyclerview.widget.DiffUtil
import org.sopt.havit.domain.entity.Contents

object ContentsDiffCallback : DiffUtil.ItemCallback<Contents>() {
    override fun areItemsTheSame(
        oldItem: Contents,
        newItem: Contents
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Contents,
        newItem: Contents
    ): Boolean {
        return oldItem == newItem
    }
}
