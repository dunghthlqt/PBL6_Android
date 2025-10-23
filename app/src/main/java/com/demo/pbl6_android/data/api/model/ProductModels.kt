package com.demo.pbl6_android.data.api.model

import com.google.gson.annotations.SerializedName

/**
 * Store info nested in Product response
 */
data class StoreInfo(
    @SerializedName("id")
    val id: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("logo")
    val logo: String?
)

/**
 * Product response from API
 * Based on API documentation: /api/v1/products/ *
 */
data class ProductResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("category")
    val category: String?,

    @SerializedName("brand")
    val brand: String?,

    @SerializedName("price")
    val price: Long?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("store")
    val store: StoreInfo?,

    @SerializedName("createdAt")
    val createdAt: String?,

    @SerializedName("updatedAt")
    val updatedAt: String?
)

/**
 * Product Variant response from API
 * Based on API documentation: /api/v1/product-variants/ *
 */
data class ProductVariantResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("price")
    val price: Long,

    @SerializedName("stock")
    val stock: Int,

    @SerializedName("attributes")
    val attributes: Map<String, String>?,

    @SerializedName("productId")
    val productId: String,

    @SerializedName("product")
    val product: ProductResponse?,

    @SerializedName("colors")
    val colors: List<ColorOptionResponse>?,

    @SerializedName("images")
    val images: List<String>?,

    @SerializedName("averageRating")
    val averageRating: Double?,

    @SerializedName("totalReviews")
    val totalReviews: Int?,

    @SerializedName("createdAt")
    val createdAt: String?,

    @SerializedName("updatedAt")
    val updatedAt: String?,

    @SerializedName("status")
    val status: String?
)

/**
 * Color option for product variant
 */
data class ColorOptionResponse(
    @SerializedName("id")
    val id: String?,

    @SerializedName("colorName")
    val colorName: String,

    @SerializedName("colorCode")
    val colorCode: String?,

    @SerializedName("price")
    val price: Long?,

    @SerializedName("stock")
    val stock: Int?,

    @SerializedName("imageUrl")
    val imageUrl: String?
)