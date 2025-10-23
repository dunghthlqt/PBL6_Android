package com.demo.pbl6_android.data.api

import com.demo.pbl6_android.data.api.model.ApiResponse
import com.demo.pbl6_android.data.api.model.ErrorResponse
import com.google.gson.Gson
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Helper object to handle API calls and convert to ApiResult
 */
object ApiHelper {
    
    /**
     * Safe API call wrapper that converts Response to ApiResult
     */
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<ApiResponse<T>>): ApiResult<T> {
        return try {
            android.util.Log.d("ApiHelper", "📡 Making API call...")
            val response = apiCall()
            android.util.Log.d("ApiHelper", "📥 Response code: ${response.code()}")
            handleApiResponse(response)
        } catch (e: UnknownHostException) {
            android.util.Log.e("ApiHelper", "🌐 No internet connection", e)
            ApiResult.Error(
                message = "Không có kết nối internet. Vui lòng kiểm tra kết nối của bạn.",
                exception = e
            )
        } catch (e: SocketTimeoutException) {
            android.util.Log.e("ApiHelper", "⏱️ Timeout", e)
            ApiResult.Error(
                message = "Timeout - Server phản hồi quá lâu. Vui lòng thử lại.",
                exception = e
            )
        } catch (e: IOException) {
            android.util.Log.e("ApiHelper", "🔌 IO Error", e)
            ApiResult.Error(
                message = "Lỗi kết nối: ${e.message ?: "Unknown error"}",
                exception = e
            )
        } catch (e: Exception) {
            android.util.Log.e("ApiHelper", "💥 Unexpected error", e)
            ApiResult.Error(
                message = "Lỗi không xác định: ${e.message ?: "Unknown error"}",
                exception = e
            )
        }
    }
    
    /**
     * Handle API response and convert to ApiResult
     */
    private fun <T> handleApiResponse(response: Response<ApiResponse<T>>): ApiResult<T> {
        return try {
            if (response.isSuccessful) {
                val body = response.body()
                android.util.Log.d("ApiHelper", "📦 Response body: success=${body?.success}, hasData=${body?.data != null}")
                
                if (body != null && body.success && body.data != null) {
                    android.util.Log.d("ApiHelper", "✅ Success!")
                    ApiResult.Success(body.data)
                } else {
                    val errorMessage = body?.error ?: "Không có dữ liệu"
                    android.util.Log.e("ApiHelper", "❌ API returned success=false or null data: $errorMessage")
                    ApiResult.Error(
                        message = errorMessage,
                        code = response.code()
                    )
                }
            } else {
                // Parse error body
                val errorBody = response.errorBody()?.string()
                android.util.Log.e("ApiHelper", "❌ HTTP Error ${response.code()}: $errorBody")
                
                val errorMessage = if (errorBody != null) {
                    try {
                        val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                        errorResponse.error ?: errorResponse.message ?: getErrorMessageByCode(response.code())
                    } catch (e: Exception) {
                        getErrorMessageByCode(response.code())
                    }
                } else {
                    getErrorMessageByCode(response.code())
                }
                
                ApiResult.Error(
                    message = errorMessage,
                    code = response.code()
                )
            }
        } catch (e: Exception) {
            android.util.Log.e("ApiHelper", "💥 Error processing response", e)
            ApiResult.Error(
                message = "Lỗi xử lý dữ liệu: ${e.message}",
                exception = e,
                code = response.code()
            )
        }
    }
    
    /**
     * Get error message by HTTP status code
     */
    private fun getErrorMessageByCode(code: Int): String {
        return when (code) {
            400 -> "Yêu cầu không hợp lệ"
            401 -> "Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại"
            403 -> "Bạn không có quyền truy cập"
            404 -> "Không tìm thấy dữ liệu"
            408 -> "Timeout - Yêu cầu mất quá nhiều thời gian"
            500 -> "Lỗi server. Vui lòng thử lại sau"
            502 -> "Server không phản hồi"
            503 -> "Dịch vụ tạm thời không khả dụng"
            else -> "Lỗi: HTTP $code"
        }
    }
    
    /**
     * Safe API call for non-wrapped responses (direct data)
     */
    suspend fun <T> safeApiCallDirect(apiCall: suspend () -> Response<T>): ApiResult<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    ApiResult.Success(body)
                } else {
                    ApiResult.Error(
                        message = "Không có dữ liệu",
                        code = response.code()
                    )
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    try {
                        val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                        errorResponse.error ?: errorResponse.message ?: getErrorMessageByCode(response.code())
                    } catch (e: Exception) {
                        getErrorMessageByCode(response.code())
                    }
                } else {
                    getErrorMessageByCode(response.code())
                }
                
                ApiResult.Error(
                    message = errorMessage,
                    code = response.code()
                )
            }
        } catch (e: UnknownHostException) {
            ApiResult.Error(
                message = "Không có kết nối internet. Vui lòng kiểm tra kết nối của bạn.",
                exception = e
            )
        } catch (e: SocketTimeoutException) {
            ApiResult.Error(
                message = "Timeout - Server phản hồi quá lâu. Vui lòng thử lại.",
                exception = e
            )
        } catch (e: IOException) {
            ApiResult.Error(
                message = "Lỗi kết nối: ${e.message ?: "Unknown error"}",
                exception = e
            )
        } catch (e: Exception) {
            ApiResult.Error(
                message = "Lỗi không xác định: ${e.message ?: "Unknown error"}",
                exception = e
            )
        }
    }
}

