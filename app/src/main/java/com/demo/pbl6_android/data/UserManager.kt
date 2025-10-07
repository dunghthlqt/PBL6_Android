package com.demo.pbl6_android.data

import com.demo.pbl6_android.data.model.User
import com.demo.pbl6_android.data.model.UserSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserManager {
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    private val _userSettings = MutableStateFlow(UserSettings())
    val userSettings: StateFlow<UserSettings> = _userSettings.asStateFlow()
    
    init {
        loadSampleUser()
    }
    
    fun updateUser(user: User) {
        _currentUser.value = user
    }
    
    fun updateSettings(settings: UserSettings) {
        _userSettings.value = settings
    }
    
    fun updateOrderNotification(enabled: Boolean) {
        _userSettings.value = _userSettings.value.copy(orderNotification = enabled)
    }
    
    fun updatePromotionNotification(enabled: Boolean) {
        _userSettings.value = _userSettings.value.copy(promotionNotification = enabled)
    }
    
    fun updateNewsNotification(enabled: Boolean) {
        _userSettings.value = _userSettings.value.copy(newsNotification = enabled)
    }
    
    fun updateShowPhone(enabled: Boolean) {
        _userSettings.value = _userSettings.value.copy(showPhone = enabled)
    }
    
    fun updateShowEmail(enabled: Boolean) {
        _userSettings.value = _userSettings.value.copy(showEmail = enabled)
    }
    
    fun logout() {
        _currentUser.value = null
        _userSettings.value = UserSettings()
    }
    
    private fun loadSampleUser() {
        _currentUser.value = User(
            id = "user_001",
            fullName = "Nguyễn Văn An",
            email = "nguyenvanan@gmail.com",
            phoneNumber = "0987654321",
            address = "Số 123, Đường ABC, Quận 1, TP.HCM",
            birthday = "15/03/1990",
            gender = "Nam",
            avatarUrl = "",
            memberSince = "Thành viên từ tháng 3/2023",
            orderCount = 24,
            loyaltyPoints = 156
        )
    }
}

