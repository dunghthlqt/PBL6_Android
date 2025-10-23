package com.demo.pbl6_android.data.repository

import com.demo.pbl6_android.data.api.ApiHelper
import com.demo.pbl6_android.data.api.ApiResult
import com.demo.pbl6_android.data.api.RetrofitClient
import com.demo.pbl6_android.data.api.model.ProductResponse
import com.demo.pbl6_android.data.api.model.ProductVariantResponse
import com.demo.pbl6_android.data.model.Product
import com.demo.pbl6_android.data.model.ProductColor

/**
 * Repository for Product API calls
 * Handles all product-related API requests
 */
object ProductApiRepository {
    
    private val apiService = RetrofitClient.apiService
    
    /**
     * Get latest product variants
     * This will be used for landing page to show latest products
     */
    suspend fun getLatestProducts(
        page: Int = 0,
        size: Int = 20
    ): ApiResult<List<Product>> {
        return try {
            android.util.Log.d("ProductApiRepository", "🌐 Calling API: getLatestProductVariants(page=$page, size=$size)")
            
            val result = ApiHelper.safeApiCall {
                apiService.getLatestProductVariants(page = page, size = size)
            }
            
            when (result) {
                is ApiResult.Success -> {
                    android.util.Log.d("ProductApiRepository", "✅ API Response: ${result.data.content.size} variants received")
                    val products = result.data.content.mapNotNull { it.toProduct() }
                    android.util.Log.d("ProductApiRepository", "✅ Converted: ${products.size} products")
                    ApiResult.Success(products)
                }
                is ApiResult.Error -> {
                    android.util.Log.e("ProductApiRepository", "❌ API Error: ${result.message} (code: ${result.code})")
                    result
                }
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            android.util.Log.e("ProductApiRepository", "💥 Exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi tải danh sách sản phẩm: ${e.message}",
                exception = e
            )
        }
    }
    
    /**
     * Search products by name
     * Note: We convert ProductResponse directly to avoid server errors when fetching variants
     */
    suspend fun searchProducts(
        query: String,
        page: Int = 0,
        size: Int = 20
    ): ApiResult<List<Product>> {
        return try {
            android.util.Log.d("ProductApiRepository", "🔍 Searching products: query=$query")
            
            val productsResult = ApiHelper.safeApiCall {
                apiService.searchProducts(name = query, page = page, size = size)
            }
            
            when (productsResult) {
                is ApiResult.Success -> {
                    android.util.Log.d("ProductApiRepository", "✅ Found ${productsResult.data.content.size} products")
                    
                    // Convert ProductResponse directly to Product
                    // Don't fetch variants to avoid server-side conversion errors
                    val products = productsResult.data.content.mapNotNull { it.toProduct() }
                    
                    android.util.Log.d("ProductApiRepository", "✅ Converted ${products.size} products")
                    ApiResult.Success(products)
                }
                is ApiResult.Error -> {
                    android.util.Log.e("ProductApiRepository", "❌ Search error: ${productsResult.message}")
                    productsResult
                }
                is ApiResult.Loading -> productsResult
            }
        } catch (e: Exception) {
            android.util.Log.e("ProductApiRepository", "💥 Search exception: ${e.message}", e)
            ApiResult.Error(
                message = "Lỗi khi tìm kiếm sản phẩm: ${e.message}",
                exception = e
            )
        }
    }
    
    /**
     * Get products by category
     */
    suspend fun getProductsByCategory(
        category: String,
        page: Int = 0,
        size: Int = 20
    ): ApiResult<List<Product>> {
        return try {
            val result = ApiHelper.safeApiCall {
                apiService.getProductVariantsByCategory(category = category, page = page, size = size)
            }
            
            when (result) {
                is ApiResult.Success -> {
                    val products = result.data.content.mapNotNull { it.toProduct() }
                    ApiResult.Success(products)
                }
                is ApiResult.Error -> result
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            ApiResult.Error(
                message = "Lỗi khi tải sản phẩm theo danh mục: ${e.message}",
                exception = e
            )
        }
    }
    
    /**
     * Get product by ID (actually gets product variant by ID)
     */
    suspend fun getProductById(productId: String): ApiResult<Product> {
        return try {
            val result = ApiHelper.safeApiCall {
                apiService.getProductVariantById(productId)
            }
            
            when (result) {
                is ApiResult.Success -> {
                    val product = result.data.toProduct()
                    if (product != null) {
                        ApiResult.Success(product)
                    } else {
                        ApiResult.Error("Không thể chuyển đổi dữ liệu sản phẩm")
                    }
                }
                is ApiResult.Error -> result
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            ApiResult.Error(
                message = "Lỗi khi tải thông tin sản phẩm: ${e.message}",
                exception = e
            )
        }
    }
    
    /**
     * Get product variants by product ID
     */
    suspend fun getProductVariantsByProductId(productId: String): ApiResult<List<Product>> {
        return try {
            val result = ApiHelper.safeApiCall {
                apiService.getProductVariantsByProduct(productId)
            }
            
            when (result) {
                is ApiResult.Success -> {
                    val products = result.data.content.mapNotNull { it.toProduct() }
                    ApiResult.Success(products)
                }
                is ApiResult.Error -> result
                is ApiResult.Loading -> result
            }
        } catch (e: Exception) {
            ApiResult.Error(
                message = "Lỗi khi tải các phiên bản sản phẩm: ${e.message}",
                exception = e
            )
        }
    }
    
    /**
     * Extension function to convert ProductResponse to Product
     * Used for search results and product listings
     */
    private fun ProductResponse.toProduct(): Product? {
        return try {
            Product(
                id = id,
                name = name,
                description = description ?: "",
                brand = brand ?: "",
                material = "",
                origin = "",
                sizes = listOf("Standard"),
                colors = listOf(
                    ProductColor(
                        name = "Default",
                        hexCode = "#000000",
                        isAvailable = true
                    )
                ),
                currentPrice = price?.toInt() ?: 0,
                originalPrice = price?.let { (it * 1.2).toInt() } ?: 0,
                discount = 20,
                rating = 0f,
                reviewCount = 0,
                soldCount = 0,
                images = emptyList(),
                category = category ?: "",
                shopId = store?.id ?: "",
                shopName = store?.name ?: "",
                specifications = emptyMap()
            )
        } catch (e: Exception) {
            android.util.Log.e("ProductApiRepository", "💥 Error converting Product to Product: ${e.message}", e)
            null
        }
    }
    
    /**
     * Extension function to convert ProductVariantResponse to Product
     */
    private fun ProductVariantResponse.toProduct(): Product? {
        return try {
            // Extract size from attributes
            val size = attributes?.get("size") ?: name
            
            // Convert colors
            val colorsList = colors?.map { colorOption ->
                ProductColor(
                    name = colorOption.colorName,
                    hexCode = colorOption.colorCode ?: "#000000",
                    isAvailable = (colorOption.stock ?: 0) > 0
                )
            } ?: listOf(
                ProductColor(
                    name = "Default",
                    hexCode = "#000000",
                    isAvailable = stock > 0
                )
            )
            
            // Get images
            val imagesList = images ?: emptyList()
            
            Product(
                id = id,
                name = product?.name ?: name,
                description = product?.description ?: description ?: "",
                brand = product?.brand ?: "",
                material = attributes?.get("material") ?: "",
                origin = attributes?.get("origin") ?: "",
                sizes = listOf(size),
                colors = colorsList,
                currentPrice = price.toInt(),
                originalPrice = (price * 1.2).toInt(), // Assume 20% discount for now
                discount = 20,
                rating = averageRating?.toFloat() ?: 0f,
                reviewCount = totalReviews ?: 0,
                soldCount = 0, // Not available from API
                images = imagesList,
                category = product?.category ?: "",
                shopId = product?.store?.id ?: "",
                shopName = product?.store?.name ?: "",
                specifications = attributes ?: emptyMap()
            )
        } catch (e: Exception) {
            android.util.Log.e("ProductApiRepository", "💥 Error converting ProductVariant to Product: ${e.message}", e)
            null
        }
    }
}

