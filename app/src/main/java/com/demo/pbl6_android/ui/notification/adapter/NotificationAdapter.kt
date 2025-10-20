package com.demo.pbl6_android.ui.notification.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.pbl6_android.data.model.NotificationItem
import com.demo.pbl6_android.databinding.ItemNotificationBinding

class NotificationAdapter(
    private val notifications: List<NotificationItem>,
    private val onItemClick: (NotificationItem) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NotificationItem) {
            binding.apply {
                tvTitle.text = item.title
                tvDescription.text = item.description
                ivIcon.setImageResource(item.iconResId)
                
                if (item.badgeCount > 0) {
                    badgeContainer.visibility = View.VISIBLE
                    tvBadge.text = if (item.badgeCount > 99) "99+" else item.badgeCount.toString()
                } else {
                    badgeContainer.visibility = View.GONE
                }
                
                root.setOnClickListener {
                    onItemClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notifications[position])
    }

    override fun getItemCount(): Int = notifications.size
}

