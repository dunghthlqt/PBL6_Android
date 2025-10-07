package com.demo.pbl6_android.data.model

data class Conversation(
    val id: String,
    val shopId: String,
    val shopName: String,
    val shopAvatar: String,
    val lastMessage: String,
    val lastMessageTime: Long,
    val unreadCount: Int,
    val isOnline: Boolean,
    val isSentByUser: Boolean = false
)

