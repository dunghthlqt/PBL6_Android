package com.demo.pbl6_android.data.cache

import android.content.Context
import android.content.SharedPreferences
import com.demo.pbl6_android.data.model.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Simple cache for product data using SharedPreferences
 * Reduces API calls and improves load times
 */
class ProductCache(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "product_cache",
        Context.MODE_PRIVATE
    )
    
    private val gson = Gson()
    
    companion object {
        private const val KEY_LATEST_PRODUCTS = "latest_products"
        private const val KEY_LATEST_PRODUCTS_TIMESTAMP = "latest_products_timestamp"
        private const val CACHE_EXPIRY_MS = 5 * 60 * 1000L // 5 minutes
        
        @Volatile
        private var instance: ProductCache? = null
        
        fun getInstance(context: Context): ProductCache {
            return instance ?: synchronized(this) {
                instance ?: ProductCache(context.applicationContext).also { instance = it }
            }
        }
    }
    
    /**
     * Save latest products to cache
     */
    fun saveLatestProducts(products: List<Product>) {
        val json = gson.toJson(products)
        prefs.edit()
            .putString(KEY_LATEST_PRODUCTS, json)
            .putLong(KEY_LATEST_PRODUCTS_TIMESTAMP, System.currentTimeMillis())
            .apply()
        
        android.util.Log.d("ProductCache", "💾 Cached ${products.size} products")
    }
    
    /**
     * Get latest products from cache
     * Returns null if cache expired or not found
     */
    fun getLatestProducts(): List<Product>? {
        val timestamp = prefs.getLong(KEY_LATEST_PRODUCTS_TIMESTAMP, 0)
        val currentTime = System.currentTimeMillis()
        
        // Check if cache expired
        if (currentTime - timestamp > CACHE_EXPIRY_MS) {
            android.util.Log.d("ProductCache", "⏱️ Cache expired")
            return null
        }
        
        val json = prefs.getString(KEY_LATEST_PRODUCTS, null) ?: return null
        
        return try {
            val type = object : TypeToken<List<Product>>() {}.type
            val products: List<Product> = gson.fromJson(json, type)
            android.util.Log.d("ProductCache", "📦 Retrieved ${products.size} products from cache")
            products
        } catch (e: Exception) {
            android.util.Log.e("ProductCache", "❌ Error parsing cache", e)
            null
        }
    }
    
    /**
     * Check if cache is valid
     */
    fun isCacheValid(): Boolean {
        val timestamp = prefs.getLong(KEY_LATEST_PRODUCTS_TIMESTAMP, 0)
        val currentTime = System.currentTimeMillis()
        return currentTime - timestamp <= CACHE_EXPIRY_MS
    }
    
    /**
     * Clear all cached data
     */
    fun clearCache() {
        prefs.edit().clear().apply()
        android.util.Log.d("ProductCache", "🗑️ Cache cleared")
    }
    
    /**
     * Get cache age in seconds
     */
    fun getCacheAge(): Long {
        val timestamp = prefs.getLong(KEY_LATEST_PRODUCTS_TIMESTAMP, 0)
        if (timestamp == 0L) return -1
        return (System.currentTimeMillis() - timestamp) / 1000
    }
}

