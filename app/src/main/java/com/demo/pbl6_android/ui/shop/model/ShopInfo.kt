package com.demo.pbl6_android.ui.shop.model

data class ShopInfo(
    val id: String,
    val name: String,
    val rating: Float,
    val followers: Int,
    val isFollowing: Boolean,
    val avatarUrl: String,
    val description: String? = null,
    val totalProducts: Int = 0,
    val responseRate: Int = 0,
    val responseTime: String? = null
)

