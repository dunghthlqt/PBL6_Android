package com.demo.pbl6_android.ui.seller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.pbl6_android.ui.seller.model.SalesMetric
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class SalesTab {
    REVENUE,
    PRODUCTS
}

enum class TimeFilter {
    TODAY,
    YESTERDAY,
    LAST_7_DAYS,
    LAST_30_DAYS,
    BY_DAY,
    BY_WEEK,
    BY_MONTH
}

data class SellerSalesUiState(
    val isLoading: Boolean = false,
    val selectedTab: SalesTab = SalesTab.REVENUE,
    val selectedTimeFilter: TimeFilter = TimeFilter.LAST_30_DAYS,
    val dateRange: String = "19 Th09 - 18 Th10",
    val metrics: List<SalesMetric> = emptyList(),
    val errorMessage: String? = null
)

sealed class SellerSalesEvent {
    data object LoadData : SellerSalesEvent()
    data class SelectTab(val tab: SalesTab) : SellerSalesEvent()
    data class SelectTimeFilter(val filter: TimeFilter) : SellerSalesEvent()
    data object OpenFilterDropdown : SellerSalesEvent()
    data object OpenCategoryFilter : SellerSalesEvent()
}

class SellerSalesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SellerSalesUiState())
    val uiState: StateFlow<SellerSalesUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun handleEvent(event: SellerSalesEvent) {
        when (event) {
            is SellerSalesEvent.LoadData -> loadData()
            is SellerSalesEvent.SelectTab -> selectTab(event.tab)
            is SellerSalesEvent.SelectTimeFilter -> selectTimeFilter(event.filter)
            is SellerSalesEvent.OpenFilterDropdown -> openFilterDropdown()
            is SellerSalesEvent.OpenCategoryFilter -> openCategoryFilter()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // TODO: Call repository to load sales data from API
                val metrics = getMetricsForTab(_uiState.value.selectedTab)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    metrics = metrics
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Đã có lỗi xảy ra"
                )
            }
        }
    }

    private fun selectTab(tab: SalesTab) {
        viewModelScope.launch {
            val metrics = getMetricsForTab(tab)
            _uiState.value = _uiState.value.copy(
                selectedTab = tab,
                metrics = metrics
            )
        }
    }

    private fun selectTimeFilter(filter: TimeFilter) {
        _uiState.value = _uiState.value.copy(selectedTimeFilter = filter)
        loadData()
    }

    private fun openFilterDropdown() {
        // TODO: Show dropdown filter dialog
    }

    private fun openCategoryFilter() {
        // TODO: Show category filter dialog
    }

    private fun getMetricsForTab(tab: SalesTab): List<SalesMetric> {
        return when (tab) {
            SalesTab.REVENUE -> listOf(
                SalesMetric("Doanh số", "đ0", "0%"),
                SalesMetric("Đơn hàng", "0", "0%"),
                SalesMetric("Doanh số trên mỗi đơn hàng", "đ0", "0%"),
                SalesMetric("Người mua", "0", "0%"),
                SalesMetric("Doanh số trên mỗi Người Mua", "đ0", "0%"),
                SalesMetric("Tỷ lệ chuyển đổi đơn hàng", "0%", "0%")
            )
            SalesTab.PRODUCTS -> listOf(
                SalesMetric("Số lượt truy cập", "0", "0%", hasInfoIcon = true),
                SalesMetric("Khách truy cập thêm vào giỏ hàng", "0", "0%"),
                SalesMetric("Tỷ lệ thêm vào giỏ hàng", "0%", "0%"),
                SalesMetric("Người mua đã đặt", "0", "0%"),
                SalesMetric("Sản phẩm trong Đơn hàng đã Đặt", "0", "0%"),
                SalesMetric("Tỷ lệ chuyển đổi thành đơn đã xác nhận", "0%", "0%"),
                SalesMetric("Người mua", "0", "0%"),
                SalesMetric("Sản phẩm đã được xác nhận", "0", "0%")
            )
        }
    }
}

