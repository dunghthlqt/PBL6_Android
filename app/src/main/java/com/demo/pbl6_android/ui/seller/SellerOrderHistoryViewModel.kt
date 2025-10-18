package com.demo.pbl6_android.ui.seller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.pbl6_android.ui.seller.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SellerOrderHistoryUiState(
    val isLoading: Boolean = false,
    val selectedMainTab: SellerOrderStatus = SellerOrderStatus.PENDING_CONFIRMATION,
    val selectedPendingPickupSubTab: PendingPickupSubStatus = PendingPickupSubStatus.ALL,
    val selectedCancelledSubTab: CancelledSubStatus = CancelledSubStatus.ALL,
    val selectedReturnRefundSubTab: ReturnRefundSubStatus = ReturnRefundSubStatus.ALL,
    val selectedDeliveryFailedSubTab: DeliveryFailedSubStatus = DeliveryFailedSubStatus.ALL,
    val selectedShippingMethod: ShippingMethod = ShippingMethod.ALL,
    val selectedSortType: OrderSortType = OrderSortType.CONFIRM_DATE_DESC,
    val orders: List<SellerOrder> = emptyList(),
    val errorMessage: String? = null
)

sealed class SellerOrderHistoryEvent {
    data class SelectMainTab(val status: SellerOrderStatus) : SellerOrderHistoryEvent()
    data class SelectPendingPickupSubTab(val subStatus: PendingPickupSubStatus) : SellerOrderHistoryEvent()
    data class SelectCancelledSubTab(val subStatus: CancelledSubStatus) : SellerOrderHistoryEvent()
    data class SelectReturnRefundSubTab(val subStatus: ReturnRefundSubStatus) : SellerOrderHistoryEvent()
    data class SelectDeliveryFailedSubTab(val subStatus: DeliveryFailedSubStatus) : SellerOrderHistoryEvent()
    data class SelectShippingMethod(val method: ShippingMethod) : SellerOrderHistoryEvent()
    data class SelectSortType(val sortType: OrderSortType) : SellerOrderHistoryEvent()
    data class SearchOrders(val query: String) : SellerOrderHistoryEvent()
    data object RefreshOrders : SellerOrderHistoryEvent()
}

class SellerOrderHistoryViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SellerOrderHistoryUiState())
    val uiState: StateFlow<SellerOrderHistoryUiState> = _uiState.asStateFlow()

    init {
        loadOrders()
    }

    fun handleEvent(event: SellerOrderHistoryEvent) {
        when (event) {
            is SellerOrderHistoryEvent.SelectMainTab -> selectMainTab(event.status)
            is SellerOrderHistoryEvent.SelectPendingPickupSubTab -> selectPendingPickupSubTab(event.subStatus)
            is SellerOrderHistoryEvent.SelectCancelledSubTab -> selectCancelledSubTab(event.subStatus)
            is SellerOrderHistoryEvent.SelectReturnRefundSubTab -> selectReturnRefundSubTab(event.subStatus)
            is SellerOrderHistoryEvent.SelectDeliveryFailedSubTab -> selectDeliveryFailedSubTab(event.subStatus)
            is SellerOrderHistoryEvent.SelectShippingMethod -> selectShippingMethod(event.method)
            is SellerOrderHistoryEvent.SelectSortType -> selectSortType(event.sortType)
            is SellerOrderHistoryEvent.SearchOrders -> searchOrders(event.query)
            is SellerOrderHistoryEvent.RefreshOrders -> loadOrders()
        }
    }

    private fun selectMainTab(status: SellerOrderStatus) {
        _uiState.value = _uiState.value.copy(
            selectedMainTab = status,
            selectedShippingMethod = ShippingMethod.ALL,
            selectedSortType = OrderSortType.CONFIRM_DATE_DESC
        )
        loadOrders()
    }

    private fun selectPendingPickupSubTab(subStatus: PendingPickupSubStatus) {
        _uiState.value = _uiState.value.copy(selectedPendingPickupSubTab = subStatus)
        loadOrders()
    }

    private fun selectCancelledSubTab(subStatus: CancelledSubStatus) {
        _uiState.value = _uiState.value.copy(selectedCancelledSubTab = subStatus)
        loadOrders()
    }

    private fun selectReturnRefundSubTab(subStatus: ReturnRefundSubStatus) {
        _uiState.value = _uiState.value.copy(selectedReturnRefundSubTab = subStatus)
        loadOrders()
    }

    private fun selectDeliveryFailedSubTab(subStatus: DeliveryFailedSubStatus) {
        _uiState.value = _uiState.value.copy(selectedDeliveryFailedSubTab = subStatus)
        loadOrders()
    }

    private fun selectShippingMethod(method: ShippingMethod) {
        _uiState.value = _uiState.value.copy(selectedShippingMethod = method)
        loadOrders()
    }

    private fun selectSortType(sortType: OrderSortType) {
        _uiState.value = _uiState.value.copy(selectedSortType = sortType)
        loadOrders()
    }

    private fun searchOrders(query: String) {
        // TODO: Implement search
    }

    private fun loadOrders() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                // TODO: Call repository to load orders from API
                val orders = getSampleOrders()
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    orders = orders
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Đã có lỗi xảy ra"
                )
            }
        }
    }

    private fun getSampleOrders(): List<SellerOrder> {
        val currentTime = System.currentTimeMillis()
        val allOrders = mutableListOf(
            SellerOrder(
                orderId = "1",
                orderCode = "#230629MPHVCD9M",
                buyerName = "minhpleiku1990",
                status = SellerOrderStatus.DELIVERED,
                products = listOf(
                    OrderProduct(
                        productId = "p1",
                        productName = "Pin sạc dự phòng Li-ion 10000mAh ivalue Trắng",
                        imageUrl = "",
                        variant = "",
                        quantity = 1,
                        price = 100000
                    )
                ),
                totalAmount = 128700,
                shippingMethod = ShippingMethod.FAST,
                orderDate = currentTime - 7 * 24 * 60 * 60 * 1000,
                confirmDate = currentTime - 6 * 24 * 60 * 60 * 1000,
                deliveryDeadline = currentTime - 1 * 24 * 60 * 60 * 1000,
                isRated = false
            ),
            SellerOrder(
                orderId = "2",
                orderCode = "#2206282E9FH8YN",
                buyerName = "dergeist",
                status = SellerOrderStatus.DELIVERED,
                products = listOf(
                    OrderProduct(
                        productId = "p2",
                        productName = "bộ phát wifi ZTE mf65m",
                        imageUrl = "",
                        variant = "",
                        quantity = 1,
                        price = 120000
                    )
                ),
                totalAmount = 157700,
                shippingMethod = ShippingMethod.FAST,
                orderDate = currentTime - 10 * 24 * 60 * 60 * 1000,
                confirmDate = currentTime - 9 * 24 * 60 * 60 * 1000,
                deliveryDeadline = currentTime - 3 * 24 * 60 * 60 * 1000,
                isRated = false
            ),
            SellerOrder(
                orderId = "3",
                orderCode = "#2205158FE9BTJJ",
                buyerName = "utphongcomputer",
                status = SellerOrderStatus.DELIVERED,
                products = listOf(
                    OrderProduct(
                        productId = "p3",
                        productName = "Android TV Box X96 mini plus",
                        imageUrl = "",
                        variant = "",
                        quantity = 1,
                        price = 400000
                    )
                ),
                totalAmount = 427500,
                shippingMethod = ShippingMethod.EXPRESS,
                orderDate = currentTime - 15 * 24 * 60 * 60 * 1000,
                confirmDate = currentTime - 14 * 24 * 60 * 60 * 1000,
                deliveryDeadline = currentTime - 8 * 24 * 60 * 60 * 1000,
                isRated = false
            ),
            // Cancelled Orders
            SellerOrder(
                orderId = "4",
                orderCode = "#230715ABCDEF12",
                buyerName = "nguyenvana",
                status = SellerOrderStatus.CANCELLED,
                products = listOf(
                    OrderProduct(
                        productId = "p4",
                        productName = "Tai nghe Bluetooth TWS Xiaomi Redmi Buds 3",
                        imageUrl = "",
                        variant = "",
                        quantity = 1,
                        price = 250000
                    )
                ),
                totalAmount = 250000,
                shippingMethod = ShippingMethod.FAST,
                orderDate = currentTime - 5 * 24 * 60 * 60 * 1000,
                cancelledBy = CancelledBy.BUYER,
                cancellationReason = CancellationReason.CHANGED_MIND,
                cancellationDate = currentTime - 4 * 24 * 60 * 60 * 1000
            ),
            SellerOrder(
                orderId = "5",
                orderCode = "#230720GHIJKL34",
                buyerName = "tranvanb",
                status = SellerOrderStatus.CANCELLED,
                products = listOf(
                    OrderProduct(
                        productId = "p5",
                        productName = "Chuột không dây Logitech M185",
                        imageUrl = "",
                        variant = "",
                        quantity = 2,
                        price = 150000
                    )
                ),
                totalAmount = 300000,
                shippingMethod = ShippingMethod.ECONOMY,
                orderDate = currentTime - 8 * 24 * 60 * 60 * 1000,
                cancelledBy = CancelledBy.BUYER,
                cancellationReason = CancellationReason.VOUCHER_CHANGE,
                cancellationDate = currentTime - 7 * 24 * 60 * 60 * 1000
            ),
            SellerOrder(
                orderId = "6",
                orderCode = "#230722MNOPQR56",
                buyerName = "phamthic",
                status = SellerOrderStatus.CANCELLED,
                products = listOf(
                    OrderProduct(
                        productId = "p6",
                        productName = "Ốp lưng iPhone 13 Pro Max Spigen",
                        imageUrl = "",
                        variant = "Màu đen",
                        quantity = 1,
                        price = 350000
                    )
                ),
                totalAmount = 350000,
                shippingMethod = ShippingMethod.EXPRESS,
                orderDate = currentTime - 3 * 24 * 60 * 60 * 1000,
                cancelledBy = CancelledBy.BUYER,
                cancellationReason = CancellationReason.ADDRESS_CHANGE,
                cancellationDate = currentTime - 2 * 24 * 60 * 60 * 1000
            ),
            SellerOrder(
                orderId = "7",
                orderCode = "#230725STUVWX78",
                buyerName = "levand",
                status = SellerOrderStatus.CANCELLED,
                products = listOf(
                    OrderProduct(
                        productId = "p7",
                        productName = "Bàn phím cơ Gaming K552",
                        imageUrl = "",
                        variant = "Blue Switch",
                        quantity = 1,
                        price = 550000
                    )
                ),
                totalAmount = 550000,
                shippingMethod = ShippingMethod.FAST,
                orderDate = currentTime - 12 * 24 * 60 * 60 * 1000,
                cancelledBy = CancelledBy.SELLER,
                cancellationReason = CancellationReason.CHANGED_MIND,
                cancellationDate = currentTime - 11 * 24 * 60 * 60 * 1000
            )
        )
        
        // Filter orders based on selected tab
        return allOrders.filter { order ->
            order.status == _uiState.value.selectedMainTab
        }
    }

    fun getShippingMethodsForCurrentTab(): List<ShippingMethod> {
        return when (_uiState.value.selectedMainTab) {
            SellerOrderStatus.PENDING_PICKUP,
            SellerOrderStatus.DELIVERY_FAILED -> listOf(
                ShippingMethod.ALL,
                ShippingMethod.STANDARD
            )
            else -> listOf(
                ShippingMethod.ALL,
                ShippingMethod.EXPRESS,
                ShippingMethod.BULKY,
                ShippingMethod.FAST,
                ShippingMethod.ECONOMY
            )
        }
    }

    fun hasSubTabs(): Boolean {
        return when (_uiState.value.selectedMainTab) {
            SellerOrderStatus.PENDING_PICKUP,
            SellerOrderStatus.CANCELLED,
            SellerOrderStatus.RETURN_REFUND,
            SellerOrderStatus.DELIVERY_FAILED -> true
            else -> false
        }
    }

    fun hasSortFilter(): Boolean {
        return _uiState.value.selectedMainTab == SellerOrderStatus.PENDING_PICKUP
    }
}

