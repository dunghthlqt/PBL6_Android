package com.demo.pbl6_android.ui.seller.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.demo.pbl6_android.databinding.ItemSellerOrderBinding
import com.demo.pbl6_android.ui.seller.model.SellerOrder
import java.text.NumberFormat
import java.util.Locale

class SellerOrderAdapter(
    private val onViewDetailClick: (SellerOrder) -> Unit
) : ListAdapter<SellerOrder, SellerOrderAdapter.OrderViewHolder>(OrderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemSellerOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderViewHolder(binding, onViewDetailClick)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class OrderViewHolder(
        private val binding: ItemSellerOrderBinding,
        private val onViewDetailClick: (SellerOrder) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(order: SellerOrder) {
            binding.apply {
                // Buyer info
                tvBuyerName.text = order.buyerName
                tvOrderStatus.text = order.status.displayName
                
                // First product
                val firstProduct = order.products.first()
                tvProductName.text = firstProduct.productName
                tvProductQuantity.text = "x ${firstProduct.quantity}"
                
                // Variant if exists
                if (firstProduct.variant.isNotEmpty()) {
                    tvProductVariant.visibility = View.VISIBLE
                    tvProductVariant.text = firstProduct.variant
                } else {
                    tvProductVariant.visibility = View.GONE
                }
                
                // Show more products indicator
                if (order.products.size > 1) {
                    tvMoreProducts.visibility = View.VISIBLE
                    tvMoreProducts.text = "+ ${order.products.size - 1} sản phẩm khác"
                } else {
                    tvMoreProducts.visibility = View.GONE
                }
                
                // Product count and total
                val productCount = order.products.sumOf { it.quantity }
                tvProductCount.text = "$productCount sản phẩm"
                tvTotalAmount.text = formatCurrency(order.totalAmount)
                
                // Rating status for delivered orders
                if (order.status.name == "DELIVERED") {
                    tvRatingStatus.visibility = View.VISIBLE
                    tvRatingStatus.text = if (order.isRated) "Đã có đánh giá" else "Chưa có đánh giá"
                } else {
                    tvRatingStatus.visibility = View.GONE
                }
                
                // Order code
                tvOrderCode.text = "Mã đơn hàng: ${order.orderCode}"
                
                // View detail button
                btnViewDetail.setOnClickListener {
                    onViewDetailClick(order)
                }
            }
        }
        
        private fun formatCurrency(amount: Int): String {
            val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
            return formatter.format(amount).replace("₫", "đ")
        }
    }

    private class OrderDiffCallback : DiffUtil.ItemCallback<SellerOrder>() {
        override fun areItemsTheSame(oldItem: SellerOrder, newItem: SellerOrder): Boolean {
            return oldItem.orderId == newItem.orderId
        }

        override fun areContentsTheSame(oldItem: SellerOrder, newItem: SellerOrder): Boolean {
            return oldItem == newItem
        }
    }
}

