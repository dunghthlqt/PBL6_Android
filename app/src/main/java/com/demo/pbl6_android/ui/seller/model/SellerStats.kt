package com.demo.pbl6_android.ui.seller.model

data class SellerStats(
    val pendingOrders: Int = 0,
    val cancelledOrders: Int = 0,
    val returnOrders: Int = 0,
    val reviewsToRespond: Int = 0
)

