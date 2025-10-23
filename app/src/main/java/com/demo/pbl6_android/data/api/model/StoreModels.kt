package com.demo.pbl6_android.data.api.model

import com.google.gson.annotations.SerializedName

/**
 * Store response from API
 * Based on: GET /api/v1/stores, GET /api/v1/stores/{storeId}
 */
data class StoreResponse(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("description")
    val description: String?,
    
    @SerializedName("address")
    val address: StoreAddressResponse?,
    
    @SerializedName("logoUrl")
    val logoUrl: String?,
    
    @SerializedName("bannerUrl")
    val bannerUrl: String?,
    
    @SerializedName("status")
    val status: String, // PENDING, APPROVED, REJECTED, DELETED
    
    @SerializedName("owner")
    val owner: StoreOwnerResponse?,
    
    @SerializedName("createdAt")
    val createdAt: String?,
    
    @SerializedName("updatedAt")
    val updatedAt: String?
)

/**
 * Store address information
 */
data class StoreAddressResponse(
    @SerializedName("province")
    val province: String?,
    
    @SerializedName("ward")
    val ward: String?,
    
    @SerializedName("homeAddress")
    val homeAddress: String?
) {
    fun getFullAddress(): String {
        return buildString {
            homeAddress?.let { append(it) }
            if (isNotEmpty() && ward != null) append(", ")
            ward?.let { append(it) }
            if (isNotEmpty() && province != null) append(", ")
            province?.let { append(it) }
        }
    }
}

/**
 * Store owner information
 */
data class StoreOwnerResponse(
    @SerializedName("id")
    val id: String?,
    
    @SerializedName("fullName")
    val fullName: String?,
    
    @SerializedName("email")
    val email: String?,
    
    @SerializedName("phone")
    val phone: String?
)

/**
 * Store status enum
 */
enum class StoreStatus(val apiValue: String) {
    PENDING("PENDING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    DELETED("DELETED");
    
    companion object {
        fun fromApiValue(value: String): StoreStatus {
            return values().firstOrNull { it.apiValue == value } ?: PENDING
        }
    }
}

