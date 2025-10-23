package com.demo.pbl6_android.data.api.model

import com.google.gson.annotations.SerializedName

/**
 * Order DTO for checkout
 * Based on: POST /api/v1/buyer/orders/checkout
 */
data class OrderDTO(
    @SerializedName("note")
    val note: String?,
    
    @SerializedName("address")
    val address: AddressDTO,
    
    @SerializedName("payment_method")
    val paymentMethod: String,
    
    @SerializedName("promotion_code")
    val promotionCode: String?
)

/**
 * Order response from API
 * Based on: GET /api/v1/buyer/orders
 */
data class OrderResponse(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("orderCode")
    val orderCode: String?,
    
    @SerializedName("user")
    val user: OrderUserResponse?,
    
    @SerializedName("store")
    val store: OrderStoreResponse?,
    
    @SerializedName("items")
    val items: List<OrderItemResponse>,
    
    @SerializedName("totalPrice")
    val totalPrice: Long,
    
    @SerializedName("shippingFee")
    val shippingFee: Long?,
    
    @SerializedName("discount")
    val discount: Long?,
    
    @SerializedName("finalPrice")
    val finalPrice: Long,
    
    @SerializedName("status")
    val status: String, // PENDING, CONFIRMED, SHIPPING, DELIVERED, CANCELLED
    
    @SerializedName("paymentMethod")
    val paymentMethod: String?,
    
    @SerializedName("paymentStatus")
    val paymentStatus: String?,
    
    @SerializedName("note")
    val note: String?,
    
    @SerializedName("cancelReason")
    val cancelReason: String?,
    
    @SerializedName("deliveryAddress")
    val deliveryAddress: OrderAddressResponse?,
    
    @SerializedName("createdAt")
    val createdAt: String?,
    
    @SerializedName("updatedAt")
    val updatedAt: String?,
    
    @SerializedName("confirmedAt")
    val confirmedAt: String?,
    
    @SerializedName("shippedAt")
    val shippedAt: String?,
    
    @SerializedName("deliveredAt")
    val deliveredAt: String?,
    
    @SerializedName("cancelledAt")
    val cancelledAt: String?
)

/**
 * Order item in order response
 */
data class OrderItemResponse(
    @SerializedName("id")
    val id: String?,
    
    @SerializedName("productVariantId")
    val productVariantId: String,
    
    @SerializedName("productName")
    val productName: String,
    
    @SerializedName("variantName")
    val variantName: String?,
    
    @SerializedName("imageUrl")
    val imageUrl: String?,
    
    @SerializedName("price")
    val price: Long,
    
    @SerializedName("quantity")
    val quantity: Int,
    
    @SerializedName("colorId")
    val colorId: String?,
    
    @SerializedName("colorName")
    val colorName: String?,
    
    @SerializedName("subtotal")
    val subtotal: Long
)

/**
 * User info in order response
 */
data class OrderUserResponse(
    @SerializedName("id")
    val id: String?,
    
    @SerializedName("fullName")
    val fullName: String?,
    
    @SerializedName("email")
    val email: String?,
    
    @SerializedName("phone")
    val phone: String?
)

/**
 * Store info in order response
 */
data class OrderStoreResponse(
    @SerializedName("id")
    val id: String?,
    
    @SerializedName("name")
    val name: String?,
    
    @SerializedName("logoUrl")
    val logoUrl: String?,
    
    @SerializedName("phone")
    val phone: String?
)

/**
 * Delivery address in order response
 */
data class OrderAddressResponse(
    @SerializedName("province")
    val province: String?,
    
    @SerializedName("ward")
    val ward: String?,
    
    @SerializedName("homeAddress")
    val homeAddress: String?,
    
    @SerializedName("suggestedName")
    val suggestedName: String?,
    
    @SerializedName("recipientName")
    val recipientName: String?,
    
    @SerializedName("recipientPhone")
    val recipientPhone: String?
)

/**
 * Order status enum
 */
enum class OrderStatus(val value: String) {
    PENDING("PENDING"),
    CONFIRMED("CONFIRMED"),
    SHIPPING("SHIPPING"),
    DELIVERED("DELIVERED"),
    CANCELLED("CANCELLED");
    
    companion object {
        fun fromString(value: String): OrderStatus {
            return values().find { it.value == value } ?: PENDING
        }
    }
}

/**
 * Payment method enum
 */
enum class PaymentMethod(val value: String) {
    COD("COD"), // Cash on Delivery
    BANK_TRANSFER("BANK_TRANSFER"),
    MOMO("MOMO"),
    VNPAY("VNPAY");
    
    companion object {
        fun fromString(value: String): PaymentMethod {
            return values().find { it.value == value } ?: COD
        }
    }
}

/**
 * Checkout response (multiple orders grouped by store)
 */
data class CheckoutResponse(
    @SerializedName("orders")
    val orders: List<OrderResponse>,
    
    @SerializedName("totalAmount")
    val totalAmount: Long?,
    
    @SerializedName("message")
    val message: String?
)

