package com.demo.pbl6_android.data.model

enum class CardType(val displayName: String) {
    VISA("Visa"),
    MASTERCARD("Mastercard"),
    JCB("JCB"),
    MOMO("MoMo"),
    ZALOPAY("ZaloPay"),
    CASH_ON_DELIVERY("Thanh toán khi nhận hàng")
}

data class PaymentMethod(
    val id: String,
    val cardType: CardType,
    val cardNumber: String,
    val cardHolderName: String,
    val expiryDate: String,
    val isDefault: Boolean = false
) {
    val maskedCardNumber: String
        get() = "**** **** **** ${cardNumber.takeLast(4)}"
}

