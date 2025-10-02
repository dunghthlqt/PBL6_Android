package com.demo.pbl6_android.data

import com.demo.pbl6_android.data.model.Product
import com.demo.pbl6_android.ui.cart.model.CartProduct
import com.demo.pbl6_android.ui.cart.model.CartShop
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object CartManager {
    
    private val _cartShops = MutableStateFlow<List<CartShop>>(emptyList())
    val cartShops: StateFlow<List<CartShop>> = _cartShops.asStateFlow()
    
    private val _cartItemCount = MutableStateFlow(0)
    val cartItemCount: StateFlow<Int> = _cartItemCount.asStateFlow()
    
    fun addToCart(product: Product, selectedColor: String, selectedSize: String, quantity: Int) {
        val shops = _cartShops.value.map { shop ->
            CartShop(
                id = shop.id,
                name = shop.name,
                products = shop.products.toMutableList(),
                isSelected = shop.isSelected
            )
        }.toMutableList()
        
        val cartProduct = CartProduct(
            id = "${product.id}_${selectedColor}_${selectedSize}",
            name = product.name,
            color = selectedColor,
            size = selectedSize,
            currentPrice = product.currentPrice,
            originalPrice = product.originalPrice,
            quantity = quantity,
            imageUrl = product.images.firstOrNull() ?: "",
            isSelected = false
        )
        
        val existingShop = shops.find { it.id == product.shopId }
        
        if (existingShop != null) {
            val existingProduct = existingShop.products.find { it.id == cartProduct.id }
            
            if (existingProduct != null) {
                existingProduct.quantity += quantity
            } else {
                existingShop.products.add(cartProduct)
            }
        } else {
            shops.add(
                CartShop(
                    id = product.shopId,
                    name = product.shopName,
                    products = mutableListOf(cartProduct),
                    isSelected = false
                )
            )
        }
        
        _cartShops.value = shops
        updateCartItemCount()
    }
    
    fun removeFromCart(productId: String) {
        val shops = _cartShops.value.map { shop ->
            CartShop(
                id = shop.id,
                name = shop.name,
                products = shop.products.filter { it.id != productId }.toMutableList(),
                isSelected = shop.isSelected
            )
        }.filter { it.products.isNotEmpty() }
        
        _cartShops.value = shops
        updateCartItemCount()
    }
    
    fun updateQuantity(productId: String, newQuantity: Int) {
        val shops = _cartShops.value.map { shop ->
            CartShop(
                id = shop.id,
                name = shop.name,
                products = shop.products.map { product ->
                    if (product.id == productId) {
                        product.copy(quantity = newQuantity)
                    } else {
                        product
                    }
                }.toMutableList(),
                isSelected = shop.isSelected
            )
        }
        
        _cartShops.value = shops
        updateCartItemCount()
    }
    
    fun clearCart() {
        _cartShops.value = emptyList()
        _cartItemCount.value = 0
    }
    
    fun getCartShops(): List<CartShop> {
        return _cartShops.value
    }
    
    private fun updateCartItemCount() {
        var count = 0
        _cartShops.value.forEach { shop ->
            count += shop.products.size
        }
        _cartItemCount.value = count
    }
}

