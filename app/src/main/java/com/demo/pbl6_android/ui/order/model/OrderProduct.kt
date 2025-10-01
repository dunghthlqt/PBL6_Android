package com.demo.pbl6_android.ui.order.model

data class OrderProduct(
    val id: String,
    val name: String,
    val color: String,
    val size: String,
    val currentPrice: Int,
    val originalPrice: Int,
    val quantity: Int,
    val imageUrl: String
)

