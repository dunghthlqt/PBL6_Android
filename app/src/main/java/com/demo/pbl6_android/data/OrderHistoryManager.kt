package com.demo.pbl6_android.data

import com.demo.pbl6_android.data.model.Order
import com.demo.pbl6_android.data.model.OrderStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object OrderHistoryManager {
    
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders
    
    fun addOrder(order: Order) {
        val currentOrders = _orders.value.toMutableList()
        currentOrders.add(0, order) // Add to beginning
        _orders.value = currentOrders
    }
    
    fun getOrderById(orderId: String): Order? {
        return _orders.value.find { it.orderId == orderId }
    }
    
    fun getOrdersByStatus(status: OrderStatus): List<Order> {
        return _orders.value.filter { it.status == status }
    }
    
    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        val currentOrders = _orders.value.toMutableList()
        val index = currentOrders.indexOfFirst { it.orderId == orderId }
        if (index != -1) {
            currentOrders[index] = currentOrders[index].copy(status = newStatus)
            _orders.value = currentOrders
        }
    }
}

