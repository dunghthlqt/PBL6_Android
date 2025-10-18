package com.demo.pbl6_android.ui.seller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.pbl6_android.R
import com.demo.pbl6_android.ui.seller.model.SellerRecommendation
import com.demo.pbl6_android.ui.seller.model.SellerStats
import com.demo.pbl6_android.ui.seller.model.SellerTask
import com.demo.pbl6_android.ui.seller.model.ShopProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SellerHomeUiState(
    val isLoading: Boolean = false,
    val shopProfile: ShopProfile? = null,
    val stats: SellerStats = SellerStats(),
    val tasks: List<SellerTask> = emptyList(),
    val recommendations: List<SellerRecommendation> = emptyList(),
    val errorMessage: String? = null
)

sealed class SellerHomeEvent {
    data object LoadData : SellerHomeEvent()
    data object RefreshData : SellerHomeEvent()
    data object ViewShop : SellerHomeEvent()
    data object OpenSettings : SellerHomeEvent()
    data object OpenNotifications : SellerHomeEvent()
    data class StartTask(val taskId: String) : SellerHomeEvent()
    data class TryRecommendation(val recommendationId: String) : SellerHomeEvent()
}

class SellerHomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SellerHomeUiState())
    val uiState: StateFlow<SellerHomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun handleEvent(event: SellerHomeEvent) {
        when (event) {
            is SellerHomeEvent.LoadData -> loadData()
            is SellerHomeEvent.RefreshData -> refreshData()
            is SellerHomeEvent.ViewShop -> viewShop()
            is SellerHomeEvent.OpenSettings -> openSettings()
            is SellerHomeEvent.OpenNotifications -> openNotifications()
            is SellerHomeEvent.StartTask -> startTask(event.taskId)
            is SellerHomeEvent.TryRecommendation -> tryRecommendation(event.recommendationId)
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                // TODO: Call repository to load seller data from API
                val shopProfile = getShopProfileSample()
                val stats = getStatsSample()
                val tasks = getTasksSample()
                val recommendations = getRecommendationsSample()
                
                _uiState.value = SellerHomeUiState(
                    isLoading = false,
                    shopProfile = shopProfile,
                    stats = stats,
                    tasks = tasks,
                    recommendations = recommendations
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Đã có lỗi xảy ra"
                )
            }
        }
    }

    private fun refreshData() {
        loadData()
    }

    private fun viewShop() {
        // Handle navigation to shop view
    }

    private fun openSettings() {
        // Handle navigation to settings
    }

    private fun openNotifications() {
        // Handle navigation to notifications
    }

    private fun startTask(taskId: String) {
        viewModelScope.launch {
            try {
                // TODO: Implement task start logic
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Không thể bắt đầu nhiệm vụ"
                )
            }
        }
    }

    private fun tryRecommendation(recommendationId: String) {
        viewModelScope.launch {
            try {
                // TODO: Implement recommendation action
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Không thể thực hiện"
                )
            }
        }
    }

    private fun getShopProfileSample(): ShopProfile {
        return ShopProfile(
            id = "shop_001",
            name = "LTT Computer - Gaming gear",
            url = "shopee.vn/dunglqmblol123",
            avatarUrl = "",
            notificationCount = 20
        )
    }

    private fun getStatsSample(): SellerStats {
        return SellerStats(
            pendingOrders = 0,
            cancelledOrders = 0,
            returnOrders = 0,
            reviewsToRespond = 6
        )
    }

    private fun getTasksSample(): List<SellerTask> {
        return listOf(
            SellerTask(
                id = "task_1",
                title = "Livestream tối thiểu 15 phút",
                reward = "Nhận đ30.000 Chi phí Quảng Cáo Shopee",
                currentProgress = 0,
                maxProgress = 1
            ),
            SellerTask(
                id = "task_2",
                title = "Tạo mã giảm giá",
                reward = "Nhận đ3.000 Chi phí Quảng Cáo Shopee",
                currentProgress = 0,
                maxProgress = 15
            )
        )
    }

    private fun getRecommendationsSample(): List<SellerRecommendation> {
        return listOf(
            SellerRecommendation(
                id = "rec_1",
                title = "Quảng cáo Shopee",
                description = "Tăng trung bình 30% lượng truy cập và doanh thu đơn hàng",
                iconResId = R.drawable.ic_megaphone
            ),
            SellerRecommendation(
                id = "rec_2",
                title = "Giải Pháp Tiếp Thị Liên Kết",
                description = "Đẩy mạnh quảng bá Shop với mạng lưới tiếp thị liên kết rộng lớn của Shopee",
                iconResId = R.drawable.ic_bullhorn
            )
        )
    }
}

