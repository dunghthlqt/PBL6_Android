package com.demo.pbl6_android.ui.seller.model

data class SellerTask(
    val id: String,
    val title: String,
    val reward: String,
    val currentProgress: Int,
    val maxProgress: Int,
    val isCompleted: Boolean = false
)

