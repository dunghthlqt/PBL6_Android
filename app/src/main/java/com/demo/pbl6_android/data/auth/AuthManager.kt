package com.demo.pbl6_android.data.auth

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthManager private constructor(context: Context) {
    
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )
    
    private val _isAuthenticated = MutableStateFlow(isUserLoggedIn())
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()
    
    private val _currentUserId = MutableStateFlow(getUserId())
    val currentUserId: StateFlow<String?> = _currentUserId.asStateFlow()
    
    private val _currentUsername = MutableStateFlow(getUsername())
    val currentUsername: StateFlow<String?> = _currentUsername.asStateFlow()
    
    companion object {
        private const val PREFS_NAME = "auth_prefs"
        private const val KEY_TOKEN = "token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USERNAME = "username"
        private const val KEY_ROLES = "roles"
        
        @Volatile
        private var instance: AuthManager? = null
        
        fun getInstance(context: Context): AuthManager {
            return instance ?: synchronized(this) {
                instance ?: AuthManager(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }
    
    fun saveAuthData(
        token: String,
        refreshToken: String,
        userId: String,
        username: String,
        roles: List<String>
    ) {
        sharedPreferences.edit().apply {
            putString(KEY_TOKEN, token)
            putString(KEY_REFRESH_TOKEN, refreshToken)
            putString(KEY_USER_ID, userId)
            putString(KEY_USERNAME, username)
            putString(KEY_ROLES, roles.joinToString(","))
            apply()
        }
        
        _isAuthenticated.value = true
        _currentUserId.value = userId
        _currentUsername.value = username
    }
    
    fun getToken(): String? {
        return sharedPreferences.getString(KEY_TOKEN, null)
    }
    
    fun getRefreshToken(): String? {
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null)
    }
    
    fun getUserId(): String? {
        return sharedPreferences.getString(KEY_USER_ID, null)
    }
    
    fun getUsername(): String? {
        return sharedPreferences.getString(KEY_USERNAME, null)
    }
    
    fun getRoles(): List<String> {
        val rolesString = sharedPreferences.getString(KEY_ROLES, "") ?: ""
        return if (rolesString.isNotEmpty()) {
            rolesString.split(",")
        } else {
            emptyList()
        }
    }
    
    fun isUserLoggedIn(): Boolean {
        return !getToken().isNullOrEmpty()
    }
    
    fun logout() {
        sharedPreferences.edit().clear().apply()
        _isAuthenticated.value = false
        _currentUserId.value = null
        _currentUsername.value = null
    }
    
    fun clearAuthData() {
        logout()
    }
}

