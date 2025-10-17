package com.demo.pbl6_android.ui.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.pbl6_android.data.model.Product
import com.demo.pbl6_android.ui.shop.model.ShopInfo
import com.demo.pbl6_android.ui.shop.model.ShopVoucher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ShopUiState(
    val isLoading: Boolean = false,
    val shopInfo: ShopInfo? = null,
    val vouchers: List<ShopVoucher> = emptyList(),
    val flashSaleProducts: List<Product> = emptyList(),
    val suggestedProducts: List<Product> = emptyList(),
    val errorMessage: String? = null
)

sealed class ShopEvent {
    data class LoadShop(val shopId: String?) : ShopEvent()
    data class ToggleFollow(val shopId: String) : ShopEvent()
    data class SaveVoucher(val voucher: ShopVoucher) : ShopEvent()
    data class RemindProduct(val productId: String) : ShopEvent()
    data object OpenChat : ShopEvent()
}

class ShopViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ShopUiState())
    val uiState: StateFlow<ShopUiState> = _uiState.asStateFlow()

    fun handleEvent(event: ShopEvent) {
        when (event) {
            is ShopEvent.LoadShop -> loadShop(event.shopId)
            is ShopEvent.ToggleFollow -> toggleFollow(event.shopId)
            is ShopEvent.SaveVoucher -> saveVoucher(event.voucher)
            is ShopEvent.RemindProduct -> remindProduct(event.productId)
            is ShopEvent.OpenChat -> openChat()
        }
    }

    private fun loadShop(shopName: String?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                // TODO: Call repository to load shop data from API
                // Sử dụng shopName tạm thời
                val shopInfo = ShopInfo(
                    id = "shop_${shopName?.hashCode() ?: 0}",
                    name = shopName ?: "LagiHitech",
                    rating = 4.9f,
                    followers = 69200,
                    isFollowing = false,
                    avatarUrl = "",
                    totalProducts = 150
                )
                val vouchers = getVouchersSample()
                val flashSaleProducts = getFlashSaleProductsSample()
                val suggestedProducts = getSuggestedProductsSample()
                
                _uiState.value = ShopUiState(
                    isLoading = false,
                    shopInfo = shopInfo,
                    vouchers = vouchers,
                    flashSaleProducts = flashSaleProducts,
                    suggestedProducts = suggestedProducts
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Đã có lỗi xảy ra"
                )
            }
        }
    }

    private fun toggleFollow(shopId: String) {
        viewModelScope.launch {
            try {
                // TODO: Call API to follow/unfollow shop
                val currentShopInfo = _uiState.value.shopInfo
                if (currentShopInfo != null) {
                    val updatedShopInfo = currentShopInfo.copy(
                        isFollowing = !currentShopInfo.isFollowing
                    )
                    _uiState.value = _uiState.value.copy(shopInfo = updatedShopInfo)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Không thể thực hiện"
                )
            }
        }
    }

    private fun saveVoucher(voucher: ShopVoucher) {
        viewModelScope.launch {
            try {
                // TODO: Call API to save voucher
                val updatedVouchers = _uiState.value.vouchers.map {
                    if (it.id == voucher.id) it.copy(isSaved = true) else it
                }
                _uiState.value = _uiState.value.copy(vouchers = updatedVouchers)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Không thể lưu voucher"
                )
            }
        }
    }

    private fun remindProduct(productId: String) {
        viewModelScope.launch {
            try {
                // TODO: Call API to set reminder
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Không thể đặt nhắc nhở"
                )
            }
        }
    }

    private fun openChat() {
        // Handle chat navigation
    }

    private fun getShopInfoSample(): ShopInfo {
        return ShopInfo(
            id = "shop_lagihi tech_001",
            name = _uiState.value.shopInfo?.name ?: "LagiHitech",
            rating = 4.9f,
            followers = 69200,
            isFollowing = false,
            avatarUrl = "",
            totalProducts = 150
        )
    }
    
    private fun getVouchersSample(): List<ShopVoucher> {
        return listOf(
            ShopVoucher(
                id = "1",
                discount = "15k",
                minAmount = "399k",
                expiryDate = "31.10.2025",
                quantity = 3
            ),
            ShopVoucher(
                id = "2",
                discount = "40k",
                minAmount = "599k",
                expiryDate = "31.10.2025",
                quantity = 1
            )
        )
    }
    
    private fun getFlashSaleProductsSample(): List<Product> {
        return listOf(
            Product(
                id = "flash_1",
                name = "Máy triệt lông Halio",
                description = "Máy triệt lông băng lạnh",
                brand = "Halio",
                material = "ABS",
                origin = "Hàn Quốc",
                sizes = listOf("Standard"),
                colors = emptyList(),
                currentPrice = 2720000,
                originalPrice = 4000000,
                discount = 32,
                rating = 4.8f,
                reviewCount = 1250,
                soldCount = 3200,
                images = emptyList(),
                category = "Điện tử",
                shopId = "shop_001",
                shopName = "LagiHitech"
            ),
            Product(
                id = "flash_2",
                name = "Ghế chính hãng Roichen",
                description = "Ghế ngồi ngủ đa năng",
                brand = "Roichen",
                material = "Vải cao cấp",
                origin = "Việt Nam",
                sizes = listOf("M", "L"),
                colors = emptyList(),
                currentPrice = 777000,
                originalPrice = 990000,
                discount = 22,
                rating = 4.7f,
                reviewCount = 850,
                soldCount = 2100,
                images = emptyList(),
                category = "Nội thất",
                shopId = "shop_001",
                shopName = "LagiHitech"
            )
        )
    }
    
    private fun getSuggestedProductsSample(): List<Product> {
        return listOf(
            Product(
                id = "prod_1",
                name = "Bàn chải đánh răng điện",
                description = "Bàn chải tự động, sạch sâu",
                brand = "OralB",
                material = "Nhựa y tế",
                origin = "Hàn Quốc",
                sizes = listOf("Standard"),
                colors = emptyList(),
                currentPrice = 450000,
                originalPrice = 650000,
                discount = 31,
                rating = 4.6f,
                reviewCount = 520,
                soldCount = 1500,
                images = emptyList(),
                category = "Sức khỏe",
                shopId = "shop_001",
                shopName = "LagiHitech"
            ),
            Product(
                id = "prod_2",
                name = "Ổ cứng SSD Samsung 1TB",
                description = "Tốc độ cao, bền bỉ",
                brand = "Samsung",
                material = "SSD",
                origin = "Hàn Quốc",
                sizes = listOf("1TB"),
                colors = emptyList(),
                currentPrice = 2100000,
                originalPrice = 2500000,
                discount = 16,
                rating = 4.9f,
                reviewCount = 2100,
                soldCount = 5200,
                images = emptyList(),
                category = "Điện tử",
                shopId = "shop_001",
                shopName = "LagiHitech"
            ),
            Product(
                id = "prod_3",
                name = "Bàn phím cơ Gaming RGB",
                description = "Switch xanh, đèn RGB",
                brand = "Akko",
                material = "Nhôm",
                origin = "Trung Quốc",
                sizes = listOf("Full-size"),
                colors = emptyList(),
                currentPrice = 1250000,
                originalPrice = 1800000,
                discount = 31,
                rating = 4.8f,
                reviewCount = 890,
                soldCount = 1950,
                images = emptyList(),
                category = "Gaming",
                shopId = "shop_001",
                shopName = "LagiHitech"
            ),
            Product(
                id = "prod_4",
                name = "Tai nghe Bluetooth AirPods",
                description = "Chống ồn chủ động",
                brand = "Apple",
                material = "Nhựa cao cấp",
                origin = "Trung Quốc",
                sizes = listOf("Standard"),
                colors = emptyList(),
                currentPrice = 4500000,
                originalPrice = 5500000,
                discount = 18,
                rating = 4.7f,
                reviewCount = 1580,
                soldCount = 3100,
                images = emptyList(),
                category = "Audio",
                shopId = "shop_001",
                shopName = "LagiHitech"
            )
        )
    }
}

