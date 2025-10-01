package com.demo.pbl6_android.ui.order

import com.demo.pbl6_android.ui.cart.model.CartProduct
import com.demo.pbl6_android.ui.cart.model.CartShop

object OrderData {
    var selectedShops: List<CartShop> = emptyList()
    
    fun setOrderData(shops: List<CartShop>) {
        selectedShops = shops
    }
    
    fun clearOrderData() {
        selectedShops = emptyList()
    }
}

