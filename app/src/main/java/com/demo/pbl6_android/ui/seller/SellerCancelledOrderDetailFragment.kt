package com.demo.pbl6_android.ui.seller

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.demo.pbl6_android.databinding.FragmentSellerCancelledOrderDetailBinding
import com.demo.pbl6_android.ui.seller.model.CancelledBy
import com.demo.pbl6_android.ui.seller.model.SellerOrder
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SellerCancelledOrderDetailFragment : Fragment() {

    private var _binding: FragmentSellerCancelledOrderDetailBinding? = null
    private val binding: FragmentSellerCancelledOrderDetailBinding
        get() = _binding!!
    
    private var order: SellerOrder? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellerCancelledOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // TODO: Get order from arguments or ViewModel
        loadSampleCancelledOrder()
        
        setupToolbar()
        setupListeners()
        displayOrderDetail()
    }

    private fun setupToolbar() {
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        
        binding.btnHelp.setOnClickListener {
            showToast("Trợ giúp")
        }
    }

    private fun setupListeners() {
        binding.apply {
            btnViewRevenueDetail.setOnClickListener {
                showToast("Xem chi tiết doanh thu")
            }
            
            buyerInfoContainer.setOnClickListener {
                showToast("Xem thông tin người mua")
            }
            
            btnAddNote.setOnClickListener {
                showToast("Thêm ghi chú")
            }
            
            btnContactBuyerTop.setOnClickListener {
                contactBuyer()
            }
            
            btnContactBuyer.setOnClickListener {
                contactBuyer()
            }
            
            // Copy order code
            tvOrderCode.setOnClickListener {
                copyOrderCode()
            }
        }
    }

    private fun displayOrderDetail() {
        order?.let { order ->
            binding.apply {
                // Status Header - Show who cancelled and reason
                val cancelledByText = when (order.cancelledBy) {
                    CancelledBy.BUYER -> "Đã hủy bởi người mua"
                    CancelledBy.SELLER -> "Đã hủy bởi người bán"
                    else -> "Đã hủy"
                }
                tvOrderStatus.text = cancelledByText
                
                // Cancellation info with reason
                val cancellerText = when (order.cancelledBy) {
                    CancelledBy.BUYER -> "Người mua"
                    CancelledBy.SELLER -> "Người bán"
                    else -> "Người dùng"
                }
                val reasonText = order.cancellationReason?.displayName ?: "Không rõ lý do"
                tvCancellationInfo.text = "$cancellerText đã hủy đơn hàng này. Lý do hủy: $reasonText"
                
                order.cancellationDate?.let {
                    tvCancellationTime.text = "Thời gian hủy: ${formatDate(it)}"
                }
                
                // Delivery Address (masked for privacy)
                val address = maskAddress(order.buyerName)
                tvBuyerAddress.text = address
                
                // Payment Info - All amounts are 0 for cancelled orders
                tvProductTotal.text = "đ0"
                tvShippingFee.text = "đ0"
                tvTransactionFee.text = "đ0"
                tvServiceFee.text = "đ0"
                tvRevenueDetail.text = "đ0 >"
                tvAdjustment.text = "đ0"
                tvFinalRevenue.text = "đ0"
                
                // Buyer payment - only this field shows actual amount
                val firstProduct = order.products.first()
                val shippingFee = (order.totalAmount * 0.07).toInt() // 7% example
                val buyerPayment = order.totalAmount + shippingFee
                tvBuyerPayment.text = formatCurrency(buyerPayment)
                tvPaymentMethod.text = "TK Ngân hàng liên kết ShopeePay"
                
                // Buyer & Product Info
                tvBuyerName.text = order.buyerName
                tvProductName.text = firstProduct.productName
                tvProductQuantity.text = "x${firstProduct.quantity}"
                tvProductPrice.text = formatCurrency(firstProduct.price)
                
                // Order Info
                tvOrderCode.text = order.orderCode.replace("#", "")
                tvOrderDate.text = formatDate(order.orderDate)
                
                order.cancellationDate?.let {
                    tvCancellationDate.text = formatDate(it)
                }
            }
        }
    }

    private fun loadSampleCancelledOrder() {
        // TODO: Load from arguments or ViewModel
        val currentTime = System.currentTimeMillis()
        order = com.demo.pbl6_android.ui.seller.model.SellerOrder(
            orderId = "1",
            orderCode = "#2205158FE9BTJJ",
            buyerName = "utphongcomputer",
            status = com.demo.pbl6_android.ui.seller.model.SellerOrderStatus.CANCELLED,
            products = listOf(
                com.demo.pbl6_android.ui.seller.model.OrderProduct(
                    productId = "p1",
                    productName = "Android TV Box X96 mini plus",
                    imageUrl = "",
                    variant = "",
                    quantity = 1,
                    price = 400000
                )
            ),
            totalAmount = 400000,
            shippingMethod = com.demo.pbl6_android.ui.seller.model.ShippingMethod.EXPRESS,
            orderDate = currentTime - 15 * 24 * 60 * 60 * 1000,
            cancelledBy = CancelledBy.BUYER,
            cancellationReason = com.demo.pbl6_android.ui.seller.model.CancellationReason.CHANGED_MIND,
            cancellationDate = currentTime - 14 * 24 * 60 * 60 * 1000
        )
    }

    private fun maskAddress(name: String): String {
        // Mask sensitive information for privacy
        val maskedName = if (name.length > 3) {
            name.substring(0, 2) + "*".repeat(name.length - 3) + name.last()
        } else {
            name
        }
        return "$maskedName\n******83\n******, Thị Trấn Hưng Lợi, Huyện Thanh Trì, Sóc Trăng"
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd 'Th'MM yyyy HH:mm", Locale("vi", "VN"))
        return sdf.format(Date(timestamp))
    }

    private fun formatCurrency(amount: Int): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        return formatter.format(amount).replace("₫", "đ")
    }

    private fun copyOrderCode() {
        order?.let {
            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Order Code", it.orderCode.replace("#", ""))
            clipboard.setPrimaryClip(clip)
            showToast("Đã sao chép mã đơn hàng")
        }
    }

    private fun contactBuyer() {
        showToast("Liên hệ người mua: ${order?.buyerName}")
        // TODO: Navigate to chat with buyer
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

