package com.demo.pbl6_android.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.pbl6_android.R
import com.demo.pbl6_android.data.OrderRepository
import com.demo.pbl6_android.data.ProductRepository
import com.demo.pbl6_android.data.model.Order
import com.demo.pbl6_android.data.model.OrderStatus
import com.demo.pbl6_android.databinding.FragmentOrderDetailBinding
import com.demo.pbl6_android.ui.order.adapter.OrderDetailProductAdapter
import com.demo.pbl6_android.ui.product.adapter.RelatedProductAdapter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class OrderDetailFragment : Fragment() {

    private var _binding: FragmentOrderDetailBinding? = null
    private val binding: FragmentOrderDetailBinding
        get() = _binding!!

    private var order: Order? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupViews()
        loadOrderDetails()
        loadRecommendedProducts()
    }

    private fun setupViews() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        
        binding.btnMenu.setOnClickListener {
            showOrderMenu()
        }
    }

    private fun loadOrderDetails() {
        val orderId = arguments?.getString("orderId") ?: return
        
        order = OrderRepository.getOrderById(orderId)
        
        order?.let { displayOrder(it) }
    }

    private fun displayOrder(order: Order) {
        binding.apply {
            // Order status section
            displayOrderStatus(order)
            
            // Shipping address
            displayShippingAddress(order)
            
            // Products
            displayProducts(order)
            
            // Payment method
            displayPaymentMethod(order)
            
            // Order summary
            displayOrderSummary(order)
            
            // Order info
            displayOrderInfo(order)
            
            // Action buttons
            configureActionButtons(order)
        }
    }

    private fun displayOrderStatus(order: Order) {
        binding.apply {
            // Status badge
            tvStatusBadge.text = order.status.displayName
            val statusColor = android.graphics.Color.parseColor(order.status.color)
            cardStatusBadge.setCardBackgroundColor(getStatusBackgroundColor(order.status))
            tvStatusBadge.setTextColor(statusColor)
            statusIndicator.backgroundTintList = android.content.res.ColorStateList.valueOf(statusColor)
            
            // Status title and time
            tvStatusTitle.text = getStatusTitle(order.status)
            tvStatusTime.text = formatDateTime(order.orderDate)
            
            // Shipping info - only show for relevant statuses
            when (order.status) {
                OrderStatus.SHIPPING, OrderStatus.DELIVERED -> {
                    layoutShippingInfo.visibility = View.VISIBLE
                    tvShippingUnit.text = "Đơn vị vận chuyển: Giao hàng nhanh"
                    tvTrackingCode.text = "Mã vận đơn: GHN${order.orderId.takeLast(9).uppercase()}"
                    tvShipperInfo.text = "Shipper: Nguyễn Văn A - 0987654321"
                }
                else -> {
                    layoutShippingInfo.visibility = View.GONE
                }
            }
        }
    }

    private fun displayShippingAddress(order: Order) {
        binding.apply {
            tvRecipientName.text = "Nguyễn Thị B"
            tvPhoneNumber.text = "0123456789"
            tvFullAddress.text = order.shippingAddress
            tvAddressType.text = "Nhà riêng"
        }
    }

    private fun displayProducts(order: Order) {
        val adapter = OrderDetailProductAdapter(order.items)
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
    }

    private fun displayPaymentMethod(order: Order) {
        binding.apply {
            when (order.paymentMethod) {
                "COD", "Thanh toán khi nhận hàng" -> {
                    tvPaymentType.text = "Thanh toán khi nhận hàng"
                    tvCardNumber.text = "Tiền mặt"
                    layoutCardBadge.visibility = View.GONE
                }
                "Bank Transfer", "Chuyển khoản ngân hàng" -> {
                    tvPaymentType.text = "Thẻ tín dụng"
                    tvCardNumber.text = "**** **** **** 1234"
                    layoutCardBadge.visibility = View.VISIBLE
                    tvCardBadge.text = "VISA"
                }
                else -> {
                    tvPaymentType.text = order.paymentMethod
                    tvCardNumber.text = ""
                    layoutCardBadge.visibility = View.GONE
                }
            }
            
            // Payment status
            when (order.status) {
                OrderStatus.PENDING_PAYMENT -> {
                    tvPaymentStatus.text = "Chưa thanh toán"
                    tvPaymentStatus.setTextColor(requireContext().getColor(R.color.text_secondary))
                    cardPaymentStatus.setCardBackgroundColor(requireContext().getColor(R.color.background_light))
                }
                else -> {
                    tvPaymentStatus.text = "Đã thanh toán"
                    tvPaymentStatus.setTextColor(android.graphics.Color.parseColor("#16A34A"))
                    cardPaymentStatus.setCardBackgroundColor(android.graphics.Color.parseColor("#DCFCE7"))
                }
            }
        }
    }

    private fun displayOrderSummary(order: Order) {
        binding.apply {
            val itemCount = order.items.sumOf { it.quantity }
            tvProductCount.text = "Tạm tính ($itemCount sản phẩm)"
            
            val subtotal = order.items.sumOf { it.price * it.quantity }
            tvSubtotal.text = formatPrice(subtotal)
            
            val shippingFee = 30000
            tvShippingFee.text = formatPrice(shippingFee)
            
            val discount = 50000
            tvDiscount.text = "-${formatPrice(discount)}"
            
            val total = subtotal + shippingFee - discount
            tvTotalAmount.text = formatPrice(total)
        }
    }

    private fun displayOrderInfo(order: Order) {
        binding.apply {
            tvOrderCode.text = "#${order.orderCode}"
            tvOrderDate.text = formatDateTime(order.orderDate)
        }
    }

    private fun configureActionButtons(order: Order) {
        binding.apply {
            when (order.status) {
                OrderStatus.PENDING_PAYMENT -> {
                    btnAction1.text = "Thanh toán ngay"
                    btnAction1.visibility = View.VISIBLE
                    btnAction1.setOnClickListener { handlePayNow(order) }
                    
                    btnAction2.text = "Hủy đơn hàng"
                    btnAction2.visibility = View.VISIBLE
                    btnAction2.setOnClickListener { handleCancelOrder(order) }
                    
                    // Change button styles
                    btnAction1.setBackgroundColor(requireContext().getColor(R.color.primary))
                    btnAction1.setTextColor(requireContext().getColor(R.color.white))
                }
                
                OrderStatus.PENDING_PICKUP -> {
                    btnAction1.text = "Liên hệ shop"
                    btnAction1.visibility = View.VISIBLE
                    btnAction1.setOnClickListener { handleContactShop(order) }
                    
                    btnAction2.text = "Hủy đơn hàng"
                    btnAction2.visibility = View.VISIBLE
                    btnAction2.setOnClickListener { handleCancelOrder(order) }
                }
                
                OrderStatus.SHIPPING -> {
                    btnAction1.text = "Liên hệ shop"
                    btnAction1.visibility = View.VISIBLE
                    btnAction1.setOnClickListener { handleContactShop(order) }
                    
                    btnAction2.text = "Đã nhận được hàng"
                    btnAction2.visibility = View.VISIBLE
                    btnAction2.isEnabled = false
                    btnAction2.alpha = 0.5f
                    btnAction2.setOnClickListener { handleOrderReceived(order) }
                }
                
                OrderStatus.DELIVERED -> {
                    val daysSinceReceived = order.receivedDate?.let {
                        TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - it)
                    } ?: 0
                    
                    if (daysSinceReceived <= 7) {
                        btnAction1.text = "Trả hàng/Hoàn tiền"
                        btnAction1.setOnClickListener { handleReturnRefund(order) }
                    } else {
                        btnAction1.text = "Mua lại"
                        btnAction1.setOnClickListener { handleBuyAgain(order) }
                    }
                    btnAction1.visibility = View.VISIBLE
                    
                    btnAction2.text = "Đánh giá"
                    btnAction2.visibility = View.VISIBLE
                    btnAction2.setOnClickListener { handleReview(order) }
                }
                
                OrderStatus.RETURN_REFUND -> {
                    btnAction1.text = "Xem chi tiết"
                    btnAction1.visibility = View.VISIBLE
                    btnAction1.setOnClickListener { showToast("Xem chi tiết trả hàng") }
                    
                    btnAction2.visibility = View.GONE
                }
                
                OrderStatus.CANCELLED -> {
                    btnAction1.text = "Xem chi tiết đơn hủy"
                    btnAction1.visibility = View.VISIBLE
                    btnAction1.setOnClickListener { showToast("Xem chi tiết đơn hủy") }
                    
                    btnAction2.text = "Mua lại"
                    btnAction2.visibility = View.VISIBLE
                    btnAction2.setOnClickListener { handleBuyAgain(order) }
                }
            }
        }
    }

    private fun loadRecommendedProducts() {
        val products = ProductRepository.getAllProducts().take(4)
        
        val adapter = RelatedProductAdapter(products) { product ->
            val bundle = Bundle().apply {
                putString("productId", product.id)
            }
            findNavController().navigate(R.id.action_orderDetailFragment_to_productDetailFragment, bundle)
        }
        
        binding.rvRecommendedProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            this.adapter = adapter
        }
    }

    private fun getStatusTitle(status: OrderStatus): String {
        return when (status) {
            OrderStatus.PENDING_PAYMENT -> "Chờ thanh toán"
            OrderStatus.PENDING_PICKUP -> "Chờ lấy hàng"
            OrderStatus.SHIPPING -> "Đang giao hàng"
            OrderStatus.DELIVERED -> "Đã giao hàng thành công"
            OrderStatus.RETURN_REFUND -> "Đang xử lý trả hàng/hoàn tiền"
            OrderStatus.CANCELLED -> "Đơn hàng đã bị hủy"
        }
    }

    private fun getStatusBackgroundColor(status: OrderStatus): Int {
        return when (status) {
            OrderStatus.PENDING_PAYMENT -> android.graphics.Color.parseColor("#FEF3C7")
            OrderStatus.PENDING_PICKUP -> android.graphics.Color.parseColor("#DBEAFE")
            OrderStatus.SHIPPING -> android.graphics.Color.parseColor("#FEE2E2")
            OrderStatus.DELIVERED -> android.graphics.Color.parseColor("#DCFCE7")
            OrderStatus.RETURN_REFUND -> android.graphics.Color.parseColor("#FEF3C7")
            OrderStatus.CANCELLED -> android.graphics.Color.parseColor("#F1F5F9")
        }
    }

    private fun showOrderMenu() {
        val options = arrayOf("Sao chép mã đơn hàng", "Chia sẻ đơn hàng")
        
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Tùy chọn")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> copyOrderCode()
                    1 -> shareOrder()
                }
            }
            .show()
    }

    private fun copyOrderCode() {
        order?.let { order ->
            val clipboard = requireContext().getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText("Order Code", order.orderCode)
            clipboard.setPrimaryClip(clip)
            showToast("Đã sao chép mã đơn hàng")
        }
    }

    private fun shareOrder() {
        showToast("Chức năng chia sẻ đang phát triển")
    }

    private fun handlePayNow(order: Order) {
        showToast("Chức năng thanh toán đang phát triển")
    }

    private fun handleCancelOrder(order: Order) {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Hủy đơn hàng")
            .setMessage("Bạn có chắc chắn muốn hủy đơn hàng này?")
            .setPositiveButton("Hủy đơn") { _, _ ->
                showToast("Đã hủy đơn hàng")
                findNavController().navigateUp()
            }
            .setNegativeButton("Không", null)
            .show()
    }

    private fun handleContactShop(order: Order) {
        // Find or create conversation with this shop
        val conversations = com.demo.pbl6_android.data.ChatManager.conversations.value
        val existingConversation = conversations.find { it.shopId == order.shopId }
        
        val conversationId = existingConversation?.id ?: "conv_${order.shopId}"
        
        val bundle = Bundle().apply {
            putString("conversationId", conversationId)
        }
        findNavController().navigate(R.id.action_orderDetailFragment_to_chatFragment, bundle)
    }

    private fun handleOrderReceived(order: Order) {
        showToast("Xác nhận đã nhận hàng")
    }

    private fun handleReturnRefund(order: Order) {
        showToast("Chức năng trả hàng/hoàn tiền đang phát triển")
    }

    private fun handleReview(order: Order) {
        showToast("Chức năng đánh giá đang phát triển")
    }

    private fun handleBuyAgain(order: Order) {
        showToast("Chức năng mua lại đang phát triển")
    }

    private fun formatPrice(price: Int): String {
        return "%,dđ".format(price).replace(",", ".")
    }

    private fun formatDateTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale("vi", "VN"))
        return sdf.format(Date(timestamp))
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

