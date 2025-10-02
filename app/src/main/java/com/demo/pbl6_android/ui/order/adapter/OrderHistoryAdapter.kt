package com.demo.pbl6_android.ui.order.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.demo.pbl6_android.data.model.Order
import com.demo.pbl6_android.data.model.OrderStatus
import com.demo.pbl6_android.databinding.ItemOrderHistoryBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class OrderHistoryAdapter(
    private val onContactShopClick: (Order) -> Unit,
    private val onCancelOrderClick: (Order) -> Unit,
    private val onReceivedClick: (Order) -> Unit,
    private val onReturnRefundClick: (Order) -> Unit,
    private val onReviewClick: (Order) -> Unit,
    private val onBuyAgainClick: (Order) -> Unit,
    private val onViewDetailsClick: (Order) -> Unit,
    private val onPayNowClick: (Order) -> Unit
) : ListAdapter<Order, OrderHistoryAdapter.OrderViewHolder>(OrderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class OrderViewHolder(
        private val binding: ItemOrderHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order) {
            with(binding) {
                // Display first product item
                val firstItem = order.items.firstOrNull()
                firstItem?.let { item ->
                    tvProductName.text = item.productName
                    tvPrice.text = formatPrice(item.price)
                    // TODO: Load product image using image loading library (Glide/Coil)
                }

                tvOrderCode.text = order.orderCode
                tvQuantity.text = order.items.sumOf { it.quantity }.toString()
                tvOrderDate.text = "Ngày đặt: ${formatDate(order.orderDate)}"

                // Status
                tvStatus.text = order.status.displayName
                val statusColor = Color.parseColor(order.status.color)
                tvStatus.setTextColor(statusColor)
                statusIndicator.backgroundTintList = android.content.res.ColorStateList.valueOf(statusColor)

                // Configure buttons based on status
                configureButtonsForStatus(order)
            }
        }

        private fun configureButtonsForStatus(order: Order) {
            with(binding) {
                when (order.status) {
                    OrderStatus.PENDING_PAYMENT -> {
                        // Chờ thanh toán: "Thanh toán ngay", "Hủy đơn hàng"
                        btnAction1.text = "Thanh toán ngay"
                        btnAction1.setOnClickListener { onPayNowClick(order) }
                        btnAction1.visibility = View.VISIBLE

                        btnAction2.text = "Hủy đơn hàng"
                        btnAction2.setOnClickListener { onCancelOrderClick(order) }
                        btnAction2.visibility = View.VISIBLE
                    }

                    OrderStatus.PENDING_PICKUP -> {
                        // Chờ lấy hàng: "Liên hệ shop", "Hủy đơn hàng"
                        btnAction1.text = "Liên hệ shop"
                        btnAction1.setOnClickListener { onContactShopClick(order) }
                        btnAction1.visibility = View.VISIBLE

                        btnAction2.text = "Hủy đơn hàng"
                        btnAction2.setOnClickListener { onCancelOrderClick(order) }
                        btnAction2.visibility = View.VISIBLE
                    }

                    OrderStatus.SHIPPING -> {
                        // Chờ giao hàng: "Liên hệ shop", "Đã nhận được hàng" (disabled by default)
                        btnAction1.text = "Liên hệ shop"
                        btnAction1.setOnClickListener { onContactShopClick(order) }
                        btnAction1.visibility = View.VISIBLE

                        btnAction2.text = "Đã nhận được hàng"
                        btnAction2.setOnClickListener { onReceivedClick(order) }
                        btnAction2.isEnabled = false
                        btnAction2.alpha = 0.5f
                        btnAction2.visibility = View.VISIBLE
                    }

                    OrderStatus.DELIVERED -> {
                        // Đã giao: Logic phức tạp - "Trả hàng/Hoàn tiền" hoặc "Mua lại" + "Đánh giá"
                        val daysSinceReceived = order.receivedDate?.let {
                            TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - it)
                        } ?: 0

                        if (daysSinceReceived <= 7) {
                            btnAction1.text = "Trả hàng/Hoàn tiền"
                            btnAction1.setOnClickListener { onReturnRefundClick(order) }
                        } else {
                            btnAction1.text = "Mua lại"
                            btnAction1.setOnClickListener { onBuyAgainClick(order) }
                        }
                        btnAction1.visibility = View.VISIBLE

                        btnAction2.text = "Đánh giá"
                        btnAction2.setOnClickListener { onReviewClick(order) }
                        btnAction2.visibility = View.VISIBLE
                    }

                    OrderStatus.RETURN_REFUND -> {
                        // Trả hàng: "Xem chi tiết"
                        btnAction1.text = "Xem chi tiết"
                        btnAction1.setOnClickListener { onViewDetailsClick(order) }
                        btnAction1.visibility = View.VISIBLE

                        btnAction2.visibility = View.GONE
                    }

                    OrderStatus.CANCELLED -> {
                        // Đã hủy: "Xem chi tiết đơn hủy", "Mua lại"
                        btnAction1.text = "Xem chi tiết đơn hủy"
                        btnAction1.setOnClickListener { onViewDetailsClick(order) }
                        btnAction1.visibility = View.VISIBLE

                        btnAction2.text = "Mua lại"
                        btnAction2.setOnClickListener { onBuyAgainClick(order) }
                        btnAction2.visibility = View.VISIBLE
                    }
                }
            }
        }

        private fun formatPrice(price: Int): String {
            val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
            return "${formatter.format(price)}đ"
        }

        private fun formatDate(timestamp: Long): String {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("vi", "VN"))
            return sdf.format(Date(timestamp))
        }
    }

    class OrderDiffCallback : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.orderId == newItem.orderId
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }
}

