package com.demo.pbl6_android.data.model

data class Voucher(
    val id: String,
    val code: String,
    val title: String,
    val discount: String,
    val minAmount: Int,
    val expiryDate: String,
    val type: VoucherType,
    val maxDiscount: Int? = null
)

enum class VoucherType {
    SHOP,
    PLATFORM
}

