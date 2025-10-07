package com.demo.pbl6_android.data

import com.demo.pbl6_android.data.model.Conversation
import com.demo.pbl6_android.data.model.Message
import com.demo.pbl6_android.data.model.MessageType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.TimeUnit

object ChatManager {
    
    private val _conversations = MutableStateFlow<List<Conversation>>(emptyList())
    val conversations: StateFlow<List<Conversation>> = _conversations.asStateFlow()
    
    private val _messages = MutableStateFlow<Map<String, List<Message>>>(emptyMap())
    val messages: StateFlow<Map<String, List<Message>>> = _messages.asStateFlow()
    
    init {
        loadSampleConversations()
        loadSampleMessages()
    }
    
    fun getConversation(conversationId: String): Conversation? {
        return _conversations.value.find { it.id == conversationId }
    }
    
    fun getMessagesForConversation(conversationId: String): List<Message> {
        return _messages.value[conversationId] ?: emptyList()
    }
    
    fun sendMessage(conversationId: String, content: String) {
        val currentMessages = _messages.value[conversationId]?.toMutableList() ?: mutableListOf()
        
        val newMessage = Message(
            id = "msg_${System.currentTimeMillis()}",
            conversationId = conversationId,
            content = content,
            timestamp = System.currentTimeMillis(),
            isFromUser = true,
            isRead = true,
            messageType = MessageType.TEXT
        )
        
        currentMessages.add(newMessage)
        
        val updatedMessages = _messages.value.toMutableMap()
        updatedMessages[conversationId] = currentMessages
        _messages.value = updatedMessages
        
        // Update conversation's last message
        updateConversationLastMessage(conversationId, content, System.currentTimeMillis(), true)
        
        // Simulate shop response after 2 seconds (in real app, this would come from server)
        // You can implement this with coroutines if needed
    }
    
    fun markConversationAsRead(conversationId: String) {
        val updatedConversations = _conversations.value.map { conversation ->
            if (conversation.id == conversationId) {
                conversation.copy(unreadCount = 0)
            } else {
                conversation
            }
        }
        _conversations.value = updatedConversations
        
        // Mark all messages as read
        val conversationMessages = _messages.value[conversationId]?.map { message ->
            message.copy(isRead = true)
        } ?: emptyList()
        
        val updatedMessages = _messages.value.toMutableMap()
        updatedMessages[conversationId] = conversationMessages
        _messages.value = updatedMessages
    }
    
    private fun updateConversationLastMessage(
        conversationId: String,
        lastMessage: String,
        timestamp: Long,
        isSentByUser: Boolean
    ) {
        val updatedConversations = _conversations.value.map { conversation ->
            if (conversation.id == conversationId) {
                conversation.copy(
                    lastMessage = lastMessage,
                    lastMessageTime = timestamp,
                    isSentByUser = isSentByUser
                )
            } else {
                conversation
            }
        }.sortedByDescending { it.lastMessageTime }
        
        _conversations.value = updatedConversations
    }
    
    private fun loadSampleConversations() {
        val now = System.currentTimeMillis()
        val conversations = listOf(
            Conversation(
                id = "conv_001",
                shopId = "shop_tech_001",
                shopName = "Cửa hàng điện thoại ABC",
                shopAvatar = "",
                lastMessage = "Cảm ơn bạn đã mua hàng! Đơn hàng sẽ được giao trong 2-3 ngày.",
                lastMessageTime = now - TimeUnit.MINUTES.toMillis(30),
                unreadCount = 2,
                isOnline = true,
                isSentByUser = false
            ),
            Conversation(
                id = "conv_002",
                shopId = "shop_tech_002",
                shopName = "Fashion Store XYZ",
                shopAvatar = "",
                lastMessage = "Bạn: Còn size M không ạ?",
                lastMessageTime = now - TimeUnit.HOURS.toMillis(2),
                unreadCount = 0,
                isOnline = false,
                isSentByUser = true
            ),
            Conversation(
                id = "conv_003",
                shopId = "shop_tech_003",
                shopName = "Laptop Gaming Pro",
                shopAvatar = "",
                lastMessage = "Laptop đã được cập nhật firmware mới nhất rồi bạn nhé!",
                lastMessageTime = now - TimeUnit.DAYS.toMillis(1),
                unreadCount = 1,
                isOnline = true,
                isSentByUser = false
            ),
            Conversation(
                id = "conv_004",
                shopId = "shop_beauty_001",
                shopName = "Mỹ phẩm Hàn Quốc",
                shopAvatar = "",
                lastMessage = "Bạn: 📷 Hình ảnh",
                lastMessageTime = now - TimeUnit.DAYS.toMillis(1),
                unreadCount = 0,
                isOnline = false,
                isSentByUser = true
            ),
            Conversation(
                id = "conv_005",
                shopId = "shop_home_001",
                shopName = "Đồ gia dụng thông minh",
                shopAvatar = "",
                lastMessage = "Sản phẩm này có bảo hành 2 năm và miễn phí vận chuyển nhé!",
                lastMessageTime = now - TimeUnit.DAYS.toMillis(2),
                unreadCount = 0,
                isOnline = false,
                isSentByUser = false
            ),
            Conversation(
                id = "conv_006",
                shopId = "shop_fashion_002",
                shopName = "Thời trang nam nữ",
                shopAvatar = "",
                lastMessage = "Flash sale 50% chỉ còn 2 giờ nữa! Nhanh tay đặt hàng nhé!",
                lastMessageTime = now - TimeUnit.DAYS.toMillis(3),
                unreadCount = 0,
                isOnline = true,
                isSentByUser = false
            )
        )
        
        _conversations.value = conversations
    }
    
    private fun loadSampleMessages() {
        val now = System.currentTimeMillis()
        
        // Messages for conversation 1 (Cửa hàng điện thoại ABC)
        val conv1Messages = listOf(
            Message(
                id = "msg_001",
                conversationId = "conv_001",
                content = "Xin chào! Tôi muốn hỏi về sản phẩm này",
                timestamp = now - TimeUnit.HOURS.toMillis(3),
                isFromUser = true,
                isRead = true
            ),
            Message(
                id = "msg_002",
                conversationId = "conv_001",
                content = "Chào bạn! Cảm ơn bạn đã quan tâm đến sản phẩm của shop. Bạn muốn hỏi gì về sản phẩm này ạ?",
                timestamp = now - TimeUnit.HOURS.toMillis(3) + TimeUnit.MINUTES.toMillis(2),
                isFromUser = false,
                isRead = true
            ),
            Message(
                id = "msg_003",
                conversationId = "conv_001",
                content = "Sản phẩm này có màu nào khác không ạ? Và giá có thể thương lượng được không?",
                timestamp = now - TimeUnit.HOURS.toMillis(3) + TimeUnit.MINUTES.toMillis(5),
                isFromUser = true,
                isRead = true
            ),
            Message(
                id = "msg_004",
                conversationId = "conv_001",
                content = "Sản phẩm hiện có 3 màu: đen, trắng và xanh navy. Về giá cả, nếu bạn mua từ 2 sản phẩm trở lên shop có thể giảm 5% ạ",
                timestamp = now - TimeUnit.HOURS.toMillis(3) + TimeUnit.MINUTES.toMillis(7),
                isFromUser = false,
                isRead = true
            ),
            Message(
                id = "msg_005",
                conversationId = "conv_001",
                content = "Vậy tôi đặt 2 cái màu đen và 1 cái màu trắng được không?",
                timestamp = now - TimeUnit.HOURS.toMillis(3) + TimeUnit.MINUTES.toMillis(10),
                isFromUser = true,
                isRead = true
            ),
            Message(
                id = "msg_006",
                conversationId = "conv_001",
                content = "Cảm ơn bạn đã mua hàng! Đơn hàng sẽ được giao trong 2-3 ngày.",
                timestamp = now - TimeUnit.MINUTES.toMillis(30),
                isFromUser = false,
                isRead = false
            )
        )
        
        // Messages for other conversations
        val conv2Messages = listOf(
            Message(
                id = "msg_101",
                conversationId = "conv_002",
                content = "Xin chào shop!",
                timestamp = now - TimeUnit.HOURS.toMillis(3),
                isFromUser = true,
                isRead = true
            ),
            Message(
                id = "msg_102",
                conversationId = "conv_002",
                content = "Còn size M không ạ?",
                timestamp = now - TimeUnit.HOURS.toMillis(2),
                isFromUser = true,
                isRead = true
            )
        )
        
        val messagesMap = mutableMapOf<String, List<Message>>()
        messagesMap["conv_001"] = conv1Messages
        messagesMap["conv_002"] = conv2Messages
        
        _messages.value = messagesMap
    }
}

