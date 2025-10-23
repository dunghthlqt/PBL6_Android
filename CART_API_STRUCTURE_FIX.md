# 🔧 Fix Cart API Response Structure Mismatch

## 🐛 **Vấn đề:**

API trả về data nhưng app parse được **0 items**:

```
✅ Cart loaded: 0 items
```

Mặc dù response JSON có **3 items**:
```json
{
  "success":true,
  "data":{
    "id":"68ed255bb336fbacc3e2be07",
    "user":{...},
    "cartItems":[...3 items...],  // ← Có data!
    "totalPrice":70620000
  }
}
```

---

## 🔍 **Root Cause:**

### Model không khớp với actual API response!

**Model expect (CartModels.kt):**
```kotlin
data class CartResponse(
    val items: List<CartItemResponse>?,        // ← Expect "items"
    val totalItems: Int,                        // ← Expect "totalItems"
    val userId: String?,                        // ← Expect flat "userId"
    // ...
)

data class CartItemResponse(
    val productVariantId: String,               // ← Expect "productVariantId"
    val productVariant: ProductVariantResponse?, // ← Expect nested object
    // ...
)
```

**Actual API response:**
```json
{
  "data": {
    "cartItems": [...],        // ← API uses "cartItems" not "items"
    // NO "totalItems" field!
    "user": {                  // ← User is nested object
      "id": "..."
    },
    // ...
  }
}
```

**Cart Item structure:**
```json
{
  "productId": "...",          // ← API uses "productId" not "productVariantId"
  "productName": "...",        // ← Direct field, not nested
  "imageUrl": "...",           // ← Direct field, not nested
  "quantity": 11,
  "price": 1300000,
  "colorId": null,
  "colorName": null
  // NO "productVariant" nested object!
  // NO "subtotal" field!
}
```

### Kết quả:
- Gson không parse được `cartItems` → `items` = null
- `totalItems` không tồn tại → default = 0
- `productVariant` = null → skip tất cả items

---

## ✅ **Fix Applied:**

### 1. **Updated CartResponse:**

```kotlin
data class CartResponse(
    @SerializedName("id")
    val id: String?,
    
    @SerializedName("user")                    // ✅ Match API: nested user object
    val user: CartUserResponse?,
    
    @SerializedName("cartItems")               // ✅ Match API: "cartItems" not "items"
    val cartItems: List<CartItemResponse>?,
    
    @SerializedName("totalPrice")
    val totalPrice: Long,
    
    // ... other fields ...
) {
    // ✅ Computed properties for backward compatibility
    val items: List<CartItemResponse>?
        get() = cartItems
    
    val totalItems: Int
        get() = cartItems?.size ?: 0           // ✅ Calculate from array size
    
    val userId: String?
        get() = user?.id                       // ✅ Extract from nested user
}
```

### 2. **Added CartUserResponse:**

```kotlin
data class CartUserResponse(
    @SerializedName("id")
    val id: String?,
    
    @SerializedName("fullName")
    val fullName: String?,
    
    @SerializedName("email")
    val email: String?,
    
    @SerializedName("phone")
    val phone: String?
)
```

### 3. **Updated CartItemResponse:**

```kotlin
data class CartItemResponse(
    @SerializedName("productId")              // ✅ Match API: "productId"
    val productId: String,
    
    @SerializedName("productName")            // ✅ Direct field
    val productName: String,
    
    @SerializedName("imageUrl")               // ✅ Direct field
    val imageUrl: String?,
    
    @SerializedName("quantity")
    val quantity: Int,
    
    @SerializedName("price")
    val price: Long,
    
    @SerializedName("colorId")
    val colorId: String?,
    
    @SerializedName("colorName")
    val colorName: String?
) {
    // ✅ Computed properties for backward compatibility
    val productVariantId: String
        get() = productId
    
    val productVariant: ProductVariantResponse?
        get() = null  // Not available in this API
    
    val subtotal: Long
        get() = price * quantity
}
```

### 4. **Updated CartManager:**

```kotlin
private fun updateLocalCartFromApi(cartResponse: CartResponse) {
    // ...
    
    cartResponse.items?.forEach { item ->
        // ✅ Since API doesn't provide store info, use default store
        val storeId = "default_store"
        val storeName = "Cửa hàng của tôi"
        
        val cartProduct = CartProduct(
            id = item.productId,              // ✅ Direct access
            name = item.productName,          // ✅ Direct access
            color = item.colorName ?: "Default",
            size = "Standard",                // ✅ API doesn't provide size
            currentPrice = item.price.toInt(),
            originalPrice = (item.price * 1.2).toInt(),
            quantity = item.quantity,
            imageUrl = item.imageUrl ?: "",   // ✅ Direct access
            isSelected = false
        )
        
        shopMap.getOrPut(storeId) { mutableListOf() }.add(cartProduct)
    }
    // ...
}
```

### 5. **Enhanced Logging:**

```kotlin
when (result) {
    is ApiResult.Success -> {
        val cartData = result.data
        android.util.Log.d("CartApiRepository", "✅ Cart loaded: ${cartData.totalItems} items")
        android.util.Log.d("CartApiRepository", "📊 Cart data: id=${cartData.id}, userId=${cartData.userId}, items size=${cartData.items?.size}, totalPrice=${cartData.totalPrice}")
        android.util.Log.d("CartApiRepository", "📦 Full cart data: $cartData")
        result
    }
}
```

---

## 📊 **Before vs After:**

### Before Fix:
```
API Response: {"cartItems":[...3 items...]}
  → Gson parse: items = null (field name mismatch)
  → totalItems = 0 (default value)
  → cartResponse.items.forEach() → skip (null)
✅ Cart loaded: 0 items
```

### After Fix:
```
API Response: {"cartItems":[...3 items...]}
  → Gson parse: cartItems = [3 items] ✅
  → items (computed) = cartItems ✅
  → totalItems (computed) = cartItems.size = 3 ✅
  → cartResponse.items?.forEach() → process 3 items ✅
✅ Cart loaded: 3 items
📊 Cart data: id=..., userId=..., items size=3, totalPrice=70620000
```

---

## 🎯 **Key Insights:**

### 1. **Always verify API structure:**
❌ **Don't assume** API matches documentation  
✅ **Always inspect** actual JSON response  
✅ **Use logging** to debug mismatches  

### 2. **Computed properties for compatibility:**
```kotlin
data class CartResponse(
    @SerializedName("cartItems")
    val cartItems: List<CartItemResponse>?,
) {
    // ✅ Provide computed property for backward compatibility
    val items: List<CartItemResponse>?
        get() = cartItems
}
```

**Benefits:**
- ✅ Match actual API structure
- ✅ Keep existing code working
- ✅ Easy to refactor later

### 3. **Handle missing fields:**
```kotlin
val totalItems: Int
    get() = cartItems?.size ?: 0  // ✅ Calculate if not provided
```

### 4. **Simplified data processing:**
```kotlin
// Before: Complex nested navigation
productVariant.product?.store?.name

// After: Direct access
item.productName
```

---

## 🔍 **Testing Checklist:**

- [x] ✅ Load cart with 3 items → Show 3 items
- [x] ✅ Parse productId correctly
- [x] ✅ Parse productName correctly
- [x] ✅ Parse imageUrl correctly
- [x] ✅ Calculate totalItems from array size
- [x] ✅ Extract userId from nested user object
- [x] ✅ Handle empty cart (cartItems = [])
- [x] ✅ Handle null cart (cartItems = null)

---

## 📝 **Important Notes:**

### Store Grouping:
⚠️ **API doesn't provide store information** in cart items
- Current solution: Group all items under one "default_store"
- Store name: "Cửa hàng của tôi"
- Future: May need separate API to get store info by productId

### Size Information:
⚠️ **API doesn't provide size/variant info**
- Current solution: Use "Standard" as default size
- Future: May need to fetch product details separately

### Price Calculation:
- `currentPrice`: From API `price` field
- `originalPrice`: Calculated as `price * 1.2` (assume 20% discount)
- `subtotal`: Calculated as `price * quantity`

---

## 📁 **Files Modified:**

1. ✅ `data/api/model/CartModels.kt`
   - Updated `CartResponse` structure
   - Added `CartUserResponse`
   - Updated `CartItemResponse` structure
   - Added computed properties

2. ✅ `data/CartManager.kt`
   - Simplified cart processing
   - Direct field access
   - Default store grouping

3. ✅ `data/repository/CartApiRepository.kt`
   - Enhanced logging for debugging

---

## 🎉 **Result:**

**Before:**
- ❌ 0 items loaded despite API returning data
- ❌ Silent failure, hard to debug
- ❌ Wrong model structure

**After:**
- ✅ All 3 items loaded correctly
- ✅ Clear logging at each step
- ✅ Model matches actual API
- ✅ Backward compatible with computed properties

---

**Fixed:** October 23, 2025  
**Issue:** Cart API response structure mismatch  
**Status:** ✅ RESOLVED

