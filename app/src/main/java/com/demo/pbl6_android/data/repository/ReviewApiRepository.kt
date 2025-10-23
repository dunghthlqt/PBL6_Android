package com.demo.pbl6_android.data.repository

import com.demo.pbl6_android.data.api.ApiHelper
import com.demo.pbl6_android.data.api.ApiResult
import com.demo.pbl6_android.data.api.RetrofitClient
import com.demo.pbl6_android.data.api.model.PageResponse
import com.demo.pbl6_android.data.api.model.RatingStatsResponse
import com.demo.pbl6_android.data.api.model.ReviewDTO
import com.demo.pbl6_android.data.api.model.ReviewResponse

/**
 * Repository for Review API calls
 * Handles all review-related API requests
 */
object ReviewApiRepository {

    private val apiService = RetrofitClient.apiService

    /**
     * Create product review
     * POST /api/v1/reviews
     */
    suspend fun createReview(reviewDTO: ReviewDTO): ApiResult<ReviewResponse> {
        return try {
            android.util.Log.d("ReviewApiRepository", "⭐ Creating review: rating=${reviewDTO.rating}, productVariantId=${reviewDTO.productVariantId}")

            val result = ApiHelper.safeApiCall {
                apiService.createReview(reviewDTO)
            }

            when (result) {
                is ApiResult.Success -> {
                    android.util.Log.d("ReviewApiRepository", "✅ Review created: ${result.data.id}")
                    result
                }
                is ApiResult.Error -> {
                    android.util.Log.e("ReviewApiRepository", "❌ Create review error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("ReviewApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi tạo đánh giá: ${e.message}",
                exception = e
            )
        }
    }

    /**
     * Get review by ID
     * GET /api/v1/reviews/{reviewId}
     */
    suspend fun getReviewById(reviewId: String): ApiResult<ReviewResponse> {
        return try {
            android.util.Log.d("ReviewApiRepository", "🔍 Getting review: id=$reviewId")

            val result = ApiHelper.safeApiCall {
                apiService.getReviewById(reviewId)
            }

            when (result) {
                is ApiResult.Success -> {
                    android.util.Log.d("ReviewApiRepository", "✅ Review loaded: ${result.data.id}")
                    result
                }
                is ApiResult.Error -> {
                    android.util.Log.e("ReviewApiRepository", "❌ Get review error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("ReviewApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi tải đánh giá: ${e.message}",
                exception = e
            )
        }
    }

    /**
     * Update review
     * PUT /api/v1/reviews/{reviewId}
     */
    suspend fun updateReview(reviewId: String, reviewDTO: ReviewDTO): ApiResult<ReviewResponse> {
        return try {
            android.util.Log.d("ReviewApiRepository", "✏️ Updating review: id=$reviewId")

            val result = ApiHelper.safeApiCall {
                apiService.updateReview(reviewId, reviewDTO)
            }

            when (result) {
                is ApiResult.Success -> {
                    android.util.Log.d("ReviewApiRepository", "✅ Review updated: ${result.data.id}")
                    result
                }
                is ApiResult.Error -> {
                    android.util.Log.e("ReviewApiRepository", "❌ Update review error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("ReviewApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi cập nhật đánh giá: ${e.message}",
                exception = e
            )
        }
    }

    /**
     * Delete review
     * DELETE /api/v1/reviews/{reviewId}
     */
    suspend fun deleteReview(reviewId: String): ApiResult<String> {
        return try {
            android.util.Log.d("ReviewApiRepository", "🗑️ Deleting review: id=$reviewId")

            val result = ApiHelper.safeApiCall {
                apiService.deleteReview(reviewId)
            }

            when (result) {
                is ApiResult.Success -> {
                    android.util.Log.d("ReviewApiRepository", "✅ Review deleted")
                    result
                }
                is ApiResult.Error -> {
                    android.util.Log.e("ReviewApiRepository", "❌ Delete review error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("ReviewApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi xóa đánh giá: ${e.message}",
                exception = e
            )
        }
    }

    /**
     * Get reviews by product (paginated)
     * GET /api/v1/reviews/product/{productId}
     */
    suspend fun getReviewsByProduct(
        productId: String,
        page: Int = 0,
        size: Int = 10,
        sortBy: String = "createdAt",
        sortDir: String = "desc"
    ): ApiResult<PageResponse<ReviewResponse>> {
        return try {
            android.util.Log.d("ReviewApiRepository", "📜 Getting reviews for product: id=$productId, page=$page")

            val result = ApiHelper.safeApiCall {
                apiService.getReviewsByProduct(productId, page, size, sortBy, sortDir)
            }

            when (result) {
                is ApiResult.Success -> {
                    val reviewCount = result.data.content?.size ?: 0
                    android.util.Log.d("ReviewApiRepository", "✅ Reviews loaded: $reviewCount reviews")
                    result
                }
                is ApiResult.Error -> {
                    android.util.Log.e("ReviewApiRepository", "❌ Get reviews error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("ReviewApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi tải đánh giá: ${e.message}",
                exception = e
            )
        }
    }

    /**
     * Get reviews by product variant
     * GET /api/v1/reviews/product-variant/{productVariantId}
     */
    suspend fun getReviewsByProductVariant(productVariantId: String): ApiResult<List<ReviewResponse>> {
        return try {
            android.util.Log.d("ReviewApiRepository", "📜 Getting reviews for variant: id=$productVariantId")

            val result = ApiHelper.safeApiCall {
                apiService.getReviewsByProductVariant(productVariantId)
            }

            when (result) {
                is ApiResult.Success -> {
                    android.util.Log.d("ReviewApiRepository", "✅ Reviews loaded: ${result.data.size} reviews")
                    result
                }
                is ApiResult.Error -> {
                    android.util.Log.e("ReviewApiRepository", "❌ Get reviews error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("ReviewApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi tải đánh giá: ${e.message}",
                exception = e
            )
        }
    }

    /**
     * Get product rating statistics
     * GET /api/v1/reviews/product-variant/{productVariantId}/stats
     */
    suspend fun getProductRatingStats(productVariantId: String): ApiResult<RatingStatsResponse> {
        return try {
            android.util.Log.d("ReviewApiRepository", "📊 Getting rating stats for variant: id=$productVariantId")

            val result = ApiHelper.safeApiCall {
                apiService.getProductRatingStats(productVariantId)
            }

            when (result) {
                is ApiResult.Success -> {
                    android.util.Log.d("ReviewApiRepository", "✅ Stats loaded: avg=${result.data.averageRating}, total=${result.data.totalReviews}")
                    result
                }
                is ApiResult.Error -> {
                    android.util.Log.e("ReviewApiRepository", "❌ Get stats error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("ReviewApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi tải thống kê đánh giá: ${e.message}",
                exception = e
            )
        }
    }

    /**
     * Get my reviews
     * GET /api/v1/reviews/my-reviews
     */
    suspend fun getMyReviews(): ApiResult<List<ReviewResponse>> {
        return try {
            android.util.Log.d("ReviewApiRepository", "📜 Getting my reviews")

            val result = ApiHelper.safeApiCall {
                apiService.getMyReviews()
            }

            when (result) {
                is ApiResult.Success -> {
                    android.util.Log.d("ReviewApiRepository", "✅ My reviews loaded: ${result.data.size} reviews")
                    result
                }
                is ApiResult.Error -> {
                    android.util.Log.e("ReviewApiRepository", "❌ Get my reviews error: ${result.message}")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("ReviewApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi tải đánh giá của bạn: ${e.message}",
                exception = e
            )
        }
    }
}

