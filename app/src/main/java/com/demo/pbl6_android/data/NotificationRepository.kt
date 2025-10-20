package com.demo.pbl6_android.data

import com.demo.pbl6_android.R
import com.demo.pbl6_android.data.model.NotificationItem
import com.demo.pbl6_android.data.model.NotificationType

object NotificationRepository {
    
    fun getMyNotifications(): List<NotificationItem> {
        return listOf(
            NotificationItem(
                id = "1",
                title = "Khuyến mãi",
                description = "🤩Tặng bạn mã giảm đến 1 TRIỆU 📱Sắm điện thoại Android cũ ⭐Hàng chính hãng - Bảo hành d...",
                iconResId = R.drawable.ic_promotion,
                badgeCount = 0,
                type = NotificationType.PROMOTION
            ),
            NotificationItem(
                id = "2",
                title = "Live & Video",
                description = "CS_Gaming.house_Tư vấn sale",
                iconResId = R.drawable.ic_live_video,
                badgeCount = 0,
                type = NotificationType.LIVE_VIDEO
            ),
            NotificationItem(
                id = "3",
                title = "Thông tin Tài chính",
                description = "Giao dịch PPSA106002509621238801240 thanh toán thành công qua ShopeePay. Số tiền đ29,000 ...",
                iconResId = R.drawable.ic_financial,
                badgeCount = 0,
                type = NotificationType.FINANCIAL
            ),
            NotificationItem(
                id = "4",
                title = "Cập nhật đơn hàng",
                description = "Chưa có cập nhật đơn hàng",
                iconResId = R.drawable.ic_order_box,
                badgeCount = 0,
                type = NotificationType.ORDER_UPDATE
            )
        )
    }
    
    fun getSellerNotifications(): List<NotificationItem> {
        return listOf(
            NotificationItem(
                id = "5",
                title = "Cập nhật đơn hàng",
                description = "Chưa có cập nhật đơn hàng",
                iconResId = R.drawable.ic_order_box,
                badgeCount = 0,
                type = NotificationType.ORDER_UPDATE
            ),
            NotificationItem(
                id = "6",
                title = "Ví người bán",
                description = "Chưa có cập nhật ví người bán",
                iconResId = R.drawable.ic_wallet,
                badgeCount = 0,
                type = NotificationType.SELLER_WALLET
            ),
            NotificationItem(
                id = "7",
                title = "Kênh Marketing",
                description = "và 7 chương trình khác bật đầu mở đăng ký. Tham gia ngay>",
                iconResId = R.drawable.ic_marketing,
                badgeCount = 1,
                type = NotificationType.MARKETING
            ),
            NotificationItem(
                id = "8",
                title = "Hiệu quả hoạt động",
                description = "Hãy xem phân tích bán hàng của Shop hàng tuần nhé! Shop của bạn đã đạt các tiêu chỉ nào để ...",
                iconResId = R.drawable.ic_performance,
                badgeCount = 4,
                type = NotificationType.PERFORMANCE
            )
        )
    }
    
    fun getTotalMyNotificationsCount(): Int {
        return getMyNotifications().sumOf { it.badgeCount }
    }
    
    fun getTotalSellerNotificationsCount(): Int {
        return getSellerNotifications().sumOf { it.badgeCount }
    }
}

