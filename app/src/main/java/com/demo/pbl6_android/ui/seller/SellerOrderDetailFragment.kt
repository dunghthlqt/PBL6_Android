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
import com.demo.pbl6_android.databinding.FragmentSellerOrderDetailBinding
import com.demo.pbl6_android.ui.seller.model.SellerOrder
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SellerOrderDetailFragment : Fragment() {

    private var _binding: FragmentSellerOrderDetailBinding? = null
    private val binding: FragmentSellerOrderDetailBinding
        get() = _binding!!
    
    private var order: SellerOrder? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellerOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // TODO: Get order from arguments or ViewModel
        loadSampleOrder()
        
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
            btnViewShipping.setOnClickListener {
                showToast("Xem chi tiết vận chuyển")
            }
            
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
                // Status Header
                tvOrderStatus.text = order.status.displayName
                tvRatingStatus.text = if (order.isRated) "Đã có đánh giá" else "Chưa có đánh giá"
                
                order.confirmDate?.let {
                    tvCompletionTime.text = "Thời gian hoàn thành: ${formatDate(it)}"
                }
                
                // Delivery Address (masked for privacy)
                val address = maskAddress(order.buyerName)
                tvBuyerAddress.text = address
                
                // Shipping Info
                tvShippingCode.text = "${order.shippingMethod.displayName}: SPEVN2241415888250"
                tvShippingStatus.text = "Giao hàng thành công"
                order.deliveryDeadline?.let {
                    tvShippingTime.text = formatDate(it)
                }
                
                // Payment Info
                val firstProduct = order.products.first()
                tvProductTotal.text = formatCurrency(firstProduct.price * firstProduct.quantity)
                tvShippingFee.text = "đ0"
                
                val transactionFee = (order.totalAmount * 0.04).toInt() // 4% fee example
                tvTransactionFee.text = formatCurrency(-transactionFee)
                tvServiceFee.text = "đ0"
                
                val revenue = order.totalAmount - transactionFee
                tvRevenueDetail.text = "${formatCurrency(revenue)} >"
                
                tvAdjustment.text = "đ0"
                tvFinalRevenue.text = formatCurrency(revenue)
                
                // Buyer payment calculation
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
                
                order.confirmDate?.let {
                    tvPaymentDate.text = formatDate(it)
                }
                
                order.deliveryDeadline?.let {
                    tvDeliveryDate.text = formatDate(it)
                }
                
                order.confirmDate?.let {
                    tvCompletionDate.text = formatDate(it)
                }
            }
        }
    }

    private fun loadSampleOrder() {
        // TODO: Load from arguments or ViewModel
        val currentTime = System.currentTimeMillis()
        order = com.demo.pbl6_android.ui.seller.model.SellerOrder(
            orderId = "1",
            orderCode = "#2205158FE9BTJJ",
            buyerName = "utphongcomputer",
            status = com.demo.pbl6_android.ui.seller.model.SellerOrderStatus.DELIVERED,
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
            confirmDate = currentTime - 14 * 24 * 60 * 60 * 1000,
            deliveryDeadline = currentTime - 8 * 24 * 60 * 60 * 1000,
            isRated = false
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

