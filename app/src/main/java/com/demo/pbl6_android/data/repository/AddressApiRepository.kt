package com.demo.pbl6_android.data.repository

import com.demo.pbl6_android.data.api.ApiHelper
import com.demo.pbl6_android.data.api.ApiResult
import com.demo.pbl6_android.data.api.RetrofitClient
import com.demo.pbl6_android.data.api.model.AddressCheckResponse
import com.demo.pbl6_android.data.api.model.AddressDTO
import com.demo.pbl6_android.data.api.model.AddressResponse

/**
 * Repository for Address API calls
 * Handles all address-related API requests
 */
object AddressApiRepository {
    
    private val apiService = RetrofitClient.apiService
    
    /**
     * Get user address
     * GET /api/v1/buyer/address
     */
    suspend fun getUserAddress(): ApiResult<AddressResponse> {
        return try {
            android.util.Log.d("AddressApiRepository", "📍 Getting user address...")
            
            val result = ApiHelper.safeApiCall {
                apiService.getUserAddress()
            }
            
            when (result) {
                is ApiResult.Success -> {
                    android.util.Log.d("AddressApiRepository", "✅ Address loaded: ${result.data.province}, ${result.data.ward}")
                    result
                }
                is ApiResult.Error -> {
                    android.util.Log.e("AddressApiRepository", "❌ Get address error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("AddressApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi lấy địa chỉ: ${e.message}",
                exception = e
            )
        }
    }
    
    /**
     * Check if user has address
     * GET /api/v1/buyer/address/check
     */
    suspend fun checkHasAddress(): ApiResult<AddressCheckResponse> {
        return try {
            android.util.Log.d("AddressApiRepository", "🔍 Checking if user has address...")
            
            val result = ApiHelper.safeApiCall {
                apiService.checkHasAddress()
            }
            
            when (result) {
                is ApiResult.Success -> {
                    android.util.Log.d("AddressApiRepository", "✅ Check result: hasAddress=${result.data.hasAddress}")
                    result
                }
                is ApiResult.Error -> {
                    android.util.Log.e("AddressApiRepository", "❌ Check address error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("AddressApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi kiểm tra địa chỉ: ${e.message}",
                exception = e
            )
        }
    }
    
    /**
     * Create or update address
     * POST /api/v1/buyer/address
     */
    suspend fun createOrUpdateAddress(address: AddressDTO): ApiResult<AddressResponse> {
        return try {
            android.util.Log.d("AddressApiRepository", "💾 Saving address: ${address.province}, ${address.ward}")
            
            val result = ApiHelper.safeApiCall {
                apiService.createOrUpdateAddress(address)
            }
            
            when (result) {
                is ApiResult.Success -> {
                    android.util.Log.d("AddressApiRepository", "✅ Address saved successfully")
                    result
                }
                is ApiResult.Error -> {
                    android.util.Log.e("AddressApiRepository", "❌ Save address error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("AddressApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi lưu địa chỉ: ${e.message}",
                exception = e
            )
        }
    }
    
    /**
     * Delete address
     * DELETE /api/v1/buyer/address
     */
    suspend fun deleteAddress(): ApiResult<String> {
        return try {
            android.util.Log.d("AddressApiRepository", "🗑️ Deleting address...")
            
            val result = ApiHelper.safeApiCall {
                apiService.deleteAddress()
            }
            
            when (result) {
                is ApiResult.Success -> {
                    android.util.Log.d("AddressApiRepository", "✅ Address deleted successfully")
                    result
                }
                is ApiResult.Error -> {
                    android.util.Log.e("AddressApiRepository", "❌ Delete address error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("AddressApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi xóa địa chỉ: ${e.message}",
                exception = e
            )
        }
    }
}

