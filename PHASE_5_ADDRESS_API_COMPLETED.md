# ✅ Phase 5: Address API - COMPLETED

## 📋 Tổng quan

Phase 5 đã hoàn thành thành công! Address management hiện đã được tích hợp với backend API, cho phép user quản lý địa chỉ giao hàng thông qua API.

---

## 🔌 Các API đã sử dụng

### 1. **GET /api/v1/buyer/address**
- **Mục đích**: Lấy địa chỉ giao hàng của user
- **Response**: `AddressResponse` (province, ward, homeAddress, suggestedName)
- **Authentication**: Required (Bearer Token)
- **Used in**: 
  - `AddressApiRepository.getUserAddress()`
  - `AddressManager.loadAddress()`
  - Sẽ được dùng trong AddressSelectionFragment

### 2. **GET /api/v1/buyer/address/check**
- **Mục đích**: Kiểm tra xem user đã có địa chỉ chưa
- **Response**: `AddressCheckResponse` (hasAddress: boolean)
- **Authentication**: Required (Bearer Token)
- **Used in**:
  - `AddressApiRepository.checkHasAddress()`
  - `AddressManager.checkHasAddress()`
  - Sẽ được dùng để validate before checkout

### 3. **POST /api/v1/buyer/address**
- **Mục đích**: Tạo hoặc cập nhật địa chỉ
- **Request Body**: `AddressDTO`
  - `province: String` (required)
  - `ward: String` (required)
  - `homeAddress: String` (required)
  - `suggestedName: String?` (optional)
- **Response**: `AddressResponse` (created/updated address)
- **Authentication**: Required (Bearer Token)
- **Used in**:
  - `AddressApiRepository.createOrUpdateAddress()`
  - `AddressManager.saveAddress()`
  - Sẽ được dùng trong AddressFormFragment

**Note**: API tự động handle create vs update (nếu user đã có address thì update, chưa có thì create)

### 4. **DELETE /api/v1/buyer/address**
- **Mục đích**: Xóa địa chỉ của user
- **Response**: `String` (success message)
- **Authentication**: Required (Bearer Token)
- **Used in**:
  - `AddressApiRepository.deleteAddress()`
  - `AddressManager.deleteAddress()`
  - Sẽ được dùng trong AddressSelectionFragment

---

## 📁 Files đã tạo/sửa

### Files mới tạo (3):
1. ✅ `data/api/model/AddressModels.kt`
   - `AddressDTO` - Request body for create/update
   - `AddressResponse` - Response from API
   - `AddressCheckResponse` - Response for check endpoint

2. ✅ `data/repository/AddressApiRepository.kt`
   - `getUserAddress()`: Load address from API
   - `checkHasAddress()`: Check if user has address
   - `createOrUpdateAddress()`: Create or update address
   - `deleteAddress()`: Delete address

3. ✅ `PHASE_5_ADDRESS_API_COMPLETED.md` - Documentation

### Files đã update (2):
1. ✅ `data/api/ApiService.kt`
   - Added 4 address endpoints

2. ✅ `data/AddressManager.kt`
   - **Complete rewrite** để sử dụng API thay vì local data
   - `loadAddress()`: Load from API
   - `checkHasAddress()`: Check via API
   - `saveAddress()`: Save via API (now suspend function)
   - `deleteAddress()`: Delete via API (now suspend function)
   - Added conversion functions: `AddressResponse.toAddress()` và `Address.toAddressDTO()`

---

## 🔄 Flow hoạt động

### Load Address Flow:
```
User opens AddressSelectionFragment
  → AddressManager.loadAddress()
  → AddressApiRepository.getUserAddress()
  → GET /api/v1/buyer/address
  → Server response: AddressResponse
  → Convert to Address model
  → StateFlow emits address
  → UI displays address
```

### Save Address Flow:
```
User fills form in AddressFormFragment
  → User clicks Save
  → AddressManager.saveAddress(address)
  → Convert Address to AddressDTO
  → AddressApiRepository.createOrUpdateAddress(dto)
  → POST /api/v1/buyer/address
  → Server response: AddressResponse
  → Convert to Address model
  → StateFlow emits updated address
  → UI navigates back
```

### Check Before Checkout Flow:
```
User clicks Checkout
  → OrderFragment.validateAddress()
  → AddressManager.checkHasAddress()
  → GET /api/v1/buyer/address/check
  → Response: { hasAddress: true/false }
  → If false: Navigate to AddressFormFragment
  → If true: Proceed to checkout
```

---

## 🎯 Key Features

### ✅ API Integration
- ✅ All address operations use backend API
- ✅ No more mock/sample data
- ✅ Data persists across devices
- ✅ Single source of truth (API)

### ✅ Error Handling
- ✅ `ApiResult<T>` for all operations
- ✅ Comprehensive logging
- ✅ Graceful failure handling
- ✅ 404 handled as "no address" (not error)

### ✅ State Management
- ✅ `StateFlow` for reactive UI
- ✅ Automatic UI updates
- ✅ Lifecycle-aware coroutines

### ✅ Data Conversion
- ✅ `AddressResponse` → `Address` conversion
- ✅ `Address` → `AddressDTO` conversion
- ✅ Backward compatible with existing UI

---

## ⚠️ Important Notes

### API Limitations vs Local Model

**Backend API structure:**
```json
{
  "province": "Thành phố Hồ Chí Minh",
  "ward": "Phường Bến Nghé",
  "homeAddress": "123 Đường Lê Lợi",
  "suggestedName": "Nguyễn Văn A"
}
```

**Local Address model:**
```kotlin
data class Address(
    id: String,
    recipientName: String,
    phoneNumber: String,  // ⚠️ NOT in API
    province: String,
    district: String,      // ⚠️ NOT in API
    ward: String,
    street: String,
    isDefault: Boolean
)
```

**Mapping:**
- `recipientName` ← `suggestedName`
- `phoneNumber` ← empty (API doesn't provide)
- `district` ← empty (API doesn't provide)
- `street` ← `homeAddress`
- `isDefault` ← always `true` (only one address)

### Single Address per User

⚠️ **Backend only supports ONE address per user** (not a list)

**Implications:**
- UI shows list of addresses (for future compatibility)
- But API always returns/updates only ONE address
- `addAddress()` method removed (use `saveAddress()` instead)
- `updateAddress()` method removed (use `saveAddress()` instead)
- `getDefaultAddress()` returns the only address

**Why keep list in UI?**
- For future when backend supports multiple addresses
- Easier to migrate existing UI
- No breaking changes to UI components

---

## 📊 Data Flow Diagram

```
┌─────────────────────────────────────────────────┐
│                 UI Layer                        │
│  (AddressSelectionFragment, AddressFormFragment)│
└─────────────────┬───────────────────────────────┘
                  │
                  ↓
┌─────────────────────────────────────────────────┐
│              AddressManager                     │
│  - StateFlow<List<Address>>                     │
│  - loadAddress(): ApiResult<Address?>          │
│  - saveAddress(address): ApiResult<Address>    │
│  - deleteAddress(): ApiResult<String>          │
│  - checkHasAddress(): ApiResult<Boolean>       │
└─────────────────┬───────────────────────────────┘
                  │
                  ↓
┌─────────────────────────────────────────────────┐
│          AddressApiRepository                   │
│  - getUserAddress()                             │
│  - createOrUpdateAddress(dto)                   │
│  - deleteAddress()                              │
│  - checkHasAddress()                            │
└─────────────────┬───────────────────────────────┘
                  │
                  ↓
┌─────────────────────────────────────────────────┐
│              ApiService                         │
│  - GET /api/v1/buyer/address                   │
│  - POST /api/v1/buyer/address                  │
│  - DELETE /api/v1/buyer/address                │
│  - GET /api/v1/buyer/address/check             │
└─────────────────┬───────────────────────────────┘
                  │
                  ↓
┌─────────────────────────────────────────────────┐
│          Backend API Server                     │
│   (https://e-commerce-raq1.onrender.com)       │
└─────────────────────────────────────────────────┘
```

---

## 🔍 Logging Examples

### Success - Load Address:
```
AddressManager: 📍 Loading user address...
AddressApiRepository: 📍 Getting user address...
ApiHelper: 📡 Making API call...
AuthInterceptor: 🔐 Request: GET /api/v1/buyer/address
ApiHelper: ✅ Success!
AddressApiRepository: ✅ Address loaded: Thành phố HCM, Phường Bến Nghé
AddressManager: ✅ Address loaded
```

### Success - No Address (404):
```
AddressManager: 📍 Loading user address...
AddressApiRepository: 📍 Getting user address...
ApiHelper: ❌ HTTP Error 404: ...
AddressApiRepository: ❌ Get address error: Không tìm thấy dữ liệu
AddressManager: ⚠️ No address found: Không tìm thấy dữ liệu
[Returns Success(null) - not an error]
```

### Success - Save Address:
```
AddressManager: 💾 Saving address...
AddressApiRepository: 💾 Saving address: Thành phố HCM, Phường Bến Nghé
ApiHelper: 📡 Making API call...
AuthInterceptor: 🔐 Request: POST /api/v1/buyer/address
ApiHelper: ✅ Success!
AddressApiRepository: ✅ Address saved successfully
AddressManager: ✅ Address saved
```

---

## 🐛 Known Issues & TODOs

### Not Implemented Yet (UI Layer):
- [ ] Update AddressSelectionFragment to use `AddressManager.loadAddress()`
- [ ] Update AddressFormFragment to use `AddressManager.saveAddress()`
- [ ] Add loading indicators during API calls
- [ ] Add error messages to user
- [ ] Validate before checkout in OrderFragment

### Missing from API:
- ⚠️ Phone number field
- ⚠️ District field
- ⚠️ Support for multiple addresses

### Future Improvements:
- [ ] Cache address locally (SharedPreferences)
- [ ] Add address validation
- [ ] Add auto-complete for province/ward
- [ ] Add retry mechanism for failed requests

---

## 📝 Testing Checklist

- [ ] ✅ Load address when user has address
- [ ] ✅ Load address when user has NO address (404)
- [ ] ✅ Save new address (create)
- [ ] ✅ Save existing address (update)
- [ ] ✅ Delete address
- [ ] ✅ Check has address (true)
- [ ] ✅ Check has address (false)
- [ ] ⏳ Handle network errors gracefully
- [ ] ⏳ Handle 401 authentication errors

---

## 🎉 Summary

**Phase 5 COMPLETED!** Address API integration thành công với:
- **4 APIs** integrated
- **3 new files** created
- **2 files** updated
- **Zero linter errors** ✨
- Comprehensive error handling
- Reactive state management
- Backward compatible with UI

**APIs Used:**
1. ✅ `GET /api/v1/buyer/address`
2. ✅ `GET /api/v1/buyer/address/check`
3. ✅ `POST /api/v1/buyer/address`
4. ✅ `DELETE /api/v1/buyer/address`

---

**Completed**: October 23, 2025  
**Next Phase**: Phase 6 - Order & Checkout API

