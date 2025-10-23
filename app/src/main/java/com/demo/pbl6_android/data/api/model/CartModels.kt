package com.demo.pbl6_android.data.api.model

import com.google.gson.annotations.SerializedName

/**
 * Cart response from API
 * Based on: GET /api/v1/buyer/cart
 * ACTUAL API RESPONSE STRUCTURE
 */
data class CartResponse(
    @SerializedName("id")
    val id: String?,
    
    @SerializedName("user")
    val user: CartUserResponse?,
    
    @SerializedName("cartItems")  // ← API uses "cartItems" not "items"
    val cartItems: List<CartItemResponse>?,
    
    @SerializedName("totalPrice")
    val totalPrice: Long,
    
    @SerializedName("createdAt")
    val createdAt: String?,
    
    @SerializedName("updatedAt")
    val updatedAt: String?
) {
    // Computed property for backward compatibility
    val items: List<CartItemResponse>?
        get() = cartItems
    
    val totalItems: Int
        get() = cartItems?.size ?: 0
    
    val userId: String?
        get() = user?.id
}

/**
 * User info in cart response
 */
data class CartUserResponse(
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
 * Cart item response
 * ACTUAL API RESPONSE STRUCTURE
 */
data class CartItemResponse(
    @SerializedName("productId")  // ← API uses productId not productVariantId
    val productId: String,
    
    @SerializedName("productName")
    val productName: String,
    
    @SerializedName("imageUrl")
    val imageUrl: String?,
    
    @SerializedName("quantity")
    val quantity: Int,
    
    @SerializedName("price")
    val price: Long,
    
    @SerializedName("colorId")
    val colorId: String?,
    
    @SerializedName("colorName")
    val colorName: String?
) {
    // Computed properties for backward compatibility
    val productVariantId: String
        get() = productId
    
    val productVariant: ProductVariantResponse?
        get() = null  // Not available in this API response
    
    val subtotal: Long
        get() = price * quantity
}

/**
 * DTO for adding items to cart
 * Based on: POST /api/v1/buyer/cart/add
 */
data class AddToCartRequest(
    @SerializedName("productVariantId")
    val productVariantId: String,
    
    @SerializedName("colorId")
    val colorId: String?,
    
    @SerializedName("quantity")
    val quantity: Int
)

/**
 * DTO for updating cart item quantity
 * Based on: PUT /api/v1/buyer/cart/{productVariantId}
 */
data class UpdateQuantityRequest(
    @SerializedName("quantity")
    val quantity: Int
)

/**
 * Cart count response
 * Based on: GET /api/v1/buyer/cart/count
 */
data class CartCountResponse(
    @SerializedName("totalItems")
    val totalItems: Int,
    
    @SerializedName("isEmpty")
    val isEmpty: Boolean
)

