package com.demo.pbl6_android.data

import com.demo.pbl6_android.data.model.Voucher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object VoucherManager {
    
    private val _selectedShopVoucher = MutableStateFlow<Voucher?>(null)
    val selectedShopVoucher: StateFlow<Voucher?> = _selectedShopVoucher
    
    private val _selectedPlatformShippingVoucher = MutableStateFlow<Voucher?>(null)
    val selectedPlatformShippingVoucher: StateFlow<Voucher?> = _selectedPlatformShippingVoucher
    
    private val _selectedPlatformDiscountVoucher = MutableStateFlow<Voucher?>(null)
    val selectedPlatformDiscountVoucher: StateFlow<Voucher?> = _selectedPlatformDiscountVoucher
    
    fun selectShopVoucher(voucher: Voucher?) {
        _selectedShopVoucher.value = voucher
    }
    
    fun selectPlatformVoucher(voucher: Voucher?) {
        val isShippingVoucher = voucher?.code?.contains("SHIP", ignoreCase = true) == true || 
                                voucher?.title?.contains("vận chuyển", ignoreCase = true) == true ||
                                voucher?.title?.contains("ship", ignoreCase = true) == true
        
        if (isShippingVoucher) {
            _selectedPlatformShippingVoucher.value = voucher
        } else {
            _selectedPlatformDiscountVoucher.value = voucher
        }
    }
    
    fun clear() {
        _selectedShopVoucher.value = null
        _selectedPlatformShippingVoucher.value = null
        _selectedPlatformDiscountVoucher.value = null
    }
    
    fun calculateDiscount(originalPrice: Int, voucher: Voucher?): Int {
        if (voucher == null) return 0
        
        // Check minimum amount
        if (originalPrice < voucher.minAmount) return 0
        
        return when {
            voucher.discount.contains("%") -> {
                val percentage = voucher.discount.replace("%", "").toIntOrNull() ?: 0
                val discount = (originalPrice * percentage / 100)
                if (voucher.maxDiscount != null) {
                    minOf(discount, voucher.maxDiscount)
                } else {
                    discount
                }
            }
            voucher.discount.contains("₫") -> {
                voucher.discount.replace("₫", "").replace(".", "").toIntOrNull() ?: 0
            }
            else -> 0
        }
    }
}

