package com.demo.pbl6_android.ui.cart.model

data class CartShop(
    val id: String,
    val name: String,
    val products: MutableList<CartProduct>,
    var isSelected: Boolean = false
)
