package com.demo.pbl6_android.data.repository

import com.demo.pbl6_android.data.api.ApiHelper
import com.demo.pbl6_android.data.api.ApiResult
import com.demo.pbl6_android.data.api.RetrofitClient
import com.demo.pbl6_android.data.api.model.PageResponse
import com.demo.pbl6_android.data.api.model.StoreResponse

/**
 * Repository for Store API calls
 * Handles all store-related API requests
 */
object StoreApiRepository {

    private val apiService = RetrofitClient.apiService

    /**
     * Get all stores
     * GET /api/v1/stores
     */
    suspend fun getAllStores(): ApiResult<List<StoreResponse>> {
        return try {
            android.util.Log.d("StoreApiRepository", "🏪 Getting all stores...")

            val result = ApiHelper.safeApiCall {
                apiService.getAllStores()
            }

            when (result) {
                is ApiResult.Success -> {
                    android.util.Log.d("StoreApiRepository", "✅ Stores loaded: ${result.data.size} stores")
                    result
                }
                is ApiResult.Error -> {
                    android.util.Log.e("StoreApiRepository", "❌ Get stores error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("StoreApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi tải cửa hàng: ${e.message}",
                exception = e
            )
        }
    }

    /**
     * Get store by ID
     * GET /api/v1/stores/{storeId}
     */
    suspend fun getStoreById(storeId: String): ApiResult<StoreResponse> {
        return try {
            android.util.Log.d("StoreApiRepository", "🏪 Getting store: id=$storeId")

            val result = ApiHelper.safeApiCall {
                apiService.getStoreById(storeId)
            }

            when (result) {
                is ApiResult.Success -> {
                    android.util.Log.d("StoreApiRepository", "✅ Store loaded: ${result.data.name}")
                    result
                }
                is ApiResult.Error -> {
                    android.util.Log.e("StoreApiRepository", "❌ Get store error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("StoreApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi tải thông tin cửa hàng: ${e.message}",
                exception = e
            )
        }
    }

    /**
     * Get stores by owner
     * GET /api/v1/stores/owner/{ownerId}
     */
    suspend fun getStoresByOwner(
        ownerId: String,
        page: Int = 0,
        size: Int = 10,
        sortBy: String = "createdAt",
        sortDir: String = "desc"
    ): ApiResult<PageResponse<StoreResponse>> {
        return try {
            android.util.Log.d("StoreApiRepository", "🏪 Getting stores by owner: id=$ownerId, page=$page")

            val result = ApiHelper.safeApiCall {
                apiService.getStoresByOwner(ownerId, page, size, sortBy, sortDir)
            }

            when (result) {
                is ApiResult.Success -> {
                    val storeCount = result.data.content?.size ?: 0
                    android.util.Log.d("StoreApiRepository", "✅ Owner stores loaded: $storeCount stores")
                    result
                }
                is ApiResult.Error -> {
                    android.util.Log.e("StoreApiRepository", "❌ Get owner stores error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("StoreApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi tải cửa hàng của chủ: ${e.message}",
                exception = e
            )
        }
    }
}

