package com.demo.pbl6_android.ui.order.model

data class ShippingMethod(
    val id: String,
    val name: String,
    val estimatedDays: String,
    val price: Int,
    var isSelected: Boolean = false
)

