package com.demo.pbl6_android.data.api.model

import com.google.gson.annotations.SerializedName

/**
 * Address DTO for create/update requests
 * Based on: POST /api/v1/buyer/address
 */
data class AddressDTO(
    @SerializedName("province")
    val province: String,
    
    @SerializedName("ward")
    val ward: String,
    
    @SerializedName("homeAddress")
    val homeAddress: String,
    
    @SerializedName("suggestedName")
    val suggestedName: String? = null
)

/**
 * Address response from API
 * Based on: GET /api/v1/buyer/address
 */
data class AddressResponse(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("userId")
    val userId: String,
    
    @SerializedName("province")
    val province: String,
    
    @SerializedName("ward")
    val ward: String,
    
    @SerializedName("homeAddress")
    val homeAddress: String,
    
    @SerializedName("suggestedName")
    val suggestedName: String?,
    
    @SerializedName("createdAt")
    val createdAt: String?,
    
    @SerializedName("updatedAt")
    val updatedAt: String?
)

/**
 * Response for address check
 * Based on: GET /api/v1/buyer/address/check
 */
data class AddressCheckResponse(
    @SerializedName("hasAddress")
    val hasAddress: Boolean
)

