package com.demo.pbl6_android.data.api.model

import com.google.gson.annotations.SerializedName

/**
 * Base API response wrapper
 * All API responses follow this structure based on the API documentation
 */
data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("data")
    val data: T?,
    
    @SerializedName("error")
    val error: String?
)

/**
 * Paginated response wrapper
 * Used for endpoints that return paginated data
 */
data class PageResponse<T>(
    @SerializedName("content")
    val content: List<T>,
    
    @SerializedName("totalElements")
    val totalElements: Long,
    
    @SerializedName("totalPages")
    val totalPages: Int,
    
    @SerializedName("numberOfElements")
    val numberOfElements: Int,
    
    @SerializedName("size")
    val size: Int,
    
    @SerializedName("number")
    val number: Int,
    
    @SerializedName("first")
    val isFirst: Boolean,
    
    @SerializedName("last")
    val isLast: Boolean,
    
    @SerializedName("empty")
    val isEmpty: Boolean,
    
    @SerializedName("pageable")
    val pageable: Pageable?,
    
    @SerializedName("sort")
    val sort: Sort?
)

/**
 * Pageable information
 */
data class Pageable(
    @SerializedName("pageNumber")
    val pageNumber: Int,
    
    @SerializedName("pageSize")
    val pageSize: Int,
    
    @SerializedName("offset")
    val offset: Long,
    
    @SerializedName("paged")
    val isPaged: Boolean,
    
    @SerializedName("unpaged")
    val isUnpaged: Boolean,
    
    @SerializedName("sort")
    val sort: Sort?
)

/**
 * Sort information
 */
data class Sort(
    @SerializedName("sorted")
    val isSorted: Boolean,
    
    @SerializedName("unsorted")
    val isUnsorted: Boolean,
    
    @SerializedName("empty")
    val isEmpty: Boolean
)

/**
 * Error response details
 */
data class ErrorResponse(
    @SerializedName("success")
    val success: Boolean = false,
    
    @SerializedName("error")
    val error: String?,
    
    @SerializedName("message")
    val message: String?,
    
    @SerializedName("timestamp")
    val timestamp: String?,
    
    @SerializedName("status")
    val status: Int?
)

