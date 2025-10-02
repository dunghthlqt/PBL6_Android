package com.demo.pbl6_android.data.model

data class Order(
    val orderId: String,
    val orderCode: String,
    val orderDate: Long,
    val status: OrderStatus,
    val items: List<OrderItem>,
    val totalAmount: Int,
    val shopId: String,
    val shopName: String,
    val shippingAddress: String,
    val paymentMethod: String,
    val receivedDate: Long? = null
)

data class OrderItem(
    val productId: String,
    val productName: String,
    val productImage: String,
    val quantity: Int,
    val price: Int,
    val color: String,
    val size: String
)

enum class OrderStatus(val displayName: String, val color: String) {
    PENDING_PAYMENT("Chờ thanh toán", "#F59E0B"),
    PENDING_PICKUP("Chờ lấy hàng", "#3B82F6"),
    SHIPPING("Chờ giao hàng", "#EF4444"),
    DELIVERED("Đã giao", "#10B981"),
    RETURN_REFUND("Trả hàng/Hoàn tiền", "#F59E0B"),
    CANCELLED("Đã hủy", "#64748B");
    
    companion object {
        fun getAllStatuses(): List<OrderStatus> {
            return values().toList()
        }
    }
}

