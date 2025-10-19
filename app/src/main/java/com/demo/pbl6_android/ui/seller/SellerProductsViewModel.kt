package com.demo.pbl6_android.ui.seller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.pbl6_android.ui.seller.model.SellerProduct
import com.demo.pbl6_android.ui.seller.model.SellerProductStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SellerProductsUiState(
    val isLoading: Boolean = false,
    val selectedStatus: SellerProductStatus = SellerProductStatus.OUT_OF_STOCK,
    val products: List<SellerProduct> = emptyList(),
    val productCountByStatus: Map<SellerProductStatus, Int> = emptyMap(),
    val showWarningBanner: Boolean = false,
    val warningMessage: String? = null,
    val errorMessage: String? = null
)

sealed class SellerProductsEvent {
    data object LoadData : SellerProductsEvent()
    data class SelectTab(val status: SellerProductStatus) : SellerProductsEvent()
    data class HideProduct(val product: SellerProduct) : SellerProductsEvent()
    data class EditProduct(val product: SellerProduct) : SellerProductsEvent()
    data class ShowProductMenu(val product: SellerProduct) : SellerProductsEvent()
    data object AddNewProduct : SellerProductsEvent()
    data object DismissWarning : SellerProductsEvent()
    data object HandleWarningAction : SellerProductsEvent()
}

class SellerProductsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SellerProductsUiState())
    val uiState: StateFlow<SellerProductsUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun handleEvent(event: SellerProductsEvent) {
        when (event) {
            is SellerProductsEvent.LoadData -> loadData()
            is SellerProductsEvent.SelectTab -> selectTab(event.status)
            is SellerProductsEvent.HideProduct -> hideProduct(event.product)
            is SellerProductsEvent.EditProduct -> editProduct(event.product)
            is SellerProductsEvent.ShowProductMenu -> showProductMenu(event.product)
            is SellerProductsEvent.AddNewProduct -> addNewProduct()
            is SellerProductsEvent.DismissWarning -> dismissWarning()
            is SellerProductsEvent.HandleWarningAction -> handleWarningAction()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // TODO: Call repository to load products from API
                val allProducts = getSampleProducts()
                val productCountByStatus = calculateProductCountByStatus(allProducts)
                val currentStatusProducts = allProducts.filter { 
                    it.status == _uiState.value.selectedStatus 
                }
                _uiState.value = SellerProductsUiState(
                    isLoading = false,
                    selectedStatus = _uiState.value.selectedStatus,
                    products = currentStatusProducts,
                    productCountByStatus = productCountByStatus,
                    showWarningBanner = false,
                    warningMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Đã có lỗi xảy ra"
                )
            }
        }
    }

    private fun selectTab(status: SellerProductStatus) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // TODO: Call repository to load products by status
                val allProducts = getSampleProducts()
                val filteredProducts = allProducts.filter { it.status == status }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    selectedStatus = status,
                    products = filteredProducts
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Đã có lỗi xảy ra"
                )
            }
        }
    }

    private fun hideProduct(product: SellerProduct) {
        // TODO: Implement hide product logic
        // Call repository to hide product
    }

    private fun editProduct(product: SellerProduct) {
        // TODO: Implement edit product navigation
        // Navigate to edit product screen
    }

    private fun showProductMenu(product: SellerProduct) {
        // TODO: Show product menu options
    }

    private fun addNewProduct() {
        // TODO: Navigate to add product screen
    }

    private fun dismissWarning() {
        _uiState.value = _uiState.value.copy(showWarningBanner = false)
    }

    private fun handleWarningAction() {
        // TODO: Navigate to payment/deposit screen
    }

    private fun calculateProductCountByStatus(products: List<SellerProduct>): Map<SellerProductStatus, Int> {
        return SellerProductStatus.values().associateWith { status ->
            products.count { it.status == status }
        }
    }

    private fun getSampleProducts(): List<SellerProduct> {
        return listOf(
            SellerProduct(
                id = "1",
                name = "sdp basike bear 5000mah",
                price = 50000,
                imageUrl = "",
                stockQuantity = 0,
                soldCount = 2,
                likeCount = 0,
                viewCount = 0,
                status = SellerProductStatus.OUT_OF_STOCK
            ),
            SellerProduct(
                id = "2",
                name = "Pin sạc dự phòng Li-ion 10000mAh ivalue Trắng",
                price = 100000,
                imageUrl = "",
                stockQuantity = 0,
                soldCount = 1,
                likeCount = 0,
                viewCount = 0,
                status = SellerProductStatus.OUT_OF_STOCK
            ),
            SellerProduct(
                id = "3",
                name = "Bộ sạc Usb Type - C 20w Orico",
                price = 50000,
                imageUrl = "",
                stockQuantity = 0,
                soldCount = 0,
                likeCount = 0,
                viewCount = 0,
                status = SellerProductStatus.OUT_OF_STOCK
            ),
            SellerProduct(
                id = "4",
                name = "bộ phát wifi ZTE mf65m",
                price = 120000,
                imageUrl = "",
                stockQuantity = 0,
                soldCount = 1,
                likeCount = 0,
                viewCount = 0,
                status = SellerProductStatus.OUT_OF_STOCK
            ),
            SellerProduct(
                id = "5",
                name = "Tai nghe Bluetooth Premium",
                price = 250000,
                imageUrl = "",
                stockQuantity = 15,
                soldCount = 50,
                likeCount = 25,
                viewCount = 320,
                status = SellerProductStatus.IN_STOCK
            ),
            SellerProduct(
                id = "6",
                name = "Chuột Gaming RGB",
                price = 350000,
                imageUrl = "",
                stockQuantity = 20,
                soldCount = 35,
                likeCount = 18,
                viewCount = 250,
                status = SellerProductStatus.IN_STOCK
            ),
            SellerProduct(
                id = "7",
                name = "Bàn phím cơ Blue Switch",
                price = 650000,
                imageUrl = "",
                stockQuantity = 0,
                soldCount = 0,
                likeCount = 5,
                viewCount = 45,
                status = SellerProductStatus.PENDING_APPROVAL
            ),
            SellerProduct(
                id = "8",
                name = "Webcam Full HD 1080p",
                price = 450000,
                imageUrl = "",
                stockQuantity = 0,
                soldCount = 0,
                likeCount = 0,
                viewCount = 0,
                status = SellerProductStatus.VIOLATION
            ),
            SellerProduct(
                id = "9",
                name = "Đế tản nhiệt Laptop",
                price = 180000,
                imageUrl = "",
                stockQuantity = 0,
                soldCount = 12,
                likeCount = 8,
                viewCount = 95,
                status = SellerProductStatus.HIDDEN
            )
        )
    }
}

