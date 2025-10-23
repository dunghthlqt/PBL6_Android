# 📋 CHI TIẾT KẾ HOẠCH API INTEGRATION - TẤT CẢ PHASES

## ✅ PHASE 1: INFRASTRUCTURE SETUP (COMPLETED)

### Mục tiêu:
Xây dựng foundation cho API integration

### Công việc đã hoàn thành:
- ✅ **AuthInterceptor**: Tự động inject JWT Bearer token
- ✅ **ApiResult sealed class**: Handle Success/Error/Loading states
- ✅ **Base Response Models**: ApiResponse<T>, PageResponse<T>, ErrorResponse
- ✅ **ApiHelper**: Safe API call wrapper với error handling
- ✅ **RetrofitClient**: Configure với AuthInterceptor + Logging
- ✅ **PBL6Application**: Initialize RetrofitClient with context
- ✅ **AndroidManifest.xml**: Declare custom Application class

### Files created:
- `data/api/AuthInterceptor.kt`
- `data/api/ApiResult.kt`
- `data/api/model/BaseResponse.kt`
- `data/api/ApiHelper.kt`
- `data/api/RetrofitClient.kt`
- `PBL6Application.kt`

---

## ✅ PHASE 2: PRODUCTS API (COMPLETED)

### Mục tiêu:
Tích hợp API cho products, product variants, categories, brands

### Công việc đã hoàn thành:

#### 2.1 Data Models
- ✅ `ProductResponse` - Product entity từ API
- ✅ `ProductVariantResponse` - Product variant với colors, sizes, price
- ✅ `ColorOptionResponse` - Color options cho variants
- ✅ `CategoryResponse` - Category entity
- ✅ `BrandResponse` - Brand entity

#### 2.2 API Endpoints (14 endpoints)
**Products (4):**
- ✅ `GET /api/v1/products` - Search by name
- ✅ `GET /api/v1/products/{id}` - Get by ID
- ✅ `GET /api/v1/products/category/{name}` - By category
- ✅ `GET /api/v1/products/category/{category}/brand/{brand}` - By category & brand

**Product Variants (6):**
- ✅ `GET /api/v1/product-variants/latest` - Latest variants
- ✅ `GET /api/v1/product-variants/{id}` - Get by ID
- ✅ `GET /api/v1/product-variants/product/{productId}` - By product
- ✅ `GET /api/v1/product-variants/store/{storeId}` - By store
- ✅ `GET /api/v1/product-variants/category/{category}` - By category
- ✅ `GET /api/v1/product-variants/category/{category}/brand/{brand}` - By category & brand

**Categories (2):**
- ✅ `GET /api/v1/categories` - All categories (paginated)
- ✅ `GET /api/v1/categories/{id}` - Get by ID

**Brands (2):**
- ✅ `GET /api/v1/brands` - All brands (paginated)
- ✅ `GET /api/v1/brands/{id}` - Get by ID

#### 2.3 Repository Layer
- ✅ **ProductApiRepository**: Handle API calls + convert to domain models
- ✅ **ProductRepository**: Use API with fallback to mock data
- ✅ Conversion logic: `ProductVariantResponse.toProduct()`

#### 2.4 UI Integration
- ✅ **LandingPageFragment**: Load products from API
- ✅ **ProductDetailFragment**: Get product by ID
- ✅ **SearchResultsFragment**: Search products
- ✅ **CategoryProductsFragment**: Filter by category
- ✅ **CategoriesFragment**: Show popular products
- ✅ **OrderDetailFragment**: Show recommended products

#### 2.5 Bug Fixes
- ✅ MainActivity recreation issue (theme application)
- ✅ Fragment lifecycle crashes (viewLifecycleOwner)
- ✅ ProductRepository lazy init
- ✅ Comprehensive logging for debugging
- ✅ Fixed Gson parsing error in search (category/brand as String instead of Object)
- ✅ Fixed 500 error in search by avoiding problematic variant fetch (server-side bug)
- ✅ Removed mock/sample data fallback - show errors instead
- ✅ Updated all fragments to handle ApiResult properly
- ✅ Temporarily disabled search function
- ✅ Fixed image display to show full images with aspect ratio

### Files created/modified:
- `data/api/model/ProductModels.kt`
- `data/api/model/CategoryModels.kt`
- `data/api/ApiService.kt` (added 14 endpoints)
- `data/repository/ProductApiRepository.kt`
- `data/ProductRepository.kt` (integrated API)
- 6 UI Fragments (lifecycle fixes)
- `MainActivity.kt` (theme fix)

---

## 🔄 PHASE 3: CATEGORIES & BRANDS API (PENDING)

### Mục tiêu:
Hoàn thiện category browsing và brand filtering

### Công việc cần làm:

#### 3.1 Repository Layer
- [ ] Create `CategoryApiRepository`
  - `getAllCategories(page, size)`: ApiResult<List<Category>>
  - `getCategoryById(id)`: ApiResult<Category>
  - Convert `CategoryResponse` → `Category`
  
- [ ] Create `BrandApiRepository`
  - `getAllBrands(page, size)`: ApiResult<List<Brand>>
  - `getBrandById(id)`: ApiResult<Brand>
  - Convert `BrandResponse` → `Brand`

#### 3.2 Update Existing Repositories
- [ ] **CategoryRepository**: Use `CategoryApiRepository` instead of mock
- [ ] Add caching mechanism (SharedPreferences/Room)

#### 3.3 UI Integration
- [ ] **CategoriesFragment**: Load real categories from API
- [ ] **CategoryProductsFragment**: Use API for filtering
- [ ] Add loading states
- [ ] Add error handling

#### 3.4 Testing
- [ ] Test category listing
- [ ] Test category filtering
- [ ] Test brand filtering
- [ ] Test error scenarios

### Files to create/modify:
- `data/repository/CategoryApiRepository.kt` (NEW)
- `data/repository/BrandApiRepository.kt` (NEW)
- `data/CategoryRepository.kt` (UPDATE)
- `ui/categories/CategoriesFragment.kt` (UPDATE)
- `ui/categories/CategoryProductsFragment.kt` (UPDATE)

### APIs to use:
- `GET /api/v1/categories` (paginated)
- `GET /api/v1/categories/all` (all at once)
- `GET /api/v1/brands` (paginated)
- `GET /api/v1/brands/all` (all at once)

---

## 🛒 PHASE 4: SHOPPING CART API (PENDING)

### Mục tiêu:
Chuyển cart từ local state sang API backend

### Công việc cần làm:

#### 4.1 Data Models
- [ ] `CartResponse` - Full cart with items
- [ ] `CartItemResponse` - Individual cart item
- [ ] `AddToCartRequest` - DTO for adding items

#### 4.2 API Endpoints (6)
- [ ] `GET /api/v1/buyer/cart` - Get cart
- [ ] `GET /api/v1/buyer/cart/count` - Get item count
- [ ] `POST /api/v1/buyer/cart/add` - Add items
- [ ] `PUT /api/v1/buyer/cart/{productVariantId}` - Update quantity
- [ ] `DELETE /api/v1/buyer/cart/{productVariantId}` - Remove item
- [ ] `DELETE /api/v1/buyer/cart/clear` - Clear cart

#### 4.3 Repository Layer
- [ ] Create `CartApiRepository`
- [ ] Update `CartManager` to use API instead of local state
- [ ] Sync local cache with API

#### 4.4 UI Integration
- [ ] **CartFragment**: Load from API
- [ ] **ProductDetailFragment**: Add to cart via API
- [ ] Show loading states
- [ ] Handle errors gracefully

#### 4.5 Authentication
- [ ] Require login for cart operations
- [ ] Navigate to login if not authenticated

### Files to create/modify:
- `data/api/model/CartModels.kt` (NEW)
- `data/repository/CartApiRepository.kt` (NEW)
- `data/CartManager.kt` (UPDATE - use API)
- `ui/cart/CartFragment.kt` (UPDATE)
- `ui/product/ProductDetailFragment.kt` (UPDATE)

---

## 📍 PHASE 5: ADDRESS API (PENDING)

### Mục tiêu:
Quản lý địa chỉ giao hàng qua API

### Công việc cần làm:

#### 5.1 Data Models
- [ ] `AddressResponse` - Address entity
- [ ] `AddressRequest` - DTO for create/update

#### 5.2 API Endpoints (3)
- [ ] `GET /api/v1/buyer/address` - Get user address
- [ ] `GET /api/v1/buyer/address/check` - Check if has address
- [ ] `POST /api/v1/buyer/address` - Create/update address
- [ ] `DELETE /api/v1/buyer/address` - Delete address

#### 5.3 Repository Layer
- [ ] Create `AddressApiRepository`
- [ ] Update `AddressManager` to use API

#### 5.4 UI Integration
- [ ] **AddressSelectionFragment**: Load from API
- [ ] **AddressFormFragment**: Create/update via API
- [ ] **OrderFragment**: Check address before checkout

### Files to create/modify:
- `data/api/model/AddressModels.kt` (NEW)
- `data/repository/AddressApiRepository.kt` (NEW)
- `data/AddressManager.kt` (UPDATE)
- `ui/address/AddressSelectionFragment.kt` (UPDATE)
- `ui/address/AddressFormFragment.kt` (UPDATE)

---

## 📦 PHASE 6: ORDER & CHECKOUT API (PENDING)

### Mục tiêu:
Xử lý checkout và order history qua API

### Công việc cần làm:

#### 6.1 Data Models
- [ ] `OrderResponse` - Order entity
- [ ] `OrderItemResponse` - Order item
- [ ] `CheckoutRequest` - DTO for checkout
- [ ] `OrderStatus` enum

#### 6.2 API Endpoints (4)
- [ ] `POST /api/v1/buyer/orders/checkout` - Create order
- [ ] `GET /api/v1/buyer/orders` - Order history (paginated)
- [ ] `GET /api/v1/buyer/orders/{orderId}` - Order detail
- [ ] `PUT /api/v1/buyer/orders/{orderId}/cancel` - Cancel order

#### 6.3 Repository Layer
- [ ] Create `OrderApiRepository`
- [ ] Update `OrderRepository` to use API

#### 6.4 UI Integration
- [ ] **OrderFragment**: Checkout via API
- [ ] **OrderHistoryFragment**: Load from API
- [ ] **OrderDetailFragment**: Get order detail from API
- [ ] Handle order status updates

### Files to create/modify:
- `data/api/model/OrderModels.kt` (NEW)
- `data/repository/OrderApiRepository.kt` (NEW)
- `data/OrderRepository.kt` (UPDATE)
- `ui/order/OrderFragment.kt` (UPDATE)
- `ui/order/OrderHistoryFragment.kt` (UPDATE)
- `ui/order/OrderDetailFragment.kt` (UPDATE)

---

## ⭐ PHASE 7: REVIEWS API (PENDING)

### Mục tiêu:
Xử lý reviews và ratings qua API

### Công việc cần làm:

#### 7.1 Data Models
- [ ] `ReviewResponse` - Review entity
- [ ] `ReviewRequest` - DTO for create/update
- [ ] `RatingStatsResponse` - Rating statistics

#### 7.2 API Endpoints (6)
- [ ] `POST /api/v1/reviews` - Create review
- [ ] `GET /api/v1/reviews/{reviewId}` - Get review
- [ ] `PUT /api/v1/reviews/{reviewId}` - Update review
- [ ] `DELETE /api/v1/reviews/{reviewId}` - Delete review
- [ ] `GET /api/v1/reviews/product/{productId}` - Get reviews by product
- [ ] `GET /api/v1/reviews/product-variant/{variantId}/stats` - Rating stats

#### 7.3 Repository Layer
- [ ] Create `ReviewApiRepository`
- [ ] Create `ReviewRepository`

#### 7.4 UI Integration
- [ ] **ProductReviewsFragment**: Load reviews from API
- [ ] **CreateReviewFragment**: Submit review via API (NEW)
- [ ] Show rating statistics

### Files to create/modify:
- `data/api/model/ReviewModels.kt` (NEW)
- `data/repository/ReviewApiRepository.kt` (NEW)
- `data/repository/ReviewRepository.kt` (NEW)
- `ui/product/ProductReviewsFragment.kt` (UPDATE)
- `ui/review/CreateReviewFragment.kt` (NEW)

---

## 🏪 PHASE 8: STORES & PROMOTIONS API (PENDING)

### Mục tiêu:
Browse stores và apply promotions

### Công việc cần làm:

#### 8.1 Data Models
- [ ] `StoreResponse` - Store entity
- [ ] `PromotionResponse` - Promotion/voucher entity

#### 8.2 API Endpoints (4)
- [ ] `GET /api/v1/stores` - All stores
- [ ] `GET /api/v1/stores/{storeId}` - Store detail
- [ ] `GET /api/v1/b2c/promotions/active` - Active promotions
- [ ] `POST /api/v1/b2c/promotions/{promotionId}/validate` - Validate voucher

#### 8.3 Repository Layer
- [ ] Create `StoreApiRepository`
- [ ] Create `PromotionApiRepository`

#### 8.4 UI Integration
- [ ] **ShopFragment**: Load store detail from API
- [ ] **ShopVoucherFragment**: Load vouchers from API
- [ ] **PlatformVoucherFragment**: Load platform vouchers

### Files to create/modify:
- `data/api/model/StoreModels.kt` (NEW)
- `data/api/model/PromotionModels.kt` (NEW)
- `data/repository/StoreApiRepository.kt` (NEW)
- `data/repository/PromotionApiRepository.kt` (NEW)
- `ui/shop/ShopFragment.kt` (UPDATE)
- `ui/voucher/ShopVoucherFragment.kt` (UPDATE)
- `ui/voucher/PlatformVoucherFragment.kt` (UPDATE)

---

## 🧪 PHASE 9: TESTING & OPTIMIZATION (PENDING)

### Mục tiêu:
Test toàn bộ app và optimize performance

### Công việc cần làm:

#### 9.1 End-to-End Testing
- [ ] Test full user journey: Browse → Add to cart → Checkout → Order
- [ ] Test authentication flow
- [ ] Test error scenarios
- [ ] Test network failures

#### 9.2 Performance Optimization
- [ ] Implement caching (SharedPreferences/Room)
- [ ] Optimize image loading (Glide/Coil)
- [ ] Reduce API calls (cache responses)
- [ ] Add pagination for lists

#### 9.3 UI/UX Polish
- [ ] Add loading states for all API calls
- [ ] Add empty states
- [ ] Add error states with retry
- [ ] Improve animations

#### 9.4 Bug Fixes
- [ ] Fix any remaining crashes
- [ ] Fix memory leaks
- [ ] Fix UI glitches

### Testing Checklist:
- [ ] Authentication works
- [ ] Product browsing works
- [ ] Search works
- [ ] Cart operations work
- [ ] Checkout works
- [ ] Order history works
- [ ] Reviews work
- [ ] Vouchers work

---

## 📊 PROGRESS TRACKING

| Phase | Status | Progress | Files Created | Files Modified |
|-------|--------|----------|---------------|----------------|
| Phase 1 | ✅ DONE | 100% | 6 | 1 |
| Phase 2 | ✅ DONE | 100% | 3 | 8 |
| Phase 3 | ⏳ TODO | 0% | 2 | 2 |
| Phase 4 | ⏳ TODO | 0% | 3 | 3 |
| Phase 5 | ⏳ TODO | 0% | 3 | 3 |
| Phase 6 | ⏳ TODO | 0% | 3 | 4 |
| Phase 7 | ⏳ TODO | 0% | 4 | 1 |
| Phase 8 | ⏳ TODO | 0% | 5 | 3 |
| Phase 9 | ⏳ TODO | 0% | 0 | ALL |

**Total Progress: 22% (2/9 phases)**

---

## 🔑 KEY PRINCIPLES

1. **Clean Architecture**: Data → Domain → Presentation
2. **Repository Pattern**: Abstract data sources
3. **API First**: Use real API with mock fallback
4. **Error Handling**: ApiResult for all API calls
5. **Lifecycle Aware**: viewLifecycleOwner for coroutines
6. **Logging**: Comprehensive debug logs
7. **Testing**: Test each phase thoroughly before moving on
8. **Performance**: Cache responses, lazy load data
9. **UX**: Loading states, error states, empty states
10. **Security**: JWT authentication for protected endpoints

---

## 📝 NOTES

- Base URL: `https://e-commerce-raq1.onrender.com/`
- Public endpoints: products, product-variants, stores, categories, brands
- Protected endpoints: cart, orders, address, reviews (require Bearer token)
- Use `viewLifecycleOwner.lifecycleScope` in Fragments
- Check `_binding != null` before UI updates
- Apply theme BEFORE `super.onCreate()` in MainActivity
- Use lazy initialization for heavy objects
- **Important:** `/api/v1/products` returns `category` and `brand` as **strings**, not objects
- **Important:** `/api/v1/product-variants` has nested `product.category` and `product.brand` as **strings**
- **Known Issue:** `/api/v1/product-variants/product/{productId}` has server-side error (500) - avoid using it
- **Workaround:** Use `/api/v1/products` directly for search, convert ProductResponse to Product

---

**Last Updated:** Phase 2 Completed - October 22, 2025

