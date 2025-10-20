package com.demo.pbl6_android.data.model

data class Review(
    val id: String,
    val userName: String,
    val rating: Float,
    val comment: String,
    val images: List<Int>,
    val date: String,
    val helpfulCount: Int
)

