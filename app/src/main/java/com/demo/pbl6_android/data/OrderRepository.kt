package com.demo.pbl6_android.data

import com.demo.pbl6_android.data.api.ApiResult
import com.demo.pbl6_android.data.api.model.AddressDTO
import com.demo.pbl6_android.data.api.model.CheckoutResponse
import com.demo.pbl6_android.data.api.model.OrderDTO
import com.demo.pbl6_android.data.api.model.OrderItemResponse
import com.demo.pbl6_android.data.api.model.OrderResponse
import com.demo.pbl6_android.data.model.Address
import com.demo.pbl6_android.data.model.Order
import com.demo.pbl6_android.data.model.OrderItem
import com.demo.pbl6_android.data.model.OrderStatus
import com.demo.pbl6_android.data.repository.OrderApiRepository
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Repository for Order management
 * Now uses API instead of mock data
 */
object OrderRepository {
    
    // ============================================
    // PUBLIC API METHODS
    // ============================================
    
    /**
     * Checkout cart and create orders
     */
    suspend fun checkout(
        address: Address,
        paymentMethod: String,
        note: String? = null,
        promotionCode: String? = null
    ): ApiResult<CheckoutResponse> {
        val addressDTO = AddressDTO(
            province = address.province,
            ward = address.ward,
            homeAddress = address.street,
            suggestedName = address.recipientName.ifEmpty { null }
        )
        
        val orderDTO = OrderDTO(
            note = note,
            address = addressDTO,
            paymentMethod = paymentMethod,
            promotionCode = promotionCode
        )
        
        return OrderApiRepository.checkout(orderDTO)
    }
    
    /**
     * Get order history (all orders)
     */
    suspend fun getAllOrders(page: Int = 1, size: Int = 10): ApiResult<List<Order>> {
        return when (val result = OrderApiRepository.getOrderHistory(page, size, null)) {
            is ApiResult.Success -> {
                val orders = result.data.content?.map { it.toOrder() } ?: emptyList()
                ApiResult.Success(orders)
            }
            is ApiResult.Error -> result
            is ApiResult.Loading -> ApiResult.Loading
        }
    }
    
    /**
     * Get orders filtered by status
     */
    suspend fun getOrdersByStatus(status: OrderStatus, page: Int = 1, size: Int = 10): ApiResult<List<Order>> {
        val statusString = mapLocalStatusToApiStatus(status)
        return when (val result = OrderApiRepository.getOrderHistory(page, size, statusString)) {
            is ApiResult.Success -> {
                val orders = result.data.content?.map { it.toOrder() } ?: emptyList()
                ApiResult.Success(orders)
            }
            is ApiResult.Error -> result
            is ApiResult.Loading -> ApiResult.Loading
        }
    }
    
    /**
     * Get order detail by ID
     */
    suspend fun getOrderById(orderId: String): ApiResult<Order> {
        return when (val result = OrderApiRepository.getOrderDetail(orderId)) {
            is ApiResult.Success -> {
                ApiResult.Success(result.data.toOrder())
            }
            is ApiResult.Error -> result
            is ApiResult.Loading -> ApiResult.Loading
        }
    }
    
    /**
     * Cancel order
     */
    suspend fun cancelOrder(orderId: String, reason: String?): ApiResult<Order> {
        return when (val result = OrderApiRepository.cancelOrder(orderId, reason)) {
            is ApiResult.Success -> {
                ApiResult.Success(result.data.toOrder())
            }
            is ApiResult.Error -> result
            is ApiResult.Loading -> ApiResult.Loading
        }
    }
    
    // ============================================
    // CONVERSION FUNCTIONS
    // ============================================
    
    /**
     * Convert OrderResponse to Order domain model
     */
    private fun OrderResponse.toOrder(): Order {
        return Order(
            orderId = this.id,
            orderCode = this.orderCode ?: this.id.take(8).uppercase(),
            orderDate = parseTimestamp(this.createdAt),
            status = mapApiStatusToLocalStatus(this.status),
            items = this.items.map { it.toOrderItem() },
            totalAmount = this.finalPrice.toInt(),
            shopId = this.store?.id ?: "unknown_store",
            shopName = this.store?.name ?: "Unknown Store",
            shippingAddress = formatAddress(this.deliveryAddress),
            paymentMethod = this.paymentMethod ?: "COD",
            receivedDate = if (this.status == "DELIVERED") parseTimestamp(this.deliveredAt) else null
        )
    }
    
    /**
     * Convert OrderItemResponse to OrderItem
     */
    private fun OrderItemResponse.toOrderItem(): OrderItem {
        return OrderItem(
            productId = this.productVariantId,
            productName = this.productName,
            productImage = this.imageUrl ?: "",
            quantity = this.quantity,
            price = this.price.toInt(),
            color = this.colorName ?: "Default",
            size = this.variantName ?: "Standard"
        )
    }
    
    /**
     * Map API order status to local OrderStatus enum
     */
    private fun mapApiStatusToLocalStatus(apiStatus: String): OrderStatus {
        return when (apiStatus) {
            "PENDING" -> OrderStatus.PENDING_PAYMENT
            "CONFIRMED" -> OrderStatus.PENDING_PICKUP
            "SHIPPING" -> OrderStatus.SHIPPING
            "DELIVERED" -> OrderStatus.DELIVERED
            "CANCELLED" -> OrderStatus.CANCELLED
            else -> OrderStatus.PENDING_PAYMENT
        }
    }
    
    /**
     * Map local OrderStatus to API status string
     */
    private fun mapLocalStatusToApiStatus(localStatus: OrderStatus): String {
        return when (localStatus) {
            OrderStatus.PENDING_PAYMENT -> "PENDING"
            OrderStatus.PENDING_PICKUP -> "CONFIRMED"
            OrderStatus.SHIPPING -> "SHIPPING"
            OrderStatus.DELIVERED -> "DELIVERED"
            OrderStatus.CANCELLED -> "CANCELLED"
            OrderStatus.RETURN_REFUND -> "CANCELLED" // Map to closest API status
        }
    }
    
    /**
     * Format delivery address from OrderAddressResponse
     */
    private fun formatAddress(address: com.demo.pbl6_android.data.api.model.OrderAddressResponse?): String {
        if (address == null) return "Không có địa chỉ"
        
        val parts = listOfNotNull(
            address.homeAddress,
            address.ward,
            address.province
        ).filter { it.isNotEmpty() }
        
        return if (parts.isNotEmpty()) {
            parts.joinToString(", ")
        } else {
            "Không có địa chỉ"
        }
    }
    
    /**
     * Parse ISO timestamp to Long (milliseconds)
     */
    private fun parseTimestamp(timestamp: String?): Long {
        if (timestamp == null) return System.currentTimeMillis()
        
        return try {
            // Try parsing ISO 8601 format
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            format.parse(timestamp)?.time ?: System.currentTimeMillis()
        } catch (e: Exception) {
            android.util.Log.w("OrderRepository", "Failed to parse timestamp: $timestamp")
            System.currentTimeMillis()
        }
    }
}
