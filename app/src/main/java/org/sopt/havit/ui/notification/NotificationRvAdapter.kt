package org.sopt.havit.ui.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.data.remote.NotificationResponse
import org.sopt.havit.databinding.ItemNotificationBinding

class NotificationRvAdapter :
    RecyclerView.Adapter<NotificationRvAdapter.NotificationViewHolder>() {

    val contentsList = mutableListOf<NotificationResponse.NotificationData>()

    class NotificationViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: NotificationResponse.NotificationData, position: Int) {
            binding.content = data
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: NotificationViewHolder,
        position: Int
    ) {
        holder.onBind(contentsList[position], position)
    }

    override fun getItemCount(): Int = contentsList.size
}