package com.demo.pbl6_android.ui.seller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SellerFinanceUiState(
    val isLoading: Boolean = false,
    val totalBalance: Long = 0L,
    val errorMessage: String? = null
)

sealed class SellerFinanceEvent {
    data object LoadData : SellerFinanceEvent()
    data object ViewTransactionHistory : SellerFinanceEvent()
    data object WithdrawMoney : SellerFinanceEvent()
    data object ViewRevenue : SellerFinanceEvent()
    data object RegisterPiShip : SellerFinanceEvent()
    data object OpenSettings : SellerFinanceEvent()
}

class SellerFinanceViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SellerFinanceUiState())
    val uiState: StateFlow<SellerFinanceUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun handleEvent(event: SellerFinanceEvent) {
        when (event) {
            is SellerFinanceEvent.LoadData -> loadData()
            is SellerFinanceEvent.ViewTransactionHistory -> viewTransactionHistory()
            is SellerFinanceEvent.WithdrawMoney -> withdrawMoney()
            is SellerFinanceEvent.ViewRevenue -> viewRevenue()
            is SellerFinanceEvent.RegisterPiShip -> registerPiShip()
            is SellerFinanceEvent.OpenSettings -> openSettings()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // TODO: Call repository to load finance data from API
                // For now, using sample data
                val totalBalance = 0L
                _uiState.value = SellerFinanceUiState(
                    isLoading = false,
                    totalBalance = totalBalance
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Đã có lỗi xảy ra"
                )
            }
        }
    }

    private fun viewTransactionHistory() {
        // TODO: Navigate to transaction history screen
    }

    private fun withdrawMoney() {
        // TODO: Navigate to withdraw money screen or show dialog
    }

    private fun viewRevenue() {
        // TODO: Navigate to revenue detail screen
    }

    private fun registerPiShip() {
        // TODO: Navigate to PiShip registration or open web view
    }

    private fun openSettings() {
        // TODO: Navigate to finance settings
    }
}

