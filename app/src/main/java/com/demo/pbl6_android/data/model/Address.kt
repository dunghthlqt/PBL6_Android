package com.demo.pbl6_android.data.model

data class Address(
    val id: String,
    val recipientName: String,
    val phoneNumber: String,
    val province: String,
    val district: String,
    val ward: String,
    val detailAddress: String,
    val isDefault: Boolean = false
) {
    val fullAddress: String
        get() = "$detailAddress, $ward, $district, $province"
}

