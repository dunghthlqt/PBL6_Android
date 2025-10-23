package com.demo.pbl6_android.data.api.model

import com.google.gson.annotations.SerializedName

/**
 * Category response from API
 */
data class CategoryResponse(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("description")
    val description: String?
)

/**
 * Brand response from API
 */
data class BrandResponse(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("name")
    val name: String
)

