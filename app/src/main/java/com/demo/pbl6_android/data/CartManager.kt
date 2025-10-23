package com.demo.pbl6_android.data

import com.demo.pbl6_android.data.api.ApiResult
import com.demo.pbl6_android.data.api.model.CartItemResponse
import com.demo.pbl6_android.data.api.model.CartResponse
import com.demo.pbl6_android.data.model.Product
import com.demo.pbl6_android.data.repository.CartApiRepository
import com.demo.pbl6_android.ui.cart.model.CartProduct
import com.demo.pbl6_android.ui.cart.model.CartShop
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * CartManager - Handles shopping cart with API integration
 * Syncs with backend API and manages local state
 */
object CartManager {
    
    private val _cartShops = MutableStateFlow<List<CartShop>>(emptyList())
    val cartShops: StateFlow<List<CartShop>> = _cartShops.asStateFlow()
    
    private val _cartItemCount = MutableStateFlow(0)
    val cartItemCount: StateFlow<Int> = _cartItemCount.asStateFlow()
    
    /**
     * Load cart from API
     */
    suspend fun loadCart(): ApiResult<Unit> {
        return try {
            when (val result = CartApiRepository.getCart()) {
                is ApiResult.Success -> {
                    updateLocalCartFromApi(result.data)
                    ApiResult.Success(Unit)
                }
                is ApiResult.Error -> {
                    android.util.Log.e("CartManager", "Failed to load cart: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("CartManager", "Exception loading cart", e)
            ApiResult.Error("Lỗi khi tải giỏ hàng: ${e.message}", exception = e)
        }
    }
    
    /**
     * Add product to cart via API
     */
    suspend fun addToCart(
        product: Product,
        selectedColor: String,
        selectedSize: String,
        quantity: Int
    ): ApiResult<Unit> {
        return try {
            // Use product ID as productVariantId
            // TODO: When product variants are available, use the actual variant ID
            val result = CartApiRepository.addToCart(
                productVariantId = product.id,
                colorId = null, // TODO: Get actual color ID from product
                quantity = quantity
            )
            
            when (result) {
                is ApiResult.Success -> {
                    updateLocalCartFromApi(result.data)
                    ApiResult.Success(Unit)
                }
                is ApiResult.Error -> result
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("CartManager", "Exception adding to cart", e)
            ApiResult.Error("Lỗi khi thêm vào giỏ hàng: ${e.message}", exception = e)
        }
    }
    
    /**
     * Update cart item quantity via API
     */
    suspend fun updateQuantity(
        productVariantId: String,
        colorId: String?,
        newQuantity: Int
    ): ApiResult<Unit> {
        return try {
            val result = CartApiRepository.updateCartItemQuantity(
                productVariantId = productVariantId,
                colorId = colorId,
                quantity = newQuantity
            )
            
            when (result) {
                is ApiResult.Success -> {
                    updateLocalCartFromApi(result.data)
                    ApiResult.Success(Unit)
                }
                is ApiResult.Error -> result
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("CartManager", "Exception updating quantity", e)
            ApiResult.Error("Lỗi khi cập nhật số lượng: ${e.message}", exception = e)
        }
    }
    
    /**
     * Remove item from cart via API
     */
    suspend fun removeFromCart(
        productVariantId: String,
        colorId: String?
    ): ApiResult<Unit> {
        return try {
            val result = CartApiRepository.removeCartItem(
                productVariantId = productVariantId,
                colorId = colorId
            )
            
            when (result) {
                is ApiResult.Success -> {
                    updateLocalCartFromApi(result.data)
                    ApiResult.Success(Unit)
                }
                is ApiResult.Error -> result
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("CartManager", "Exception removing from cart", e)
            ApiResult.Error("Lỗi khi xóa sản phẩm: ${e.message}", exception = e)
        }
    }
    
    /**
     * Clear entire cart via API
     */
    suspend fun clearCart(): ApiResult<Unit> {
        return try {
            val result = CartApiRepository.clearCart()
            
            when (result) {
                is ApiResult.Success -> {
                    _cartShops.value = emptyList()
                    _cartItemCount.value = 0
                    ApiResult.Success(Unit)
                }
                is ApiResult.Error -> result
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("CartManager", "Exception clearing cart", e)
            ApiResult.Error("Lỗi khi xóa giỏ hàng: ${e.message}", exception = e)
        }
    }
    
    /**
     * Get cart shops (local state)
     */
    fun getCartShops(): List<CartShop> {
        return _cartShops.value
    }
    
    /**
     * Update local cart state from API response
     */
    private fun updateLocalCartFromApi(cartResponse: CartResponse) {
        try {
            android.util.Log.d("CartManager", "🔄 Updating local cart from API response...")
            
            // Assign to local variable to avoid smart cast issues with computed property
            val items = cartResponse.items
            
            // Check if items is null or empty
            if (items == null) {
                android.util.Log.w("CartManager", "⚠️ Cart items is null, clearing local cart")
                _cartShops.value = emptyList()
                _cartItemCount.value = 0
                return
            }
            
            if (items.isEmpty()) {
                android.util.Log.d("CartManager", "📭 Cart is empty")
                _cartShops.value = emptyList()
                _cartItemCount.value = 0
                return
            }
            
            // Group cart items by store
            // Note: API doesn't return store info, so we group all in one "default" store for now
            val shopMap = mutableMapOf<String, MutableList<CartProduct>>()
            val storeNames = mutableMapOf<String, String>()
            
            items.forEach { item ->
                // Since API doesn't provide store info, use a default store
                val storeId = "default_store"
                val storeName = "Cửa hàng của tôi"
                
                storeNames[storeId] = storeName
                
                val cartProduct = CartProduct(
                    id = item.productId,
                    name = item.productName,
                    color = item.colorName ?: "Default",
                    size = "Standard",  // API doesn't provide size info
                    currentPrice = item.price.toInt(),
                    originalPrice = (item.price * 1.2).toInt(), // Assume 20% discount
                    quantity = item.quantity,
                    imageUrl = item.imageUrl ?: "",
                    isSelected = false
                )
                
                shopMap.getOrPut(storeId) { mutableListOf() }.add(cartProduct)
            }
            
            // Convert to CartShop list
            val cartShops = shopMap.map { (storeId, products) ->
                CartShop(
                    id = storeId,
                    name = storeNames[storeId] ?: "Unknown Store",
                    products = products,
                    isSelected = false
                )
            }
            
            _cartShops.value = cartShops
            _cartItemCount.value = cartResponse.totalItems
            
            android.util.Log.d("CartManager", "✅ Cart updated: ${cartResponse.totalItems} items in ${cartShops.size} shops")
        } catch (e: Exception) {
            android.util.Log.e("CartManager", "💥 Error updating local cart from API", e)
            // Set empty cart on error to prevent crash
            _cartShops.value = emptyList()
            _cartItemCount.value = 0
        }
    }
}
