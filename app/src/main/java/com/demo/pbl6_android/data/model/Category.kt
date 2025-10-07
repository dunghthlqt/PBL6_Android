package com.demo.pbl6_android.data.model

data class Category(
    val id: String,
    val name: String,
    val icon: String,
    val productCount: Int = 0,
    var isSelected: Boolean = false
)

