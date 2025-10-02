package com.demo.pbl6_android.data

import com.demo.pbl6_android.data.model.Product
import com.demo.pbl6_android.data.model.ProductColor

object ProductRepository {
    
    private val products = mutableListOf<Product>()
    
    init {
        loadSampleProducts()
    }
    
    fun getAllProducts(): List<Product> {
        return products.toList()
    }
    
    fun getProductById(productId: String): Product? {
        return products.find { it.id == productId }
    }
    
    fun getProductsByCategory(category: String): List<Product> {
        return products.filter { it.category == category }
    }
    
    fun getRelatedProducts(productId: String, limit: Int = 4): List<Product> {
        val product = getProductById(productId) ?: return emptyList()
        return products
            .filter { it.id != productId && it.category == product.category }
            .take(limit)
    }
    
    private fun loadSampleProducts() {
        products.clear()
        
        // Laptop products
        products.add(
            Product(
                id = "laptop_001",
                name = "MacBook Pro 14 inch M3 Pro 2024",
                description = "MacBook Pro 14 inch với chip M3 Pro mạnh mẽ, màn hình Liquid Retina XDR tuyệt đẹp, RAM 18GB, SSD 512GB. Phù hợp cho công việc đồ họa, lập trình và sáng tạo nội dung chuyên nghiệp.",
                brand = "Apple",
                material = "Nhôm nguyên khối",
                origin = "Trung Quốc",
                sizes = listOf("14 inch"),
                colors = listOf(
                    ProductColor("Space Gray", "#5F5F5F"),
                    ProductColor("Silver", "#E3E4E5")
                ),
                currentPrice = 49990000,
                originalPrice = 52990000,
                discount = 6,
                rating = 4.8f,
                reviewCount = 256,
                soldCount = 1523,
                images = listOf("", "", ""),
                category = "Laptop",
                shopId = "shop_tech_001",
                shopName = "Tech Store Official",
                specifications = mapOf(
                    "CPU" to "Apple M3 Pro 11-core",
                    "RAM" to "18GB Unified Memory",
                    "Ổ cứng" to "512GB SSD",
                    "Màn hình" to "14.2 inch Liquid Retina XDR",
                    "Card đồ họa" to "14-core GPU",
                    "Pin" to "70Wh, sử dụng đến 17 giờ"
                )
            )
        )
        
        products.add(
            Product(
                id = "laptop_002",
                name = "Dell XPS 15 9530 Intel Core i7 Gen 13",
                description = "Dell XPS 15 với thiết kế sang trọng, màn hình OLED 3.5K tuyệt đẹp, hiệu năng mạnh mẽ với Intel Core i7 Gen 13, RAM 16GB, SSD 512GB. Lý tưởng cho doanh nhân và người sáng tạo.",
                brand = "Dell",
                material = "Nhôm và Carbon Fiber",
                origin = "Trung Quốc",
                sizes = listOf("15.6 inch"),
                colors = listOf(
                    ProductColor("Platinum Silver", "#C0C0C0"),
                    ProductColor("Graphite", "#3C3C3C")
                ),
                currentPrice = 42990000,
                originalPrice = 47990000,
                discount = 10,
                rating = 4.7f,
                reviewCount = 189,
                soldCount = 876,
                images = listOf("", "", ""),
                category = "Laptop",
                shopId = "shop_tech_001",
                shopName = "Tech Store Official",
                specifications = mapOf(
                    "CPU" to "Intel Core i7-13700H",
                    "RAM" to "16GB DDR5",
                    "Ổ cứng" to "512GB NVMe SSD",
                    "Màn hình" to "15.6 inch OLED 3.5K Touch",
                    "Card đồ họa" to "NVIDIA RTX 4050 6GB",
                    "Pin" to "86Wh"
                )
            )
        )
        
        products.add(
            Product(
                id = "laptop_003",
                name = "ASUS ROG Strix G16 Gaming i7 Gen 13",
                description = "Laptop gaming ASUS ROG Strix G16 với hiệu năng khủng, màn hình 165Hz mượt mà, hệ thống tản nhiệt vượt trội. Chinh phục mọi tựa game AAA và công việc đồ họa nặng.",
                brand = "ASUS",
                material = "Nhựa ABS cao cấp",
                origin = "Trung Quốc",
                sizes = listOf("16 inch"),
                colors = listOf(
                    ProductColor("Eclipse Gray", "#2D2D2D"),
                    ProductColor("Volt Green", "#39FF14")
                ),
                currentPrice = 35990000,
                originalPrice = 39990000,
                discount = 10,
                rating = 4.6f,
                reviewCount = 342,
                soldCount = 2156,
                images = listOf("", "", ""),
                category = "Laptop",
                shopId = "shop_tech_002",
                shopName = "Gaming Gear Store",
                specifications = mapOf(
                    "CPU" to "Intel Core i7-13650HX",
                    "RAM" to "16GB DDR5",
                    "Ổ cứng" to "512GB NVMe SSD",
                    "Màn hình" to "16 inch FHD 165Hz",
                    "Card đồ họa" to "NVIDIA RTX 4060 8GB",
                    "Pin" to "90Wh"
                )
            )
        )
        
        // Smartphone products
        products.add(
            Product(
                id = "phone_001",
                name = "iPhone 15 Pro Max 256GB",
                description = "iPhone 15 Pro Max với chip A17 Pro mạnh mẽ nhất, camera 48MP chất lượng đỉnh cao, khung Titanium bền bỉ, Action Button mới. Đỉnh cao công nghệ trong tầm tay bạn.",
                brand = "Apple",
                material = "Khung Titanium, mặt kính Ceramic Shield",
                origin = "Trung Quốc",
                sizes = listOf("6.7 inch"),
                colors = listOf(
                    ProductColor("Natural Titanium", "#8B8680"),
                    ProductColor("Blue Titanium", "#5F6F7A"),
                    ProductColor("White Titanium", "#E5E5E0"),
                    ProductColor("Black Titanium", "#3C3C3C")
                ),
                currentPrice = 31990000,
                originalPrice = 34990000,
                discount = 9,
                rating = 4.9f,
                reviewCount = 1247,
                soldCount = 5632,
                images = listOf("", "", ""),
                category = "Smartphone",
                shopId = "shop_tech_001",
                shopName = "Tech Store Official",
                specifications = mapOf(
                    "Chip" to "Apple A17 Pro 6-core",
                    "RAM" to "8GB",
                    "Bộ nhớ" to "256GB",
                    "Màn hình" to "6.7 inch Super Retina XDR OLED",
                    "Camera" to "48MP Main + 12MP Ultra Wide + 12MP Telephoto",
                    "Pin" to "4441mAh, sạc nhanh 27W"
                )
            )
        )
        
        products.add(
            Product(
                id = "phone_002",
                name = "Samsung Galaxy S24 Ultra 12GB 256GB",
                description = "Samsung Galaxy S24 Ultra với S Pen tích hợp, màn hình Dynamic AMOLED 2X 6.8 inch, camera 200MP siêu nét, chip Snapdragon 8 Gen 3 for Galaxy mạnh mẽ. Trải nghiệm flagship đỉnh cao.",
                brand = "Samsung",
                material = "Khung Titanium, mặt kính Gorilla Armor",
                origin = "Việt Nam",
                sizes = listOf("6.8 inch"),
                colors = listOf(
                    ProductColor("Titanium Black", "#1A1A1A"),
                    ProductColor("Titanium Gray", "#7D7D7D"),
                    ProductColor("Titanium Violet", "#8B7BA8"),
                    ProductColor("Titanium Yellow", "#F0E68C")
                ),
                currentPrice = 27990000,
                originalPrice = 30990000,
                discount = 10,
                rating = 4.8f,
                reviewCount = 892,
                soldCount = 3421,
                images = listOf("", "", ""),
                category = "Smartphone",
                shopId = "shop_tech_001",
                shopName = "Tech Store Official",
                specifications = mapOf(
                    "Chip" to "Snapdragon 8 Gen 3 for Galaxy",
                    "RAM" to "12GB",
                    "Bộ nhớ" to "256GB",
                    "Màn hình" to "6.8 inch Dynamic AMOLED 2X 120Hz",
                    "Camera" to "200MP Main + 50MP Periscope + 12MP Ultra Wide + 10MP Telephoto",
                    "Pin" to "5000mAh, sạc nhanh 45W"
                )
            )
        )
        
        products.add(
            Product(
                id = "phone_003",
                name = "Xiaomi 14 Pro 12GB 256GB",
                description = "Xiaomi 14 Pro với camera Leica đẳng cấp, màn hình LTPO AMOLED 120Hz mượt mà, chip Snapdragon 8 Gen 3 cực mạnh, sạc nhanh 120W. Smartphone cao cấp với giá hợp lý.",
                brand = "Xiaomi",
                material = "Khung kim loại, mặt kính Gorilla Glass Victus",
                origin = "Trung Quốc",
                sizes = listOf("6.73 inch"),
                colors = listOf(
                    ProductColor("Black", "#000000"),
                    ProductColor("White", "#FFFFFF"),
                    ProductColor("Green", "#2D5016")
                ),
                currentPrice = 18990000,
                originalPrice = 21990000,
                discount = 14,
                rating = 4.7f,
                reviewCount = 567,
                soldCount = 2890,
                images = listOf("", "", ""),
                category = "Smartphone",
                shopId = "shop_tech_003",
                shopName = "Xiaomi Official Store",
                specifications = mapOf(
                    "Chip" to "Snapdragon 8 Gen 3",
                    "RAM" to "12GB",
                    "Bộ nhớ" to "256GB",
                    "Màn hình" to "6.73 inch LTPO AMOLED 120Hz",
                    "Camera" to "50MP Leica Main + 50MP Ultra Wide + 50MP Telephoto",
                    "Pin" to "4880mAh, sạc nhanh 120W"
                )
            )
        )
        
        products.add(
            Product(
                id = "phone_004",
                name = "OPPO Find N3 Flip 5G 12GB 256GB",
                description = "Điện thoại gập OPPO Find N3 Flip với thiết kế thời trang, màn hình phụ lớn 3.26 inch, camera Hasselblad chất lượng cao, chip Dimensity 9200. Sự kết hợp hoàn hảo giữa phong cách và công nghệ.",
                brand = "OPPO",
                material = "Khung kim loại, mặt kính",
                origin = "Trung Quốc",
                sizes = listOf("6.8 inch (펼쳤을 때)"),
                colors = listOf(
                    ProductColor("Astral Black", "#1C1C1C"),
                    ProductColor("Moonlight Muse", "#E8D7C8"),
                    ProductColor("Cream Gold", "#F5DEB3")
                ),
                currentPrice = 19990000,
                originalPrice = 22990000,
                discount = 13,
                rating = 4.6f,
                reviewCount = 234,
                soldCount = 1123,
                images = listOf("", "", ""),
                category = "Smartphone",
                shopId = "shop_tech_004",
                shopName = "OPPO Official Store",
                specifications = mapOf(
                    "Chip" to "MediaTek Dimensity 9200",
                    "RAM" to "12GB",
                    "Bộ nhớ" to "256GB",
                    "Màn hình chính" to "6.8 inch AMOLED 120Hz",
                    "Màn hình phụ" to "3.26 inch AMOLED",
                    "Camera" to "50MP Hasselblad Main + 48MP Ultra Wide + 32MP Telephoto",
                    "Pin" to "4300mAh, sạc nhanh 44W"
                )
            )
        )
    }
}

