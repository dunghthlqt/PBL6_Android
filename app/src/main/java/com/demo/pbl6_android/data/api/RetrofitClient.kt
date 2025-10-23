package com.demo.pbl6_android.data.api

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    
    private const val BASE_URL = "https://e-commerce-raq1.onrender.com/"
    
    private lateinit var context: Context
    private var apiServiceInstance: ApiService? = null
    
    /**
     * Initialize RetrofitClient with application context
     * Must be called before accessing apiService
     */
    fun initialize(applicationContext: Context) {
        context = applicationContext
    }
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .addInterceptor(loggingInterceptor)
            // Increased timeout for Render.com free tier (can take up to 90s for cold start)
            .connectTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .callTimeout(120, TimeUnit.SECONDS) // Overall timeout for entire call
            // Retry on connection failure
            .retryOnConnectionFailure(true)
            // Connection pool to reuse connections
            .connectionPool(okhttp3.ConnectionPool(5, 5, TimeUnit.MINUTES))
            .build()
    }
    
    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    val apiService: ApiService
        get() {
            if (apiServiceInstance == null) {
                if (!::context.isInitialized) {
                    throw IllegalStateException(
                        "RetrofitClient must be initialized with context before use. " +
                        "Call RetrofitClient.initialize(context) in your Application class."
                    )
                }
                apiServiceInstance = createRetrofit().create(ApiService::class.java)
            }
            return apiServiceInstance!!
        }
}

