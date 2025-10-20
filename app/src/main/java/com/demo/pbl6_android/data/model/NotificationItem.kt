package com.demo.pbl6_android.data.model

data class NotificationItem(
    val id: String,
    val title: String,
    val description: String,
    val iconResId: Int,
    val badgeCount: Int = 0,
    val type: NotificationType
)

enum class NotificationType {
    PROMOTION,
    LIVE_VIDEO,
    FINANCIAL,
    ORDER_UPDATE,
    SELLER_WALLET,
    MARKETING,
    PERFORMANCE
}

