package com.demo.pbl6_android.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class UserMode {
    BUYER,
    SELLER
}

class UserModeManager private constructor(context: Context) {
    
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )
    
    private val _currentMode = MutableStateFlow(getCurrentMode())
    val currentMode: StateFlow<UserMode> = _currentMode.asStateFlow()
    
    companion object {
        private const val PREFS_NAME = "user_mode_prefs"
        private const val KEY_USER_MODE = "user_mode"
        
        @Volatile
        private var instance: UserModeManager? = null
        
        fun getInstance(context: Context): UserModeManager {
            return instance ?: synchronized(this) {
                instance ?: UserModeManager(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }
    
    fun getCurrentMode(): UserMode {
        val modeString = sharedPreferences.getString(KEY_USER_MODE, UserMode.BUYER.name)
        return try {
            UserMode.valueOf(modeString ?: UserMode.BUYER.name)
        } catch (e: IllegalArgumentException) {
            UserMode.BUYER
        }
    }
    
    fun setMode(mode: UserMode) {
        sharedPreferences.edit().putString(KEY_USER_MODE, mode.name).apply()
        _currentMode.value = mode
    }
    
    fun switchToBuyer() {
        setMode(UserMode.BUYER)
    }
    
    fun switchToSeller() {
        setMode(UserMode.SELLER)
    }
    
    fun isBuyerMode(): Boolean {
        return currentMode.value == UserMode.BUYER
    }
    
    fun isSellerMode(): Boolean {
        return currentMode.value == UserMode.SELLER
    }
    
    fun clearMode() {
        sharedPreferences.edit().clear().apply()
        _currentMode.value = UserMode.BUYER
    }
}

