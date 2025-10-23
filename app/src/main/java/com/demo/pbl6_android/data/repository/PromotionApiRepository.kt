package com.demo.pbl6_android.data.repository

import com.demo.pbl6_android.data.api.ApiHelper
import com.demo.pbl6_android.data.api.ApiResult
import com.demo.pbl6_android.data.api.RetrofitClient
import com.demo.pbl6_android.data.api.model.DiscountCalculationResponse
import com.demo.pbl6_android.data.api.model.PromotionResponse
import com.demo.pbl6_android.data.api.model.PromotionValidationResponse

/**
 * Repository for Promotion/Voucher API calls
 * Handles all promotion-related API requests
 */
object PromotionApiRepository {

    private val apiService = RetrofitClient.apiService

    /**
     * Get all active promotions (platform-wide)
     * GET /api/v1/b2c/promotions/active
     */
    suspend fun getActivePromotions(): ApiResult<List<PromotionResponse>> {
        return try {
            android.util.Log.d("PromotionApiRepository", "🎫 Getting all active promotions...")

            val result = ApiHelper.safeApiCall {
                apiService.getActivePromotions()
            }

            when (result) {
                is ApiResult.Success -> {
                    android.util.Log.d("PromotionApiRepository", "✅ Active promotions loaded: ${result.data.size} promotions")
                    result
                }
                is ApiResult.Error -> {
                    android.util.Log.e("PromotionApiRepository", "❌ Get promotions error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("PromotionApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi tải khuyến mãi: ${e.message}",
                exception = e
            )
        }
    }

    /**
     * Get active promotions by store
     * GET /api/v1/b2c/promotions/active/store/{storeId}
     */
    suspend fun getActivePromotionsByStore(storeId: String): ApiResult<List<PromotionResponse>> {
        return try {
            android.util.Log.d("PromotionApiRepository", "🎫 Getting active promotions for store: id=$storeId")

            val result = ApiHelper.safeApiCall {
                apiService.getActivePromotionsByStore(storeId)
            }

            when (result) {
                is ApiResult.Success -> {
                    android.util.Log.d("PromotionApiRepository", "✅ Store promotions loaded: ${result.data.size} promotions")
                    result
                }
                is ApiResult.Error -> {
                    android.util.Log.e("PromotionApiRepository", "❌ Get store promotions error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("PromotionApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi tải khuyến mãi của shop: ${e.message}",
                exception = e
            )
        }
    }

    /**
     * Get promotion by ID
     * GET /api/v1/b2c/promotions/{promotionId}
     */
    suspend fun getPromotionById(promotionId: String): ApiResult<PromotionResponse> {
        return try {
            android.util.Log.d("PromotionApiRepository", "🎫 Getting promotion: id=$promotionId")

            val result = ApiHelper.safeApiCall {
                apiService.getPromotionById(promotionId)
            }

            when (result) {
                is ApiResult.Success -> {
                    android.util.Log.d("PromotionApiRepository", "✅ Promotion loaded: ${result.data.title}")
                    result
                }
                is ApiResult.Error -> {
                    android.util.Log.e("PromotionApiRepository", "❌ Get promotion error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("PromotionApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi tải thông tin khuyến mãi: ${e.message}",
                exception = e
            )
        }
    }

    /**
     * Validate promotion code/voucher
     * POST /api/v1/b2c/promotions/{promotionId}/validate
     */
    suspend fun validatePromotion(
        promotionId: String,
        orderValue: Long
    ): ApiResult<PromotionValidationResponse> {
        return try {
            android.util.Log.d("PromotionApiRepository", "✅ Validating promotion: id=$promotionId, orderValue=$orderValue")

            val result = ApiHelper.safeApiCall {
                apiService.validatePromotion(promotionId, orderValue)
            }

            when (result) {
                is ApiResult.Success -> {
                    val isValid = result.data.valid
                    android.util.Log.d("PromotionApiRepository", "✅ Validation result: ${if (isValid) "VALID" else "INVALID"}")
                    result
                }
                is ApiResult.Error -> {
                    android.util.Log.e("PromotionApiRepository", "❌ Validate promotion error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("PromotionApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi xác thực mã giảm giá: ${e.message}",
                exception = e
            )
        }
    }

    /**
     * Calculate discount amount
     * POST /api/v1/b2c/promotions/{promotionId}/calculate-discount
     */
    suspend fun calculateDiscount(
        promotionId: String,
        orderValue: Long
    ): ApiResult<DiscountCalculationResponse> {
        return try {
            android.util.Log.d("PromotionApiRepository", "💰 Calculating discount: id=$promotionId, orderValue=$orderValue")

            val result = ApiHelper.safeApiCall {
                apiService.calculateDiscount(promotionId, orderValue)
            }

            when (result) {
                is ApiResult.Success -> {
                    android.util.Log.d("PromotionApiRepository", "✅ Discount calculated: ${result.data.discountAmount}đ")
                    result
                }
                is ApiResult.Error -> {
                    android.util.Log.e("PromotionApiRepository", "❌ Calculate discount error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("PromotionApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi tính toán giảm giá: ${e.message}",
                exception = e
            )
        }
    }

    /**
     * Get all promotions by store (not just active)
     * GET /api/v1/b2c/promotions/store/{storeId}
     */
    suspend fun getPromotionsByStore(storeId: String): ApiResult<List<PromotionResponse>> {
        return try {
            android.util.Log.d("PromotionApiRepository", "🎫 Getting all promotions for store: id=$storeId")

            val result = ApiHelper.safeApiCall {
                apiService.getPromotionsByStore(storeId)
            }

            when (result) {
                is ApiResult.Success -> {
                    android.util.Log.d("PromotionApiRepository", "✅ All store promotions loaded: ${result.data.size} promotions")
                    result
                }
                is ApiResult.Error -> {
                    android.util.Log.e("PromotionApiRepository", "❌ Get all store promotions error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("PromotionApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi tải tất cả khuyến mãi của shop: ${e.message}",
                exception = e
            )
        }
    }
}

