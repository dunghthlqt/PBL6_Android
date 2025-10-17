package com.demo.pbl6_android.ui.shop.model

data class ShopVoucher(
    val id: String,
    val discount: String,
    val minAmount: String,
    val expiryDate: String,
    val quantity: Int,
    val isSaved: Boolean = false
)

