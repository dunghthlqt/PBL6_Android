package com.demo.pbl6_android.data.api

import com.demo.pbl6_android.data.api.model.AddToCartRequest
import com.demo.pbl6_android.data.api.model.AddressCheckResponse
import com.demo.pbl6_android.data.api.model.AddressDTO
import com.demo.pbl6_android.data.api.model.AddressResponse
import com.demo.pbl6_android.data.api.model.ApiResponse
import com.demo.pbl6_android.data.api.model.BrandResponse
import com.demo.pbl6_android.data.api.model.CartCountResponse
import com.demo.pbl6_android.data.api.model.CartResponse
import com.demo.pbl6_android.data.api.model.CategoryResponse
import com.demo.pbl6_android.data.api.model.CheckoutResponse
import com.demo.pbl6_android.data.api.model.DiscountCalculationResponse
import com.demo.pbl6_android.data.api.model.LoginRequest
import com.demo.pbl6_android.data.api.model.LoginResponse
import com.demo.pbl6_android.data.api.model.OrderDTO
import com.demo.pbl6_android.data.api.model.OrderResponse
import com.demo.pbl6_android.data.api.model.PageResponse
import com.demo.pbl6_android.data.api.model.ProductResponse
import com.demo.pbl6_android.data.api.model.ProductVariantResponse
import com.demo.pbl6_android.data.api.model.PromotionResponse
import com.demo.pbl6_android.data.api.model.PromotionValidationResponse
import com.demo.pbl6_android.data.api.model.RatingStatsResponse
import com.demo.pbl6_android.data.api.model.ReviewDTO
import com.demo.pbl6_android.data.api.model.ReviewResponse
import com.demo.pbl6_android.data.api.model.StoreResponse
import com.demo.pbl6_android.data.api.model.UpdateQuantityRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    
    // ============================================
    // USER MANAGEMENT APIs
    // ============================================
    
    @POST("api/v1/users/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
    
    // ============================================
    // PRODUCT APIs
    // ============================================
    
    /**
     * Search products by name
     * GET /api/v1/products
     */
    @GET("api/v1/products")
    suspend fun searchProducts(
        @Query("name") name: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("sortDir") sortDir: String = "desc"
    ): Response<ApiResponse<PageResponse<ProductResponse>>>
    
    /**
     * Get product by ID
     * GET /api/v1/products/{id}
     */
    @GET("api/v1/products/{id}")
    suspend fun getProductById(
        @Path("id") productId: String
    ): Response<ApiResponse<ProductResponse>>
    
    /**
     * Get products by category
     * GET /api/v1/products/category/{name}
     */
    @GET("api/v1/products/category/{name}")
    suspend fun getProductsByCategory(
        @Path("name") categoryName: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("sortDir") sortDir: String = "desc"
    ): Response<ApiResponse<PageResponse<ProductResponse>>>
    
    /**
     * Get products by category and brand
     * GET /api/v1/products/category/{category}/brand/{brand}
     */
    @GET("api/v1/products/category/{category}/brand/{brand}")
    suspend fun getProductsByCategoryAndBrand(
        @Path("category") category: String,
        @Path("brand") brand: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("sortDir") sortDir: String = "desc"
    ): Response<ApiResponse<PageResponse<ProductResponse>>>
    
    // ============================================
    // PRODUCT VARIANT APIs
    // ============================================
    
    /**
     * Get product variant by ID
     * GET /api/v1/product-variants/{id}
     */
    @GET("api/v1/product-variants/{id}")
    suspend fun getProductVariantById(
        @Path("id") variantId: String
    ): Response<ApiResponse<ProductVariantResponse>>
    
    /**
     * Get all variants of a product
     * GET /api/v1/product-variants/product/{productId}
     */
    @GET("api/v1/product-variants/product/{productId}")
    suspend fun getProductVariantsByProduct(
        @Path("productId") productId: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("sortDir") sortDir: String = "desc"
    ): Response<ApiResponse<PageResponse<ProductVariantResponse>>>
    
    /**
     * Get latest product variants
     * GET /api/v1/product-variants/latest
     */
    @GET("api/v1/product-variants/latest")
    suspend fun getLatestProductVariants(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("sortDir") sortDir: String = "desc"
    ): Response<ApiResponse<PageResponse<ProductVariantResponse>>>
    
    /**
     * Get product variants by store
     * GET /api/v1/product-variants/store/{storeId}
     */
    @GET("api/v1/product-variants/store/{storeId}")
    suspend fun getProductVariantsByStore(
        @Path("storeId") storeId: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("sortDir") sortDir: String = "desc"
    ): Response<ApiResponse<PageResponse<ProductVariantResponse>>>
    
    /**
     * Get product variants by category
     * GET /api/v1/product-variants/category/{category}
     */
    @GET("api/v1/product-variants/category/{category}")
    suspend fun getProductVariantsByCategory(
        @Path("category") category: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("sortDir") sortDir: String = "desc"
    ): Response<ApiResponse<PageResponse<ProductVariantResponse>>>
    
    /**
     * Get product variants by category and brand
     * GET /api/v1/product-variants/category/{category}/brand/{brand}
     */
    @GET("api/v1/product-variants/category/{category}/brand/{brand}")
    suspend fun getProductVariantsByCategoryAndBrand(
        @Path("category") category: String,
        @Path("brand") brand: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("sortDir") sortDir: String = "desc"
    ): Response<ApiResponse<PageResponse<ProductVariantResponse>>>
    
    // ============================================
    // CATEGORY APIs
    // ============================================
    
    /**
     * Get all categories without pagination
     * GET /api/v1/categories/all
     */
    @GET("api/v1/categories/all")
    suspend fun getAllCategories(): Response<ApiResponse<List<CategoryResponse>>>
    
    /**
     * Get category by name
     * GET /api/v1/categories/name/{name}
     */
    @GET("api/v1/categories/name/{name}")
    suspend fun getCategoryByName(
        @Path("name") name: String
    ): Response<ApiResponse<CategoryResponse>>
    
    // ============================================
    // BRAND APIs
    // ============================================
    
    /**
     * Get all brands without pagination
     * GET /api/v1/brands/all
     */
    @GET("api/v1/brands/all")
    suspend fun getAllBrands(): Response<ApiResponse<List<BrandResponse>>>
    
    /**
     * Get brand by name
     * GET /api/v1/brands/name/{name}
     */
    @GET("api/v1/brands/name/{name}")
    suspend fun getBrandByName(
        @Path("name") name: String
    ): Response<ApiResponse<BrandResponse>>
    
    // ============================================
    // SHOPPING CART APIs
    // ============================================
    
    /**
     * Get shopping cart
     * GET /api/v1/buyer/cart
     */
    @GET("api/v1/buyer/cart")
    suspend fun getCart(): Response<ApiResponse<CartResponse>>
    
    /**
     * Get cart item count
     * GET /api/v1/buyer/cart/count
     */
    @GET("api/v1/buyer/cart/count")
    suspend fun getCartCount(): Response<ApiResponse<CartCountResponse>>
    
    /**
     * Add items to cart
     * POST /api/v1/buyer/cart/add
     * Note: Backend returns String message instead of CartResponse
     */
    @POST("api/v1/buyer/cart/add")
    suspend fun addToCart(
        @Body items: List<AddToCartRequest>
    ): Response<ApiResponse<String>>
    
    /**
     * Update cart item quantity
     * PUT /api/v1/buyer/cart/{productVariantId}
     */
    @retrofit2.http.PUT("api/v1/buyer/cart/{productVariantId}")
    suspend fun updateCartItemQuantity(
        @Path("productVariantId") productVariantId: String,
        @Query("colorId") colorId: String?,
        @Body request: UpdateQuantityRequest
    ): Response<ApiResponse<CartResponse>>
    
    /**
     * Remove item from cart
     * DELETE /api/v1/buyer/cart/{productVariantId}
     * Note: Backend returns String message instead of CartResponse
     */
    @retrofit2.http.DELETE("api/v1/buyer/cart/{productVariantId}")
    suspend fun removeCartItem(
        @Path("productVariantId") productVariantId: String,
        @Query("colorId") colorId: String?
    ): Response<ApiResponse<String>>
    
    /**
     * Clear entire cart
     * DELETE /api/v1/buyer/cart/clear
     */
    @retrofit2.http.DELETE("api/v1/buyer/cart/clear")
    suspend fun clearCart(): Response<ApiResponse<String>>
    
    // ============================================
    // BUYER ADDRESS APIs
    // ============================================
    
    /**
     * Get user address
     * GET /api/v1/buyer/address
     */
    @GET("api/v1/buyer/address")
    suspend fun getUserAddress(): Response<ApiResponse<AddressResponse>>
    
    /**
     * Check if user has address
     * GET /api/v1/buyer/address/check
     */
    @GET("api/v1/buyer/address/check")
    suspend fun checkHasAddress(): Response<ApiResponse<AddressCheckResponse>>
    
    /**
     * Create or update address
     * POST /api/v1/buyer/address
     */
    @POST("api/v1/buyer/address")
    suspend fun createOrUpdateAddress(
        @Body address: AddressDTO
    ): Response<ApiResponse<AddressResponse>>
    
    /**
     * Delete address
     * DELETE /api/v1/buyer/address
     */
    @retrofit2.http.DELETE("api/v1/buyer/address")
    suspend fun deleteAddress(): Response<ApiResponse<String>>

    // ============================================
    // BUYER ORDER APIs
    // ============================================

    /**
     * Checkout and create order
     * POST /api/v1/buyer/orders/checkout
     */
    @POST("api/v1/buyer/orders/checkout")
    suspend fun checkout(
        @Body orderDTO: OrderDTO
    ): Response<ApiResponse<CheckoutResponse>>

    /**
     * Get order history
     * GET /api/v1/buyer/orders
     */
    @GET("api/v1/buyer/orders")
    suspend fun getOrderHistory(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
        @Query("status") status: String? = null,
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("sortDir") sortDir: String = "desc"
    ): Response<ApiResponse<PageResponse<OrderResponse>>>

    /**
     * Get order detail
     * GET /api/v1/buyer/orders/{orderId}
     */
    @GET("api/v1/buyer/orders/{orderId}")
    suspend fun getOrderDetail(
        @Path("orderId") orderId: String
    ): Response<ApiResponse<OrderResponse>>

    /**
     * Cancel order
     * PUT /api/v1/buyer/orders/{orderId}/cancel
     */
    @retrofit2.http.PUT("api/v1/buyer/orders/{orderId}/cancel")
    suspend fun cancelOrder(
        @Path("orderId") orderId: String,
        @Body reason: String?
    ): Response<ApiResponse<OrderResponse>>

    // ============================================
    // REVIEW APIs
    // ============================================

    /**
     * Create product review
     * POST /api/v1/reviews
     */
    @POST("api/v1/reviews")
    suspend fun createReview(
        @Body reviewDTO: ReviewDTO
    ): Response<ApiResponse<ReviewResponse>>

    /**
     * Get review by ID
     * GET /api/v1/reviews/{reviewId}
     */
    @GET("api/v1/reviews/{reviewId}")
    suspend fun getReviewById(
        @Path("reviewId") reviewId: String
    ): Response<ApiResponse<ReviewResponse>>

    /**
     * Update review
     * PUT /api/v1/reviews/{reviewId}
     */
    @retrofit2.http.PUT("api/v1/reviews/{reviewId}")
    suspend fun updateReview(
        @Path("reviewId") reviewId: String,
        @Body reviewDTO: ReviewDTO
    ): Response<ApiResponse<ReviewResponse>>

    /**
     * Delete review
     * DELETE /api/v1/reviews/{reviewId}
     */
    @retrofit2.http.DELETE("api/v1/reviews/{reviewId}")
    suspend fun deleteReview(
        @Path("reviewId") reviewId: String
    ): Response<ApiResponse<String>>

    /**
     * Get reviews by product
     * GET /api/v1/reviews/product/{productId}
     */
    @GET("api/v1/reviews/product/{productId}")
    suspend fun getReviewsByProduct(
        @Path("productId") productId: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("sortDir") sortDir: String = "desc"
    ): Response<ApiResponse<PageResponse<ReviewResponse>>>

    /**
     * Get reviews by product variant
     * GET /api/v1/reviews/product-variant/{productVariantId}
     */
    @GET("api/v1/reviews/product-variant/{productVariantId}")
    suspend fun getReviewsByProductVariant(
        @Path("productVariantId") productVariantId: String
    ): Response<ApiResponse<List<ReviewResponse>>>

    /**
     * Get product rating statistics
     * GET /api/v1/reviews/product-variant/{productVariantId}/stats
     */
    @GET("api/v1/reviews/product-variant/{productVariantId}/stats")
    suspend fun getProductRatingStats(
        @Path("productVariantId") productVariantId: String
    ): Response<ApiResponse<RatingStatsResponse>>

    /**
     * Get my reviews
     * GET /api/v1/reviews/my-reviews
     */
    @GET("api/v1/reviews/my-reviews")
    suspend fun getMyReviews(): Response<ApiResponse<List<ReviewResponse>>>

    // ============================================
    // STORE APIs
    // ============================================

    /**
     * Get all stores
     * GET /api/v1/stores
     */
    @GET("api/v1/stores")
    suspend fun getAllStores(): Response<ApiResponse<List<StoreResponse>>>

    /**
     * Get store by ID
     * GET /api/v1/stores/{storeId}
     */
    @GET("api/v1/stores/{storeId}")
    suspend fun getStoreById(
        @Path("storeId") storeId: String
    ): Response<ApiResponse<StoreResponse>>

    /**
     * Get stores by owner
     * GET /api/v1/stores/owner/{ownerId}
     */
    @GET("api/v1/stores/owner/{ownerId}")
    suspend fun getStoresByOwner(
        @Path("ownerId") ownerId: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("sortDir") sortDir: String = "desc"
    ): Response<ApiResponse<PageResponse<StoreResponse>>>

    // ============================================
    // PROMOTION APIs
    // ============================================

    /**
     * Get all active promotions
     * GET /api/v1/b2c/promotions/active
     */
    @GET("api/v1/b2c/promotions/active")
    suspend fun getActivePromotions(): Response<ApiResponse<List<PromotionResponse>>>

    /**
     * Get active promotions by store
     * GET /api/v1/b2c/promotions/active/store/{storeId}
     */
    @GET("api/v1/b2c/promotions/active/store/{storeId}")
    suspend fun getActivePromotionsByStore(
        @Path("storeId") storeId: String
    ): Response<ApiResponse<List<PromotionResponse>>>

    /**
     * Get promotion by ID
     * GET /api/v1/b2c/promotions/{promotionId}
     */
    @GET("api/v1/b2c/promotions/{promotionId}")
    suspend fun getPromotionById(
        @Path("promotionId") promotionId: String
    ): Response<ApiResponse<PromotionResponse>>

    /**
     * Validate promotion
     * POST /api/v1/b2c/promotions/{promotionId}/validate
     */
    @POST("api/v1/b2c/promotions/{promotionId}/validate")
    suspend fun validatePromotion(
        @Path("promotionId") promotionId: String,
        @Query("orderValue") orderValue: Long
    ): Response<ApiResponse<PromotionValidationResponse>>

    /**
     * Calculate discount
     * POST /api/v1/b2c/promotions/{promotionId}/calculate-discount
     */
    @POST("api/v1/b2c/promotions/{promotionId}/calculate-discount")
    suspend fun calculateDiscount(
        @Path("promotionId") promotionId: String,
        @Query("orderValue") orderValue: Long
    ): Response<ApiResponse<DiscountCalculationResponse>>

    /**
     * Get promotions by store (all, not just active)
     * GET /api/v1/b2c/promotions/store/{storeId}
     */
    @GET("api/v1/b2c/promotions/store/{storeId}")
    suspend fun getPromotionsByStore(
        @Path("storeId") storeId: String
    ): Response<ApiResponse<List<PromotionResponse>>>
}

