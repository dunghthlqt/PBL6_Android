package com.demo.pbl6_android.data.repository

import com.demo.pbl6_android.data.api.ApiHelper
import com.demo.pbl6_android.data.api.ApiResult
import com.demo.pbl6_android.data.api.RetrofitClient
import com.demo.pbl6_android.data.api.model.CheckoutResponse
import com.demo.pbl6_android.data.api.model.OrderDTO
import com.demo.pbl6_android.data.api.model.OrderResponse
import com.demo.pbl6_android.data.api.model.PageResponse

/**
 * Repository for Order API calls
 * Handles all order-related API requests including checkout, history, detail, and cancellation
 */
object OrderApiRepository {

    private val apiService = RetrofitClient.apiService

    /**
     * Checkout and create order
     * POST /api/v1/buyer/orders/checkout
     */
    suspend fun checkout(orderDTO: OrderDTO): ApiResult<CheckoutResponse> {
        return try {
            android.util.Log.d("OrderApiRepository", "🛒 Calling API: checkout()")
            android.util.Log.d("OrderApiRepository", "📦 Order data: address=${orderDTO.address.province}, payment=${orderDTO.paymentMethod}")

            val result = ApiHelper.safeApiCall {
                apiService.checkout(orderDTO)
            }

            when (result) {
                is ApiResult.Success -> {
                    android.util.Log.d("OrderApiRepository", "✅ Checkout successful: ${result.data.orders.size} orders created")
                    result
                }
                is ApiResult.Error -> {
                    android.util.Log.e("OrderApiRepository", "❌ Checkout error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("OrderApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi đặt hàng: ${e.message}",
                exception = e
            )
        }
    }

    /**
     * Get order history with pagination
     * GET /api/v1/buyer/orders
     */
    suspend fun getOrderHistory(
        page: Int = 1,
        size: Int = 10,
        status: String? = null
    ): ApiResult<PageResponse<OrderResponse>> {
        return try {
            android.util.Log.d("OrderApiRepository", "📜 Calling API: getOrderHistory(page=$page, size=$size, status=$status)")

            val result = ApiHelper.safeApiCall {
                apiService.getOrderHistory(page, size, status)
            }

            when (result) {
                is ApiResult.Success -> {
                    android.util.Log.d("OrderApiRepository", "✅ Order history loaded: ${result.data.content?.size ?: 0} orders")
                    result
                }
                is ApiResult.Error -> {
                    android.util.Log.e("OrderApiRepository", "❌ Get order history error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("OrderApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi tải lịch sử đơn hàng: ${e.message}",
                exception = e
            )
        }
    }

    /**
     * Get order detail by ID
     * GET /api/v1/buyer/orders/{orderId}
     */
    suspend fun getOrderDetail(orderId: String): ApiResult<OrderResponse> {
        return try {
            android.util.Log.d("OrderApiRepository", "🔍 Calling API: getOrderDetail(orderId=$orderId)")

            val result = ApiHelper.safeApiCall {
                apiService.getOrderDetail(orderId)
            }

            when (result) {
                is ApiResult.Success -> {
                    android.util.Log.d("OrderApiRepository", "✅ Order detail loaded: ${result.data.orderCode}, status=${result.data.status}")
                    result
                }
                is ApiResult.Error -> {
                    android.util.Log.e("OrderApiRepository", "❌ Get order detail error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("OrderApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi tải chi tiết đơn hàng: ${e.message}",
                exception = e
            )
        }
    }

    /**
     * Cancel order
     * PUT /api/v1/buyer/orders/{orderId}/cancel
     */
    suspend fun cancelOrder(orderId: String, reason: String?): ApiResult<OrderResponse> {
        return try {
            android.util.Log.d("OrderApiRepository", "❌ Calling API: cancelOrder(orderId=$orderId, reason=$reason)")

            val result = ApiHelper.safeApiCall {
                apiService.cancelOrder(orderId, reason)
            }

            when (result) {
                is ApiResult.Success -> {
                    android.util.Log.d("OrderApiRepository", "✅ Order cancelled successfully: ${result.data.orderCode}")
                    result
                }
                is ApiResult.Error -> {
                    android.util.Log.e("OrderApiRepository", "❌ Cancel order error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("OrderApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi hủy đơn hàng: ${e.message}",
                exception = e
            )
        }
    }
}

