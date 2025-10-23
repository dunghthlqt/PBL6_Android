package com.demo.pbl6_android.data.api

import android.content.Context
import com.demo.pbl6_android.data.auth.AuthManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val startTime = System.currentTimeMillis()
        val originalRequest = chain.request()
        val path = originalRequest.url.encodedPath
        val url = originalRequest.url.toString()

        android.util.Log.d("AuthInterceptor", "🔐 Request: ${originalRequest.method} $url")

        // Check if this endpoint should exclude token
        if (shouldExcludeToken(path)) {
            android.util.Log.d("AuthInterceptor", "✅ Public endpoint, no token needed")
            val response = chain.proceed(originalRequest)
            val duration = System.currentTimeMillis() - startTime
            android.util.Log.d("AuthInterceptor", "⏱️ Request completed in ${duration}ms (${response.code})")
            return response
        }

        val authManager = AuthManager.getInstance(context)
        val token = authManager.getToken()

        val requestBuilder = originalRequest.newBuilder()

        // Add Authorization header if token exists
        if (!token.isNullOrEmpty()) {
            android.util.Log.d("AuthInterceptor", "🔑 Adding Bearer token (length: ${token.length})")
            requestBuilder.header("Authorization", "Bearer $token")
        } else {
            android.util.Log.w("AuthInterceptor", "⚠️ No token available for protected endpoint")
        }

        val response = chain.proceed(requestBuilder.build())
        val duration = System.currentTimeMillis() - startTime
        android.util.Log.d("AuthInterceptor", "⏱️ Request completed in ${duration}ms (${response.code})")
        
        return response
    }
    
    private fun shouldExcludeToken(path: String): Boolean {
        // Exclude login, register, verify, forgot-password, and public browsing endpoints
        return path.contains("/api/v1/users/login") ||
               path.contains("/api/v1/users/register") ||
               path.contains("/api/v1/users/verify") ||
               path.contains("/forgot-password") ||
               path.contains("/reset-password") ||
               path.contains("/api/v1/products") || // Public product browsing
               path.contains("/api/v1/product-variants") || // Public product variant browsing
               path.contains("/api/v1/stores") || // Public store browsing
               path.contains("/api/v1/categories/all") || // Public categories
               path.contains("/api/v1/brands/all") // Public brands
    }
}

