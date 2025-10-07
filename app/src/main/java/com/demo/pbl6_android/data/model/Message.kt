package com.demo.pbl6_android.data.model

data class Message(
    val id: String,
    val conversationId: String,
    val content: String,
    val timestamp: Long,
    val isFromUser: Boolean,
    val isRead: Boolean = false,
    val messageType: MessageType = MessageType.TEXT
)

enum class MessageType {
    TEXT,
    IMAGE,
    PRODUCT
}

