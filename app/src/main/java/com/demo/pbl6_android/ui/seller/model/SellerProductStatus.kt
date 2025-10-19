package com.demo.pbl6_android.ui.seller.model

enum class SellerProductStatus(val displayName: String) {
    IN_STOCK("Còn hàng"),
    OUT_OF_STOCK("Hết hàng"),
    PENDING_APPROVAL("Chờ duyệt"),
    VIOLATION("Vi phạm"),
    HIDDEN("Đã ẩn")
}

