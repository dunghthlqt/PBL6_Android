package com.demo.pbl6_android.data.api.model

import com.google.gson.annotations.SerializedName

/**
 * Promotion/Voucher response from API
 * Based on: GET /api/v1/b2c/promotions/active, GET /api/v1/b2c/promotions/active/store/{storeId}
 */
data class PromotionResponse(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("storeId")
    val storeId: String?,
    
    @SerializedName("type")
    val type: String, // PERCENTAGE, FIXED_AMOUNT, BUY_X_GET_Y, etc.
    
    @SerializedName("discountType")
    val discountType: String, // PERCENTAGE, FIXED_AMOUNT
    
    @SerializedName("discountValue")
    val discountValue: Long?,
    
    @SerializedName("startDate")
    val startDate: String, // ISO 8601 format
    
    @SerializedName("endDate")
    val endDate: String, // ISO 8601 format
    
    @SerializedName("minOrderValue")
    val minOrderValue: Long?,
    
    @SerializedName("maxDiscountValue")
    val maxDiscountValue: Long?,
    
    @SerializedName("status")
    val status: String // ACTIVE, INACTIVE, EXPIRED
)

/**
 * Promotion validation response
 * Based on: POST /api/v1/b2c/promotions/{promotionId}/validate
 */
data class PromotionValidationResponse(
    @SerializedName("valid")
    val valid: Boolean,
    
    @SerializedName("message")
    val message: String?,
    
    @SerializedName("discountAmount")
    val discountAmount: Long?
)

/**
 * Discount calculation response
 * Based on: POST /api/v1/b2c/promotions/{promotionId}/calculate-discount
 */
data class DiscountCalculationResponse(
    @SerializedName("discountAmount")
    val discountAmount: Long,
    
    @SerializedName("finalAmount")
    val finalAmount: Long,
    
    @SerializedName("promotionApplied")
    val promotionApplied: Boolean
)

/**
 * Promotion type enum
 */
enum class PromotionType(val apiValue: String, val displayName: String) {
    PERCENTAGE("PERCENTAGE", "Giảm theo phần trăm"),
    FIXED_AMOUNT("FIXED_AMOUNT", "Giảm giá cố định"),
    BUY_X_GET_Y("BUY_X_GET_Y", "Mua X tặng Y"),
    FREE_SHIPPING("FREE_SHIPPING", "Miễn phí vận chuyển");
    
    companion object {
        fun fromApiValue(value: String): PromotionType {
            return values().firstOrNull { it.apiValue == value } ?: PERCENTAGE
        }
    }
}

/**
 * Discount type enum
 */
enum class DiscountType(val apiValue: String) {
    PERCENTAGE("PERCENTAGE"),
    FIXED_AMOUNT("FIXED_AMOUNT");
    
    companion object {
        fun fromApiValue(value: String): DiscountType {
            return values().firstOrNull { it.apiValue == value } ?: PERCENTAGE
        }
    }
}

/**
 * Promotion status enum
 */
enum class PromotionStatus(val apiValue: String, val displayName: String) {
    ACTIVE("ACTIVE", "Đang hoạt động"),
    INACTIVE("INACTIVE", "Không hoạt động"),
    EXPIRED("EXPIRED", "Đã hết hạn");
    
    companion object {
        fun fromApiValue(value: String): PromotionStatus {
            return values().firstOrNull { it.apiValue == value } ?: INACTIVE
        }
    }
}

/**
 * Helper extension functions
 */
fun PromotionResponse.getDiscountText(): String {
    return when (DiscountType.fromApiValue(discountType)) {
        DiscountType.PERCENTAGE -> {
            "${discountValue}% OFF"
        }
        DiscountType.FIXED_AMOUNT -> {
            "Giảm ${formatCurrency(discountValue ?: 0)}"
        }
    }
}

fun PromotionResponse.getMinOrderText(): String? {
    return minOrderValue?.let { "Đơn tối thiểu: ${formatCurrency(it)}" }
}

fun PromotionResponse.getMaxDiscountText(): String? {
    return maxDiscountValue?.let { "Giảm tối đa: ${formatCurrency(it)}" }
}

private fun formatCurrency(amount: Long): String {
    return "%,dđ".format(amount).replace(",", ".")
}

