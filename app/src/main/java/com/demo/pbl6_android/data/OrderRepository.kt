package com.demo.pbl6_android.data

import com.demo.pbl6_android.data.model.Order
import com.demo.pbl6_android.data.model.OrderItem
import com.demo.pbl6_android.data.model.OrderStatus
import java.util.concurrent.TimeUnit

object OrderRepository {
    
    private val orders = mutableListOf<Order>()
    
    init {
        loadSampleOrders()
    }
    
    fun getAllOrders(): List<Order> {
        return orders.sortedByDescending { it.orderDate }
    }
    
    fun getOrdersByStatus(status: OrderStatus): List<Order> {
        return orders.filter { it.status == status }.sortedByDescending { it.orderDate }
    }
    
    fun getOrderById(orderId: String): Order? {
        return orders.find { it.orderId == orderId }
    }
    
    fun getOrderCountByStatus(status: OrderStatus): Int {
        return orders.count { it.status == status }
    }
    
    private fun loadSampleOrders() {
        val now = System.currentTimeMillis()
        val oneDayAgo = now - TimeUnit.DAYS.toMillis(1)
        val twoDaysAgo = now - TimeUnit.DAYS.toMillis(2)
        val threeDaysAgo = now - TimeUnit.DAYS.toMillis(3)
        val fiveDaysAgo = now - TimeUnit.DAYS.toMillis(5)
        val sevenDaysAgo = now - TimeUnit.DAYS.toMillis(7)
        val tenDaysAgo = now - TimeUnit.DAYS.toMillis(10)
        
        orders.add(
            Order(
                orderId = "order_001",
                orderCode = "DH001",
                orderDate = oneDayAgo,
                status = OrderStatus.PENDING_PAYMENT,
                items = listOf(
                    OrderItem(
                        productId = "laptop_001",
                        productName = "MacBook Pro 14 inch M3 Pro 2024",
                        productImage = "",
                        quantity = 1,
                        price = 49990000,
                        color = "Space Gray",
                        size = "14 inch"
                    )
                ),
                totalAmount = 49990000,
                shopId = "shop_tech_001",
                shopName = "Tech Store Official",
                shippingAddress = "123 Đường ABC, Phường XYZ, Quận 1, TP.HCM",
                paymentMethod = "COD"
            )
        )
        
        orders.add(
            Order(
                orderId = "order_002",
                orderCode = "DH002",
                orderDate = twoDaysAgo,
                status = OrderStatus.PENDING_PAYMENT,
                items = listOf(
                    OrderItem(
                        productId = "phone_001",
                        productName = "iPhone 15 Pro Max 256GB",
                        productImage = "",
                        quantity = 1,
                        price = 31990000,
                        color = "Natural Titanium",
                        size = "256GB"
                    )
                ),
                totalAmount = 31990000,
                shopId = "shop_tech_001",
                shopName = "Tech Store Official",
                shippingAddress = "123 Đường ABC, Phường XYZ, Quận 1, TP.HCM",
                paymentMethod = "Bank Transfer"
            )
        )
        
        orders.add(
            Order(
                orderId = "order_003",
                orderCode = "DH003",
                orderDate = threeDaysAgo,
                status = OrderStatus.PENDING_PICKUP,
                items = listOf(
                    OrderItem(
                        productId = "laptop_002",
                        productName = "Dell XPS 15 9530",
                        productImage = "",
                        quantity = 1,
                        price = 42990000,
                        color = "Platinum Silver",
                        size = "15.6 inch"
                    )
                ),
                totalAmount = 42990000,
                shopId = "shop_tech_001",
                shopName = "Tech Store Official",
                shippingAddress = "123 Đường ABC, Phường XYZ, Quận 1, TP.HCM",
                paymentMethod = "COD"
            )
        )
        
        orders.add(
            Order(
                orderId = "order_004",
                orderCode = "DH004",
                orderDate = threeDaysAgo,
                status = OrderStatus.SHIPPING,
                items = listOf(
                    OrderItem(
                        productId = "phone_002",
                        productName = "Samsung Galaxy S24 Ultra",
                        productImage = "",
                        quantity = 1,
                        price = 27990000,
                        color = "Titanium Black",
                        size = "256GB"
                    )
                ),
                totalAmount = 27990000,
                shopId = "shop_tech_001",
                shopName = "Tech Store Official",
                shippingAddress = "123 Đường ABC, Phường XYZ, Quận 1, TP.HCM",
                paymentMethod = "Bank Transfer"
            )
        )
        
        orders.add(
            Order(
                orderId = "order_005",
                orderCode = "DH005",
                orderDate = fiveDaysAgo,
                status = OrderStatus.SHIPPING,
                items = listOf(
                    OrderItem(
                        productId = "laptop_003",
                        productName = "ASUS ROG Strix G16",
                        productImage = "",
                        quantity = 1,
                        price = 35990000,
                        color = "Eclipse Gray",
                        size = "16 inch"
                    )
                ),
                totalAmount = 35990000,
                shopId = "shop_tech_002",
                shopName = "Gaming Gear Store",
                shippingAddress = "123 Đường ABC, Phường XYZ, Quận 1, TP.HCM",
                paymentMethod = "COD"
            )
        )
        
        orders.add(
            Order(
                orderId = "order_006",
                orderCode = "DH006",
                orderDate = sevenDaysAgo,
                status = OrderStatus.DELIVERED,
                items = listOf(
                    OrderItem(
                        productId = "phone_003",
                        productName = "Xiaomi 14 Pro 12GB 256GB",
                        productImage = "",
                        quantity = 1,
                        price = 18990000,
                        color = "Black",
                        size = "256GB"
                    )
                ),
                totalAmount = 18990000,
                shopId = "shop_tech_003",
                shopName = "Xiaomi Official Store",
                shippingAddress = "123 Đường ABC, Phường XYZ, Quận 1, TP.HCM",
                paymentMethod = "COD",
                receivedDate = sevenDaysAgo - TimeUnit.HOURS.toMillis(2)
            )
        )
        
        orders.add(
            Order(
                orderId = "order_007",
                orderCode = "DH007",
                orderDate = sevenDaysAgo,
                status = OrderStatus.DELIVERED,
                items = listOf(
                    OrderItem(
                        productId = "phone_004",
                        productName = "OPPO Find N3 Flip 5G",
                        productImage = "",
                        quantity = 1,
                        price = 19990000,
                        color = "Astral Black",
                        size = "256GB"
                    )
                ),
                totalAmount = 19990000,
                shopId = "shop_tech_004",
                shopName = "OPPO Official Store",
                shippingAddress = "123 Đường ABC, Phường XYZ, Quận 1, TP.HCM",
                paymentMethod = "Bank Transfer",
                receivedDate = sevenDaysAgo - TimeUnit.HOURS.toMillis(3)
            )
        )
        
        orders.add(
            Order(
                orderId = "order_008",
                orderCode = "DH008",
                orderDate = tenDaysAgo,
                status = OrderStatus.RETURN_REFUND,
                items = listOf(
                    OrderItem(
                        productId = "laptop_001",
                        productName = "MacBook Pro 14 inch M3 Pro",
                        productImage = "",
                        quantity = 1,
                        price = 49990000,
                        color = "Silver",
                        size = "14 inch"
                    )
                ),
                totalAmount = 49990000,
                shopId = "shop_tech_001",
                shopName = "Tech Store Official",
                shippingAddress = "123 Đường ABC, Phường XYZ, Quận 1, TP.HCM",
                paymentMethod = "Bank Transfer"
            )
        )
        
        orders.add(
            Order(
                orderId = "order_009",
                orderCode = "DH009",
                orderDate = tenDaysAgo,
                status = OrderStatus.CANCELLED,
                items = listOf(
                    OrderItem(
                        productId = "phone_001",
                        productName = "iPhone 15 Pro Max 256GB",
                        productImage = "",
                        quantity = 1,
                        price = 31990000,
                        color = "Blue Titanium",
                        size = "256GB"
                    )
                ),
                totalAmount = 31990000,
                shopId = "shop_tech_001",
                shopName = "Tech Store Official",
                shippingAddress = "123 Đường ABC, Phường XYZ, Quận 1, TP.HCM",
                paymentMethod = "COD"
            )
        )
    }
}

