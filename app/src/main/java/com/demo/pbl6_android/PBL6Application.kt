package com.demo.pbl6_android

import android.app.Application
import com.demo.pbl6_android.data.api.RetrofitClient

class PBL6Application : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize RetrofitClient with application context
        RetrofitClient.initialize(applicationContext)
        
        // Initialize Product Cache
        com.demo.pbl6_android.data.ProductRepository.initializeCache(applicationContext)
    }
}

