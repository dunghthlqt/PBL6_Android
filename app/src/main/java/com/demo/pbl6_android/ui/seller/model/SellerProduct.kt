package com.demo.pbl6_android.ui.seller.model

data class SellerProduct(
    val id: String,
    val name: String,
    val price: Long,
    val imageUrl: String,
    val stockQuantity: Int,
    val soldCount: Int,
    val likeCount: Int,
    val viewCount: Int,
    val status: SellerProductStatus
)

