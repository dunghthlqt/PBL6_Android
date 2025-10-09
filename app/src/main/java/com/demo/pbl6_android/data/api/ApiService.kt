package com.demo.pbl6_android.data.api

import com.demo.pbl6_android.data.api.model.LoginRequest
import com.demo.pbl6_android.data.api.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    
    @POST("api/v1/users/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}

