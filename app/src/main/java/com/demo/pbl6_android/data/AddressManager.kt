package com.demo.pbl6_android.data

import com.demo.pbl6_android.data.api.ApiResult
import com.demo.pbl6_android.data.api.model.AddressDTO
import com.demo.pbl6_android.data.api.model.AddressResponse
import com.demo.pbl6_android.data.model.Address
import com.demo.pbl6_android.data.repository.AddressApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Address Manager - Manages user delivery address
 * NOTE: Backend API only supports ONE address per user (not a list)
 */
object AddressManager {
    
    // Keep as list for UI compatibility, but API only returns ONE address
    private val _addresses = MutableStateFlow<List<Address>>(emptyList())
    val addresses: StateFlow<List<Address>> = _addresses
    
    private val _selectedAddress = MutableStateFlow<Address?>(null)
    val selectedAddress: StateFlow<Address?> = _selectedAddress
    
    /**
     * Load user address from API
     * Returns ApiResult<Address?> - null if user has no address
     */
    suspend fun loadAddress(): ApiResult<Address?> {
        return try {
            android.util.Log.d("AddressManager", "📍 Loading user address...")
            
            val result = AddressApiRepository.getUserAddress()
            
            when (result) {
                is ApiResult.Success -> {
                    val address = result.data.toAddress()
                    _addresses.value = listOf(address)
                    _selectedAddress.value = address
                    android.util.Log.d("AddressManager", "✅ Address loaded")
                    ApiResult.Success(address)
                }
                is ApiResult.Error -> {
                    // If 404, user has no address yet
                    android.util.Log.w("AddressManager", "⚠️ No address found: ${result.message}")
                    _addresses.value = emptyList()
                    _selectedAddress.value = null
                    ApiResult.Success(null)  // Success with null data = no address
                }
                is ApiResult.Loading -> ApiResult.Loading
            }
        } catch (e: Exception) {
            android.util.Log.e("AddressManager", "💥 Error loading address: ${e.message}", e)
            ApiResult.Error("Lỗi khi tải địa chỉ: ${e.message}", exception = e)
        }
    }
    
    /**
     * Check if user has address
     */
    suspend fun checkHasAddress(): ApiResult<Boolean> {
        return try {
            android.util.Log.d("AddressManager", "🔍 Checking if user has address...")
            
            val result = AddressApiRepository.checkHasAddress()
            
            when (result) {
                is ApiResult.Success -> {
                    android.util.Log.d("AddressManager", "✅ Has address: ${result.data.hasAddress}")
                    ApiResult.Success(result.data.hasAddress)
                }
                is ApiResult.Error -> result
                is ApiResult.Loading -> ApiResult.Loading
            }
        } catch (e: Exception) {
            android.util.Log.e("AddressManager", "💥 Error checking address: ${e.message}", e)
            ApiResult.Error("Lỗi khi kiểm tra địa chỉ: ${e.message}", exception = e)
        }
    }
    
    /**
     * Save address (create or update)
     * Note: API automatically handles create vs update
     */
    suspend fun saveAddress(address: Address): ApiResult<Address> {
        return try {
            android.util.Log.d("AddressManager", "💾 Saving address...")
            
            val addressDTO = address.toAddressDTO()
            val result = AddressApiRepository.createOrUpdateAddress(addressDTO)
            
            when (result) {
                is ApiResult.Success -> {
                    val savedAddress = result.data.toAddress()
                    _addresses.value = listOf(savedAddress)
                    _selectedAddress.value = savedAddress
                    android.util.Log.d("AddressManager", "✅ Address saved")
                    ApiResult.Success(savedAddress)
                }
                is ApiResult.Error -> result
                is ApiResult.Loading -> ApiResult.Loading
            }
        } catch (e: Exception) {
            android.util.Log.e("AddressManager", "💥 Error saving address: ${e.message}", e)
            ApiResult.Error("Lỗi khi lưu địa chỉ: ${e.message}", exception = e)
        }
    }
    
    /**
     * Delete address
     */
    suspend fun deleteAddress(): ApiResult<String> {
        return try {
            android.util.Log.d("AddressManager", "🗑️ Deleting address...")
            
            val result = AddressApiRepository.deleteAddress()
            
            when (result) {
                is ApiResult.Success -> {
                    _addresses.value = emptyList()
                    _selectedAddress.value = null
                    android.util.Log.d("AddressManager", "✅ Address deleted")
                    result
                }
                is ApiResult.Error -> result
                is ApiResult.Loading -> ApiResult.Loading
            }
        } catch (e: Exception) {
            android.util.Log.e("AddressManager", "💥 Error deleting address: ${e.message}", e)
            ApiResult.Error("Lỗi khi xóa địa chỉ: ${e.message}", exception = e)
        }
    }
    
    /**
     * Select address for checkout
     */
    fun selectAddress(address: Address) {
        _selectedAddress.value = address
    }
    
    /**
     * Get default address (the only one)
     */
    fun getDefaultAddress(): Address? {
        return _addresses.value.firstOrNull()
    }
    
    // ============================================
    // Extension functions for conversion
    // ============================================
    
    /**
     * Convert AddressResponse to Address model
     */
    private fun AddressResponse.toAddress(): Address {
        return Address(
            id = id,
            recipientName = suggestedName ?: "",
            phoneNumber = "",  // API doesn't provide phone number
            province = province,
            district = "",  // API doesn't have district
            ward = ward,
            street = homeAddress,
            isDefault = true  // Always default since there's only one
        )
    }
    
    /**
     * Convert Address to AddressDTO
     */
    private fun Address.toAddressDTO(): AddressDTO {
        return AddressDTO(
            province = province,
            ward = ward,
            homeAddress = street,
            suggestedName = recipientName.ifEmpty { null }
        )
    }
}

