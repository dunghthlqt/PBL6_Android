package com.demo.pbl6_android.data.api.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("data")
    val data: LoginData?,
    
    @SerializedName("error")
    val error: String?
)

data class LoginData(
    @SerializedName("tokenType")
    val tokenType: String?,
    
    @SerializedName("id")
    val id: String,
    
    @SerializedName("username")
    val username: String,
    
    @SerializedName("roles")
    val roles: List<String>,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("token")
    val token: String,
    
    @SerializedName("refresh_token")
    val refreshToken: String
)

