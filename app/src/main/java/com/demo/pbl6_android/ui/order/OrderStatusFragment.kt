package com.demo.pbl6_android.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.demo.pbl6_android.R
import com.demo.pbl6_android.databinding.FragmentOrderStatusBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderStatusFragment : Fragment() {

    private var _binding: FragmentOrderStatusBinding? = null
    private val binding: FragmentOrderStatusBinding
        get() = _binding!!

    private var paymentMethod: String? = null
    private var orderId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadArguments()
        setupViews()
        displayOrderStatus()
    }

    private fun loadArguments() {
        paymentMethod = arguments?.getString("paymentMethod")
        orderId = arguments?.getString("orderId") ?: generateOrderId()
    }

    private fun setupViews() {
        binding.apply {
            btnViewOrder.setOnClickListener {
                navigateToOrderHistory()
            }

            btnBackToHome.setOnClickListener {
                navigateToHome()
            }
        }
    }

    private fun displayOrderStatus() {
        binding.apply {
            tvOrderId.text = "#$orderId"
            tvOrderDate.text = getCurrentDateTime()
            
            // Calculate and display total from OrderData or use placeholder
            val total = calculateTotal()
            tvTotalAmount.text = formatPrice(total)

            when (paymentMethod) {
                "CASH_ON_DELIVERY" -> {
                    ivStatusIcon.setImageResource(R.drawable.ic_pending_payment)
                    tvStatusTitle.text = "Đang chờ thanh toán"
                    tvStatusMessage.text = "Đơn hàng của bạn đã được tạo thành công.\nVui lòng thanh toán khi nhận hàng."
                    tvPaymentMethod.text = "Thanh toán khi nhận hàng"
                }
                "BANK_TRANSFER" -> {
                    ivStatusIcon.setImageResource(R.drawable.ic_payment_success)
                    tvStatusTitle.text = "Đã thanh toán"
                    tvStatusMessage.text = "Đơn hàng của bạn đã được thanh toán thành công.\nCảm ơn bạn đã mua hàng!"
                    tvPaymentMethod.text = "Chuyển khoản ngân hàng"
                }
                else -> {
                    ivStatusIcon.setImageResource(R.drawable.ic_pending_payment)
                    tvStatusTitle.text = "Đơn hàng đã được tạo"
                    tvStatusMessage.text = "Đơn hàng của bạn đang được xử lý."
                    tvPaymentMethod.text = "Chưa xác định"
                }
            }
        }
    }

    private fun calculateTotal(): Int {
        var total = 0
        val orderShops = OrderData.selectedShops
        
        orderShops.forEach { shop ->
            shop.products.forEach { product ->
                total += product.currentPrice * product.quantity
            }
        }
        
        // Add shipping fee if available
        com.demo.pbl6_android.ui.shipping.ShippingMethodData.selectedMethod?.let {
            total += it.price
        }
        
        return total
    }

    private fun generateOrderId(): String {
        return "ORDER${System.currentTimeMillis().toString().takeLast(8)}"
    }

    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun formatPrice(price: Int): String {
        return "₫%,d".format(price).replace(",", ".")
    }

    private fun navigateToOrderHistory() {
        // Navigate to order detail
        val bundle = Bundle().apply {
            putString("orderId", orderId)
            putInt("initialTab", 0) // Tab "Chờ lấy hàng"
        }
        findNavController().navigate(R.id.action_orderStatusFragment_to_orderHistoryFragment, bundle)
    }

    private fun navigateToHome() {
        findNavController().navigate(R.id.landingPageFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear order data after successful order
        OrderData.clearOrderData()
        com.demo.pbl6_android.ui.shipping.ShippingMethodData.selectedMethod = null
        _binding = null
    }
}

