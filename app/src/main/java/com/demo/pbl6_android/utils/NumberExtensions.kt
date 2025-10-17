package com.demo.pbl6_android.utils

fun Int.formatFollowers(): String {
    return when {
        this >= 1000000 -> String.format("%.1ftr", this / 1000000.0)
        this >= 1000 -> String.format("%.1fk", this / 1000.0)
        else -> this.toString()
    } + " Người theo dõi"
}

fun Int.formatPrice(): String {
    return String.format("%,d", this).replace(",", ".") + "đ"
}

fun Double.formatPrice(): String {
    return String.format("%,.0f", this).replace(",", ".") + "đ"
}

fun Int.formatCompactNumber(): String {
    return when {
        this >= 1000000 -> String.format("%.1ftr", this / 1000000.0)
        this >= 1000 -> String.format("%.1fk", this / 1000.0)
        else -> this.toString()
    }
}

