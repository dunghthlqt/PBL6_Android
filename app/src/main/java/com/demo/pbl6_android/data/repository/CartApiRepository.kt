package com.demo.pbl6_android.data.repository

import com.demo.pbl6_android.data.api.ApiHelper
import com.demo.pbl6_android.data.api.ApiResult
import com.demo.pbl6_android.data.api.RetrofitClient
import com.demo.pbl6_android.data.api.model.AddToCartRequest
import com.demo.pbl6_android.data.api.model.CartResponse
import com.demo.pbl6_android.data.api.model.UpdateQuantityRequest

/**
 * Repository for Shopping Cart API calls
 * Handles all cart-related API requests
 */
object CartApiRepository {
    
    private val apiService = RetrofitClient.apiService
    
    /**
     * Get shopping cart
     * GET /api/v1/buyer/cart
     */
    suspend fun getCart(): ApiResult<CartResponse> {
        return try {
            android.util.Log.d("CartApiRepository", "🛒 Calling API: getCart()")
            
            val result = ApiHelper.safeApiCall {
                apiService.getCart()
            }
            
            when (result) {
                is ApiResult.Success -> {
                    val cartData = result.data
                    android.util.Log.d("CartApiRepository", "✅ Cart loaded: ${cartData.totalItems} items")
                    android.util.Log.d("CartApiRepository", "📊 Cart data: id=${cartData.id}, userId=${cartData.userId}, items size=${cartData.items?.size}, totalPrice=${cartData.totalPrice}")
                    android.util.Log.d("CartApiRepository", "📦 Full cart data: $cartData")
                    result
                }
                is ApiResult.Error -> {
                    android.util.Log.e("CartApiRepository", "❌ Get cart error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("CartApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi tải giỏ hàng: ${e.message}",
                exception = e
            )
        }
    }
    
    /**
     * Get cart item count
     * GET /api/v1/buyer/cart/count
     */
    suspend fun getCartCount(): ApiResult<Int> {
        return try {
            android.util.Log.d("CartApiRepository", "🔢 Calling API: getCartCount()")
            
            val result = ApiHelper.safeApiCall {
                apiService.getCartCount()
            }
            
            when (result) {
                is ApiResult.Success -> {
                    android.util.Log.d("CartApiRepository", "✅ Cart count: ${result.data.totalItems}")
                    ApiResult.Success(result.data.totalItems)
                }
                is ApiResult.Error -> {
                    android.util.Log.e("CartApiRepository", "❌ Get cart count error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("CartApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi tải số lượng giỏ hàng: ${e.message}",
                exception = e
            )
        }
    }
    
    /**
     * Add item to cart
     * POST /api/v1/buyer/cart/add
     * Note: Backend returns success message, then we reload cart
     */
    suspend fun addToCart(
        productVariantId: String,
        colorId: String?,
        quantity: Int
    ): ApiResult<CartResponse> {
        return try {
            android.util.Log.d("CartApiRepository", "➕ Adding to cart: variant=$productVariantId, color=$colorId, qty=$quantity")
            
            val request = listOf(
                AddToCartRequest(
                    productVariantId = productVariantId,
                    colorId = colorId,
                    quantity = quantity
                )
            )
            
            val addResult = ApiHelper.safeApiCall {
                apiService.addToCart(request)
            }
            
            when (addResult) {
                is ApiResult.Success -> {
                    android.util.Log.d("CartApiRepository", "✅ Added to cart successfully: ${addResult.data}")
                    
                    // Backend only returns success message, so we need to reload cart
                    android.util.Log.d("CartApiRepository", "🔄 Reloading cart after add...")
                    getCart()
                }
                is ApiResult.Error -> {
                    android.util.Log.e("CartApiRepository", "❌ Add to cart error: ${addResult.message}")
                    addResult
                }
                is ApiResult.Loading -> addResult
            }
        } catch (e: Exception) {
            android.util.Log.e("CartApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi thêm vào giỏ hàng: ${e.message}",
                exception = e
            )
        }
    }
    
    /**
     * Update cart item quantity
     * PUT /api/v1/buyer/cart/{productVariantId}
     */
    suspend fun updateCartItemQuantity(
        productVariantId: String,
        colorId: String?,
        quantity: Int
    ): ApiResult<CartResponse> {
        return try {
            android.util.Log.d("CartApiRepository", "🔄 Updating cart item: variant=$productVariantId, qty=$quantity")
            
            val request = UpdateQuantityRequest(quantity = quantity)
            
            val result = ApiHelper.safeApiCall {
                apiService.updateCartItemQuantity(productVariantId, colorId, request)
            }
            
            when (result) {
                is ApiResult.Success -> {
                    android.util.Log.d("CartApiRepository", "✅ Cart item updated successfully")
                    result
                }
                is ApiResult.Error -> {
                    android.util.Log.e("CartApiRepository", "❌ Update cart item error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("CartApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi cập nhật giỏ hàng: ${e.message}",
                exception = e
            )
        }
    }
    
    /**
     * Remove item from cart
     * DELETE /api/v1/buyer/cart/{productVariantId}
     * Note: Backend returns success message, then we reload cart
     */
    suspend fun removeCartItem(
        productVariantId: String,
        colorId: String?
    ): ApiResult<CartResponse> {
        return try {
            android.util.Log.d("CartApiRepository", "🗑️ Removing cart item: variant=$productVariantId")
            
            val removeResult = ApiHelper.safeApiCall {
                apiService.removeCartItem(productVariantId, colorId)
            }
            
            when (removeResult) {
                is ApiResult.Success -> {
                    android.util.Log.d("CartApiRepository", "✅ Cart item removed successfully: ${removeResult.data}")
                    
                    // Backend only returns success message, so we need to reload cart
                    android.util.Log.d("CartApiRepository", "🔄 Reloading cart after remove...")
                    getCart()
                }
                is ApiResult.Error -> {
                    android.util.Log.e("CartApiRepository", "❌ Remove cart item error: ${removeResult.message}")
                    removeResult
                }
                is ApiResult.Loading -> removeResult
            }
        } catch (e: Exception) {
            android.util.Log.e("CartApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi xóa sản phẩm khỏi giỏ hàng: ${e.message}",
                exception = e
            )
        }
    }
    
    /**
     * Clear entire cart
     * DELETE /api/v1/buyer/cart/clear
     */
    suspend fun clearCart(): ApiResult<String> {
        return try {
            android.util.Log.d("CartApiRepository", "🧹 Clearing cart")
            
            val result = ApiHelper.safeApiCall {
                apiService.clearCart()
            }
            
            when (result) {
                is ApiResult.Success -> {
                    android.util.Log.d("CartApiRepository", "✅ Cart cleared successfully")
                    result
                }
                is ApiResult.Error -> {
                    android.util.Log.e("CartApiRepository", "❌ Clear cart error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("CartApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi xóa toàn bộ giỏ hàng: ${e.message}",
                exception = e
            )
        }
    }
}

