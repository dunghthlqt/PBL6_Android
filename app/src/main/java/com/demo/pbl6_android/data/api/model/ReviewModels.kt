package com.demo.pbl6_android.data.api.model

import com.google.gson.annotations.SerializedName

/**
 * Review DTO for creating/updating reviews
 * Based on: POST /api/v1/reviews, PUT /api/v1/reviews/{reviewId}
 */
data class ReviewDTO(
    @SerializedName("rating")
    val rating: Int, // 1-5 stars
    
    @SerializedName("comment")
    val comment: String,
    
    @SerializedName("orderId")
    val orderId: String,
    
    @SerializedName("productVariantId")
    val productVariantId: String
)

/**
 * Review response from API
 * Based on: GET /api/v1/reviews/{reviewId}
 */
data class ReviewResponse(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("orderId")
    val orderId: String,
    
    @SerializedName("productVariantId")
    val productVariantId: String,
    
    @SerializedName("rating")
    val rating: Int,
    
    @SerializedName("comment")
    val comment: String,
    
    @SerializedName("user")
    val user: UserResponse?,
    
    @SerializedName("createdAt")
    val createdAt: String?,
    
    @SerializedName("updatedAt")
    val updatedAt: String?
)

/**
 * User info in review response
 */
data class UserResponse(
    @SerializedName("id")
    val id: String?,
    
    @SerializedName("name")
    val name: String?,
    
    @SerializedName("email")
    val email: String?,
    
    @SerializedName("avatar")
    val avatar: String?
)

/**
 * Rating statistics for a product variant
 * Based on: GET /api/v1/reviews/product-variant/{productVariantId}/stats
 */
data class RatingStatsResponse(
    @SerializedName("averageRating")
    val averageRating: Double,
    
    @SerializedName("totalReviews")
    val totalReviews: Int,
    
    @SerializedName("ratingDistribution")
    val ratingDistribution: Map<String, Int>? // "5": 10, "4": 5, "3": 2, "2": 1, "1": 0
) {
    // Helper methods
    fun getRatingCount(stars: Int): Int {
        return ratingDistribution?.get(stars.toString()) ?: 0
    }
    
    fun getRatingPercentage(stars: Int): Float {
        if (totalReviews == 0) return 0f
        val count = getRatingCount(stars)
        return (count.toFloat() / totalReviews) * 100
    }
}

/**
 * Review summary for display
 */
data class ReviewSummary(
    val averageRating: Double,
    val totalReviews: Int,
    val fiveStars: Int,
    val fourStars: Int,
    val threeStars: Int,
    val twoStars: Int,
    val oneStar: Int
) {
    companion object {
        fun fromRatingStats(stats: RatingStatsResponse): ReviewSummary {
            return ReviewSummary(
                averageRating = stats.averageRating,
                totalReviews = stats.totalReviews,
                fiveStars = stats.getRatingCount(5),
                fourStars = stats.getRatingCount(4),
                threeStars = stats.getRatingCount(3),
                twoStars = stats.getRatingCount(2),
                oneStar = stats.getRatingCount(1)
            )
        }
    }
}

