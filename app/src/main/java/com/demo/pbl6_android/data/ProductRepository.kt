package com.demo.pbl6_android.data

import android.content.Context
import com.demo.pbl6_android.data.api.ApiResult
import com.demo.pbl6_android.data.cache.ProductCache
import com.demo.pbl6_android.data.model.Product
import com.demo.pbl6_android.data.repository.ProductApiRepository

/**
 * Product Repository - Handles product data from API only
 * No mock/sample data fallback
 */
object ProductRepository {
    
    private var cache: ProductCache? = null
    
    /**
     * Initialize cache with context
     * Must be called before using cache
     */
    fun initializeCache(context: Context) {
        cache = ProductCache.getInstance(context)
    }
    
    /**
     * Get all products (latest products from API)
     * Uses cache to reduce load times
     * Returns ApiResult to handle errors properly
     */
    suspend fun getAllProducts(): ApiResult<List<Product>> {
        // Try cache first
        cache?.getLatestProducts()?.let { cachedProducts ->
            android.util.Log.d("ProductRepository", "📦 Using cached products (${cachedProducts.size} products, age: ${cache?.getCacheAge()}s)")
            return ApiResult.Success(cachedProducts)
        }
        
        // Cache miss or expired, fetch from API
        val result = ProductApiRepository.getLatestProducts(page = 0, size = 50)
        
        when (result) {
            is ApiResult.Success -> {
                android.util.Log.d("ProductRepository", "✅ API Success: Loaded ${result.data.size} products")
                // Cache the result
                cache?.saveLatestProducts(result.data)
            }
            is ApiResult.Error -> {
                android.util.Log.e("ProductRepository", "❌ API Error: ${result.message}", result.exception)
            }
            is ApiResult.Loading -> {}
        }
        
        return result
    }
    
    /**
     * Get product by ID
     */
    suspend fun getProductById(productId: String): ApiResult<Product> {
        return ProductApiRepository.getProductById(productId)
    }
    
    /**
     * Get products by category
     */
    suspend fun getProductsByCategory(category: String): ApiResult<List<Product>> {
        return ProductApiRepository.getProductsByCategory(category)
    }
    
    /**
     * Search products by name
     */
    suspend fun searchProducts(query: String): ApiResult<List<Product>> {
        return ProductApiRepository.searchProducts(query)
    }
    
    /**
     * Get related products
     */
    suspend fun getRelatedProducts(productId: String, limit: Int = 4): ApiResult<List<Product>> {
        val productResult = getProductById(productId)
        
        if (productResult !is ApiResult.Success) {
            return ApiResult.Error("Không thể tải sản phẩm liên quan")
        }
        
        val product = productResult.data
        if (product.category.isEmpty()) {
            return ApiResult.Success(emptyList())
        }
        
        return when (val result = ProductApiRepository.getProductsByCategory(product.category)) {
            is ApiResult.Success -> {
                ApiResult.Success(
                    result.data
                        .filter { it.id != productId }
                        .take(limit)
                )
            }
            is ApiResult.Error -> result
            is ApiResult.Loading -> result
        }
    }
}
