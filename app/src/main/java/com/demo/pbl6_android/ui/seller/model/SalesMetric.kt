package com.demo.pbl6_android.ui.seller.model

data class SalesMetric(
    val label: String,
    val value: String,
    val changePercent: String,
    val hasInfoIcon: Boolean = false
)

