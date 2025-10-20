package com.demo.pbl6_android.data

import com.demo.pbl6_android.R
import com.demo.pbl6_android.data.model.Review

object ReviewRepository {
    
    fun getReviewsByProduct(productId: String): List<Review> {
        // Sample review data based on provided image
        return listOf(
            Review(
                id = "1",
                userName = "e*****3",
                rating = 5f,
                comment = "Ghế có thiết kế đặc biệt ở phần lưng, hỗ trợ lưng rất tốt và có thể điều chỉnh. Thực sự rất thoải mái, ngồi cả ngày cũng có thể giảm đau lưng. Rất đáng được giới thiệu!",
                images = listOf(
                    R.drawable.ic_placeholder,
                    R.drawable.ic_placeholder,
                    R.drawable.ic_placeholder,
                    R.drawable.ic_placeholder
                ),
                date = "09/10/2024",
                helpfulCount = 56
            ),
            Review(
                id = "2",
                userName = "n*****7",
                rating = 4f,
                comment = "Sản phẩm đẹp, chất lượng tốt. Đóng gói cẩn thận. Shop phục vụ nhiệt tình. Mình rất hài lòng với sản phẩm này!",
                images = listOf(
                    R.drawable.ic_placeholder,
                    R.drawable.ic_placeholder
                ),
                date = "15/09/2024",
                helpfulCount = 32
            ),
            Review(
                id = "3",
                userName = "t*****2",
                rating = 5f,
                comment = "Chất lượng quá tuyệt vời, giá cả hợp lý. Giao hàng nhanh chóng. Rất đáng để mua. Sẽ ủng hộ shop lâu dài!",
                images = listOf(),
                date = "20/09/2024",
                helpfulCount = 18
            ),
            Review(
                id = "4",
                userName = "m*****5",
                rating = 3f,
                comment = "Sản phẩm tạm ổn, nhưng màu sắc hơi khác so với hình. Vẫn dùng được.",
                images = listOf(
                    R.drawable.ic_placeholder
                ),
                date = "12/09/2024",
                helpfulCount = 8
            ),
            Review(
                id = "5",
                userName = "h*****9",
                rating = 5f,
                comment = "Chất lượng xuất sắc! Đóng gói rất cẩn thận, giao hàng nhanh. Shop tư vấn nhiệt tình. 10 điểm!",
                images = listOf(
                    R.drawable.ic_placeholder,
                    R.drawable.ic_placeholder,
                    R.drawable.ic_placeholder
                ),
                date = "05/10/2024",
                helpfulCount = 45
            ),
            Review(
                id = "6",
                userName = "l*****4",
                rating = 4f,
                comment = "Sản phẩm tốt, đúng như mô tả. Giao hàng hơi lâu nhưng vẫn chấp nhận được.",
                images = listOf(),
                date = "18/09/2024",
                helpfulCount = 12
            )
        )
    }
    
    fun getReviewsWithImages(productId: String): List<Review> {
        return getReviewsByProduct(productId).filter { it.images.isNotEmpty() }
    }
    
    fun getReviewsByRating(productId: String, rating: Int): List<Review> {
        return getReviewsByProduct(productId).filter { it.rating.toInt() == rating }
    }
    
    fun getAverageRating(productId: String): Float {
        val reviews = getReviewsByProduct(productId)
        return if (reviews.isEmpty()) 0f else reviews.map { it.rating }.average().toFloat()
    }
    
    fun getTotalReviewsCount(productId: String): Int {
        return getReviewsByProduct(productId).size
    }
    
    fun getRatingDistribution(productId: String): Map<Int, Int> {
        val reviews = getReviewsByProduct(productId)
        return (1..5).associateWith { rating ->
            reviews.count { it.rating.toInt() == rating }
        }
    }
}

