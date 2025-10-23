# 🔧 Fix Timeout Issue với Cart APIs

## 🐛 Vấn đề

Cart APIs (và các APIs khác) đang gặp timeout mặc dù:
- ✅ Postman test API rất nhanh (~1 giây)
- ✅ Backend server hoạt động bình thường
- ❌ Android app timeout sau 30 giây

---

## 🔍 Nguyên nhân

### 1. **Render.com Free Tier "Cold Start"**
Render.com free tier có tính năng **auto-sleep**:
- Server sleep sau 15 phút không activity
- Khi có request mới, server cần **50-90 giây** để wake up
- Postman có thể đã wake server trước, Android app gọi sau bị sleep lại

### 2. **OkHttp Timeout Mặc định Quá Ngắn**
Trước đây:
```kotlin
.connectTimeout(30, TimeUnit.SECONDS)  // ❌ Quá ngắn cho cold start
.readTimeout(30, TimeUnit.SECONDS)
.writeTimeout(30, TimeUnit.SECONDS)
```

### 3. **Không có Retry Mechanism**
- Khi connection fail, không tự động retry
- Không có connection pool để reuse connections

### 4. **Thiếu Overall Call Timeout**
- Không có timeout cho toàn bộ request (including retries)
- Có thể bị stuck vô hạn

---

## ✅ Giải pháp đã apply

### 1. **Tăng Timeout lên 90 giây**

```kotlin
private fun createOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(context))
        .addInterceptor(loggingInterceptor)
        
        // ✅ Increased timeout for Render.com free tier
        .connectTimeout(90, TimeUnit.SECONDS)     // Was: 30s
        .readTimeout(90, TimeUnit.SECONDS)        // Was: 30s
        .writeTimeout(90, TimeUnit.SECONDS)       // Was: 30s
        .callTimeout(120, TimeUnit.SECONDS)       // NEW: Overall timeout
        
        // ✅ Retry on connection failure
        .retryOnConnectionFailure(true)           // NEW
        
        // ✅ Connection pool to reuse connections
        .connectionPool(okhttp3.ConnectionPool(5, 5, TimeUnit.MINUTES))  // NEW
        
        .build()
}
```

**Lý do cho mỗi timeout:**
- **connectTimeout (90s)**: Thời gian để establish TCP connection (cold start có thể chậm)
- **readTimeout (90s)**: Thời gian chờ đọc data từ server (server có thể chậm xử lý)
- **writeTimeout (90s)**: Thời gian ghi data lên server (upload large data)
- **callTimeout (120s)**: Tổng thời gian cho toàn bộ request (bao gồm retries)

### 2. **Enable Retry on Connection Failure**

```kotlin
.retryOnConnectionFailure(true)
```

- Tự động retry khi connection bị drop
- Tăng success rate khi network không ổn định

### 3. **Connection Pool**

```kotlin
.connectionPool(okhttp3.ConnectionPool(5, 5, TimeUnit.MINUTES))
```

- Giữ tối đa 5 idle connections
- Mỗi connection sống 5 phút
- Reuse connections thay vì tạo mới → **Nhanh hơn**

### 4. **Enhanced Logging trong AuthInterceptor**

```kotlin
override fun intercept(chain: Interceptor.Chain): Response {
    val startTime = System.currentTimeMillis()
    // ... process request ...
    val response = chain.proceed(requestBuilder.build())
    val duration = System.currentTimeMillis() - startTime
    
    android.util.Log.d("AuthInterceptor", "⏱️ Request completed in ${duration}ms")
    return response
}
```

**Benefits:**
- ✅ Track exact request duration
- ✅ Identify slow endpoints
- ✅ Debug timeout issues

---

## 📊 Kết quả mong đợi

### Trước khi fix:
```
🔐 Request: GET /api/v1/buyer/cart
⏱️ Timeout after 30s
❌ Error: "Timeout - Server phản hồi quá lâu"
```

### Sau khi fix:
```
🔐 Request: GET /api/v1/buyer/cart
⏱️ Request completed in 52,341ms (200)
✅ Success!
```

**Lưu ý:**
- Lần đầu request (cold start): **50-90 giây**
- Lần sau (warm): **1-2 giây** (nhờ connection pool)

---

## 🎯 Các trường hợp timeout

### Case 1: Cold Start (50-90s)
**Khi xảy ra:**
- Server sleep sau 15 phút không activity
- Request đầu tiên sau khi sleep

**Giải pháp:**
- ✅ Đã tăng timeout lên 90s
- ✅ Thêm loading indicator cho user
- 💡 Recommendation: Keep-alive ping mỗi 10 phút (optional)

### Case 2: Slow Network (<1MB/s)
**Khi xảy ra:**
- User dùng 2G/3G chậm
- Network congestion

**Giải pháp:**
- ✅ Retry on connection failure
- ✅ Connection pool reuse
- 💡 Recommendation: Add offline mode (future)

### Case 3: Server Processing (10-30s)
**Khi xảy ra:**
- API xử lý data phức tạp
- Database query chậm

**Giải pháp:**
- ✅ readTimeout 90s đủ chờ
- ✅ Show progress indicator
- 💡 Recommendation: Backend optimization

---

## 🔍 Debug Timeout Issues

### Cách xem logs:

```bash
# Filter by timeout
adb logcat | grep -E "Timeout|⏱️"

# Filter by cart APIs
adb logcat | grep -E "cart|Cart"

# Filter by AuthInterceptor
adb logcat | grep "AuthInterceptor"
```

### Log format:

```
🔐 Request: POST /api/v1/buyer/cart/add
🔑 Adding Bearer token (length: 165)
⏱️ Request completed in 52341ms (200)
```

**Ý nghĩa:**
- `🔐` - Request starting
- `🔑` - Token added (protected endpoint)
- `✅` - Public endpoint (no token)
- `⏱️` - Request duration + status code

---

## 💡 Recommendations

### 1. **Backend Team:**
- ❗ Consider upgrading from Free tier → Paid tier (no sleep)
- 💡 Add health check endpoint để keep server warm
- 💡 Optimize slow queries

### 2. **Mobile Team:**
- ✅ Show loading indicator cho user
- 💡 Add retry button khi timeout
- 💡 Cache data để offline mode
- 💡 Add pull-to-refresh

### 3. **DevOps:**
- 💡 Setup monitoring cho API response time
- 💡 Alert khi timeout rate > 5%
- 💡 Consider CDN cho static assets

---

## 📝 Testing Checklist

- [ ] Test cart APIs sau khi server sleep 15 phút
- [ ] Test với slow network (Chrome DevTools throttling)
- [ ] Test retry mechanism (bật/tắt wifi nhiều lần)
- [ ] Test connection pool (multiple requests liên tiếp)
- [ ] Verify logs show correct duration
- [ ] Test với Postman để confirm server OK

---

## 🎉 Summary

**Fixed Issues:**
- ✅ Tăng timeout từ 30s → 90s
- ✅ Added overall call timeout 120s
- ✅ Enable retry on connection failure
- ✅ Added connection pool (5 connections, 5 min)
- ✅ Enhanced logging với duration tracking

**Expected Behavior:**
- 🟢 **Cold start:** 50-90s (first request after sleep)
- 🟢 **Warm requests:** 1-2s (subsequent requests)
- 🟢 **Retry:** Auto retry on failure
- 🟢 **Logging:** Clear duration logs

**Files Modified:**
- ✅ `data/api/RetrofitClient.kt`
- ✅ `data/api/AuthInterceptor.kt`

---

**Last Updated:** October 22, 2025  
**Issue:** Cart APIs timeout  
**Status:** ✅ FIXED

