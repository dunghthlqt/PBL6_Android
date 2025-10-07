package com.demo.pbl6_android.ui.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.demo.pbl6_android.data.model.Conversation
import com.demo.pbl6_android.databinding.ItemConversationBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class ConversationAdapter(
    private val onConversationClick: (Conversation) -> Unit
) : ListAdapter<Conversation, ConversationAdapter.ConversationViewHolder>(ConversationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val binding = ItemConversationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ConversationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ConversationViewHolder(
        private val binding: ItemConversationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(conversation: Conversation) {
            binding.apply {
                // Set shop name
                tvShopName.text = conversation.shopName
                
                // Set last message with "Bạn: " prefix if sent by user
                tvLastMessage.text = if (conversation.isSentByUser) {
                    "Bạn: ${conversation.lastMessage}"
                } else {
                    conversation.lastMessage
                }
                
                // Set time
                tvTime.text = formatTime(conversation.lastMessageTime)
                
                // Set online indicator
                onlineIndicator.visibility = if (conversation.isOnline) View.VISIBLE else View.GONE
                
                // Set unread badge
                if (conversation.unreadCount > 0) {
                    badgeUnread.visibility = View.VISIBLE
                    tvUnreadCount.text = conversation.unreadCount.toString()
                } else {
                    badgeUnread.visibility = View.GONE
                }
                
                // TODO: Load shop avatar using image loading library (Glide/Coil)
                // Glide.with(ivShopAvatar.context).load(conversation.shopAvatar).into(ivShopAvatar)
                
                // Set click listener
                root.setOnClickListener {
                    onConversationClick(conversation)
                }
            }
        }

        private fun formatTime(timestamp: Long): String {
            val now = System.currentTimeMillis()
            val diff = now - timestamp
            
            return when {
                diff < TimeUnit.MINUTES.toMillis(1) -> "Vừa xong"
                diff < TimeUnit.HOURS.toMillis(1) -> {
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
                    "$minutes phút trước"
                }
                diff < TimeUnit.DAYS.toMillis(1) -> {
                    val sdf = SimpleDateFormat("HH:mm", Locale("vi", "VN"))
                    sdf.format(Date(timestamp))
                }
                diff < TimeUnit.DAYS.toMillis(2) -> "Hôm qua"
                diff < TimeUnit.DAYS.toMillis(7) -> {
                    val days = TimeUnit.MILLISECONDS.toDays(diff)
                    "$days ngày trước"
                }
                else -> {
                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("vi", "VN"))
                    sdf.format(Date(timestamp))
                }
            }
        }
    }

    class ConversationDiffCallback : DiffUtil.ItemCallback<Conversation>() {
        override fun areItemsTheSame(oldItem: Conversation, newItem: Conversation): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Conversation, newItem: Conversation): Boolean {
            return oldItem == newItem
        }
    }
}

