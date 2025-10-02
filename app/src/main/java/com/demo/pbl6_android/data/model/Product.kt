package com.demo.pbl6_android.data.model

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val brand: String,
    val material: String,
    val origin: String,
    val sizes: List<String>,
    val colors: List<ProductColor>,
    val currentPrice: Int,
    val originalPrice: Int,
    val discount: Int,
    val rating: Float,
    val reviewCount: Int,
    val soldCount: Int,
    val images: List<String>,
    val category: String,
    val shopId: String,
    val shopName: String,
    val specifications: Map<String, String> = emptyMap()
)

data class ProductColor(
    val name: String,
    val hexCode: String,
    val isAvailable: Boolean = true
)

data class ProductReview(
    val id: String,
    val userId: String,
    val userName: String,
    val userAvatar: String,
    val rating: Int,
    val comment: String,
    val images: List<String>,
    val createdAt: Long
)

data class RatingBreakdown(
    val fiveStar: Int,
    val fourStar: Int,
    val threeStar: Int,
    val twoStar: Int,
    val oneStar: Int
) {
    val total: Int
        get() = fiveStar + fourStar + threeStar + twoStar + oneStar
}

