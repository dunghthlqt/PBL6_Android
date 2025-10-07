package com.demo.pbl6_android.data

import com.demo.pbl6_android.data.model.Category

object CategoryRepository {

    private val categories = listOf(
        Category(
            id = "all",
            name = "Tất cả",
            icon = "🏠",
            productCount = 0,
            isSelected = true
        ),
        Category(
            id = "fashion",
            name = "Thời trang",
            icon = "👕",
            productCount = 0
        ),
        Category(
            id = "electronics",
            name = "Điện tử",
            icon = "📱",
            productCount = 0
        ),
        Category(
            id = "home",
            name = "Gia dụng",
            icon = "🏠",
            productCount = 0
        ),
        Category(
            id = "beauty",
            name = "Làm đẹp",
            icon = "💄",
            productCount = 0
        ),
        Category(
            id = "sports",
            name = "Thể thao",
            icon = "⚽",
            productCount = 0
        ),
        Category(
            id = "books",
            name = "Sách",
            icon = "📚",
            productCount = 0
        ),
        Category(
            id = "toys",
            name = "Đồ chơi",
            icon = "🧸",
            productCount = 0
        ),
        Category(
            id = "food",
            name = "Thực phẩm",
            icon = "🍎",
            productCount = 0
        )
    )

    fun getAllCategories(): List<Category> {
        return categories.toList()
    }

    fun getCategoryById(categoryId: String): Category? {
        return categories.find { it.id == categoryId }
    }
}

