package com.demo.pbl6_android.ui.cart.model

data class CartProduct(
    val id: String,
    val name: String,
    val color: String,
    val size: String,
    val currentPrice: Int,
    val originalPrice: Int,
    var quantity: Int,
    val imageUrl: String,
    var isSelected: Boolean = false
)
