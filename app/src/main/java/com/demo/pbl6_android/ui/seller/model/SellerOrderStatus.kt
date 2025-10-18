package com.demo.pbl6_android.ui.seller.model

enum class SellerOrderStatus(val displayName: String) {
    PENDING_CONFIRMATION("Chờ xác nhận"),
    PENDING_PICKUP("Chờ lấy hàng"),
    SHIPPING("Đang giao"),
    DELIVERED("Đã giao"),
    CANCELLED("Đơn hủy"),
    RETURN_REFUND("Trả hàng/Hoàn tiền"),
    DELIVERY_FAILED("Giao không thành công")
}

enum class PendingPickupSubStatus(val displayName: String) {
    ALL("Tất cả"),
    UNPROCESSED("Chưa xử lý"),
    PROCESSED("Đã xử lý")
}

enum class CancelledSubStatus(val displayName: String) {
    ALL("Tất cả"),
    WAITING_RESPONSE("Chờ phản hồi"),
    KEEP_PACKAGE("Giữ lại kiện hàng"),
    CANCELLED("Đã hủy")
}

enum class ReturnRefundSubStatus(val displayName: String) {
    ALL("Tất cả"),
    NEW_REQUEST("Yêu cầu mới"),
    NEED_RESPONSE("Cần phản hồi"),
    RESPONDED("Đã phản hồi"),
    COMPLETED("Hoàn thành")
}

enum class DeliveryFailedSubStatus(val displayName: String) {
    ALL("Tất cả"),
    RETURNING("Đang trả hàng"),
    RETURNED("Đã trả hàng"),
    RETURN_FAILED("Trả hàng không thành công"),
    PACKAGE_LOST("Kiện hàng bị thất lạc")
}

enum class ShippingMethod(val displayName: String) {
    ALL("Tất cả"),
    EXPRESS("Hỏa tốc"),
    BULKY("Hàng cồng kềnh"),
    FAST("Nhanh"),
    ECONOMY("Tiết kiệm"),
    STANDARD("Giao Hàng Nhanh")
}

enum class OrderSortType(val displayName: String) {
    CONFIRM_DATE_DESC("Ngày xác nhận đặt đơn: Gần – Xa nhất"),
    CONFIRM_DATE_ASC("Ngày xác nhận đặt đơn: Xa – Gần nhất"),
    DELIVERY_DEADLINE_DESC("Hạn giao hàng: Gần – Xa nhất"),
    DELIVERY_DEADLINE_ASC("Hạn giao hàng: Xa – Gần nhất"),
    ORDER_DATE_DESC("Ngày đặt: Gần – Xa nhất"),
    ORDER_DATE_ASC("Ngày đặt: Xa – Gần nhất")
}

enum class CancellationReason(val displayName: String) {
    CHANGED_MIND("Đổi ý, không mua nữa"),
    VOUCHER_CHANGE("Muốn nhập/thay đổi mã voucher"),
    ADDRESS_CHANGE("Muốn thay đổi địa chỉ giao hàng")
}

enum class CancelledBy {
    BUYER,
    SELLER
}

