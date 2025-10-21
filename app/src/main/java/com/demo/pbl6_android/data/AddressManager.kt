package com.demo.pbl6_android.data

import com.demo.pbl6_android.data.model.Address
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object AddressManager {
    
    private val _addresses = MutableStateFlow<List<Address>>(getSampleAddresses())
    val addresses: StateFlow<List<Address>> = _addresses
    
    private val _selectedAddress = MutableStateFlow<Address?>(null)
    val selectedAddress: StateFlow<Address?> = _selectedAddress
    
    private fun getSampleAddresses(): List<Address> {
        return listOf(
            Address(
                id = "addr_1",
                recipientName = "Nguyễn Văn A",
                phoneNumber = "0123456789",
                province = "Thành phố Hồ Chí Minh",
                district = "Quận 1",
                ward = "Phường Bến Nghé",
                street = "123 Đường Lê Lợi",
                isDefault = true
            ),
            Address(
                id = "addr_2",
                recipientName = "Trần Thị B",
                phoneNumber = "0987654321",
                province = "Thành phố Hồ Chí Minh",
                district = "Quận 3",
                ward = "Phường 7",
                street = "456 Đường Nguyễn Đình Chiểu",
                isDefault = false
            ),
            Address(
                id = "addr_3",
                recipientName = "Lê Văn C",
                phoneNumber = "0369852147",
                province = "Sóc Trắng",
                district = "Huyện Long Phú",
                ward = "Thị Trấn Đại Ngãi",
                street = "789 Đường Trần Hưng Đạo",
                isDefault = false
            )
        )
    }
    
    fun selectAddress(address: Address) {
        _selectedAddress.value = address
    }
    
    fun addAddress(address: Address) {
        val currentList = _addresses.value.toMutableList()
        val newAddress = if (address.isDefault) {
            currentList.forEach { it.copy(isDefault = false) }
            address
        } else {
            address
        }
        currentList.add(newAddress)
        _addresses.value = currentList
    }
    
    fun updateAddress(updatedAddress: Address) {
        val currentList = _addresses.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == updatedAddress.id }
        if (index != -1) {
            if (updatedAddress.isDefault) {
                currentList.forEachIndexed { i, addr ->
                    if (i != index) {
                        currentList[i] = addr.copy(isDefault = false)
                    }
                }
            }
            currentList[index] = updatedAddress
            _addresses.value = currentList
        }
    }
    
    fun deleteAddress(addressId: String) {
        val currentList = _addresses.value.toMutableList()
        currentList.removeAll { it.id == addressId }
        _addresses.value = currentList
    }
    
    fun getDefaultAddress(): Address? {
        return _addresses.value.find { it.isDefault }
    }
}

