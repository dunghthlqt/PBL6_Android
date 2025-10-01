package com.demo.pbl6_android.ui.order.model

data class ShippingAddress(
    val recipientName: String,
    val phoneNumber: String,
    val fullAddress: String,
    val isDefault: Boolean = false
)

