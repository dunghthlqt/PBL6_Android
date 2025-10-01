package com.demo.pbl6_android.ui.order.model

data class OrderShop(
    val shopId: String,
    val shopName: String,
    val products: List<OrderProduct>,
    var shopVoucher: String? = null,
    var noteToShop: String? = null
)

