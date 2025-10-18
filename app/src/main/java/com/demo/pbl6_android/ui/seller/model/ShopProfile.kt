package com.demo.pbl6_android.ui.seller.model

data class ShopProfile(
    val id: String,
    val name: String,
    val url: String,
    val avatarUrl: String = "",
    val notificationCount: Int = 0
)

