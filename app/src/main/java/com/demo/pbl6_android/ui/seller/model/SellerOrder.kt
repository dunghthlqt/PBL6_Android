package com.demo.pbl6_android.ui.seller.model

data class SellerOrder(
    val orderId: String,
    val orderCode: String,
    val buyerName: String,
    val buyerAvatar: String = "",
    val status: SellerOrderStatus,
    val subStatus: String? = null,
    val products: List<OrderProduct>,
    val totalAmount: Int,
    val shippingMethod: ShippingMethod,
    val orderDate: Long,
    val confirmDate: Long? = null,
    val deliveryDeadline: Long? = null,
    val isRated: Boolean = false,
    val cancelledBy: CancelledBy? = null,
    val cancellationReason: CancellationReason? = null,
    val cancellationDate: Long? = null
)

data class OrderProduct(
    val productId: String,
    val productName: String,
    val imageUrl: String,
    val variant: String, // e.g., "Color: Red, Size: M"
    val quantity: Int,
    val price: Int
)

