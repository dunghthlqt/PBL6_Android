package com.demo.pbl6_android.data.model

data class User(
    val id: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val address: String,
    val birthday: String,
    val gender: String,
    val avatarUrl: String,
    val memberSince: String,
    val orderCount: Int,
    val loyaltyPoints: Int,
    val isSellerEnabled: Boolean = false,
    val shopName: String? = null,
    val shopDescription: String? = null,
    val shopRating: Float? = null,
    val totalProducts: Int = 0,
    val totalSales: Int = 0
)

data class UserSettings(
    var orderNotification: Boolean = true,
    var promotionNotification: Boolean = false,
    var newsNotification: Boolean = true,
    var showPhone: Boolean = true,
    var showEmail: Boolean = false
)

