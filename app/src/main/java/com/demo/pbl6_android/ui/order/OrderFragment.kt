package com.demo.pbl6_android.ui.order

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.pbl6_android.R
import com.demo.pbl6_android.data.AddressManager
import com.demo.pbl6_android.data.CartManager
import com.demo.pbl6_android.data.OrderRepository
import com.demo.pbl6_android.data.api.ApiResult
import com.demo.pbl6_android.data.model.Address
import com.demo.pbl6_android.databinding.FragmentOrderBinding
import com.demo.pbl6_android.ui.order.adapter.OrderShopAdapter
import com.demo.pbl6_android.ui.order.model.OrderProduct
import com.demo.pbl6_android.ui.order.model.OrderShop
import com.demo.pbl6_android.ui.order.model.PaymentMethod
import com.demo.pbl6_android.ui.order.model.ShippingAddress
import com.demo.pbl6_android.ui.order.model.ShippingMethod
import com.demo.pbl6_android.ui.shipping.ShippingMethodData
import kotlinx.coroutines.launch

class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null
    private val binding: FragmentOrderBinding
        get() = _binding!!

    private lateinit var orderShopAdapter: OrderShopAdapter
    private val orderShops = mutableListOf<OrderShop>()

    private var shippingAddress: ShippingAddress? = null
    private var selectedShippingMethod: ShippingMethod? = null
    private var selectedPaymentMethod: PaymentMethod = PaymentMethod.BANK_TRANSFER
    private var selectedShopVoucher: com.demo.pbl6_android.data.model.Voucher? = null
    private var selectedPlatformShippingVoucher: com.demo.pbl6_android.data.model.Voucher? = null
    private var selectedPlatformDiscountVoucher: com.demo.pbl6_android.data.model.Voucher? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupRecyclerView()
        loadSampleData()
        setupPaymentMethods()
        observeVouchers()
        calculateTotals()
    }

    override fun onResume() {
        super.onResume()
        // Reload data to fix disappearing items bug
        loadSampleData()
        
        // Update shipping method if changed
        ShippingMethodData.selectedMethod?.let { method ->
            selectedShippingMethod = method
            displayShippingMethod(method)
            calculateTotals()
        }
        
        // Update voucher displays
        updateVoucherDisplays()
    }
    
    private fun observeVouchers() {
        viewLifecycleOwner.lifecycleScope.launch {
            com.demo.pbl6_android.data.VoucherManager.selectedShopVoucher.collect { voucher ->
                selectedShopVoucher = voucher
                updateVoucherDisplays()
                calculateTotals()
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            com.demo.pbl6_android.data.VoucherManager.selectedPlatformShippingVoucher.collect { voucher ->
                selectedPlatformShippingVoucher = voucher
                updateVoucherDisplays()
                calculateTotals()
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            com.demo.pbl6_android.data.VoucherManager.selectedPlatformDiscountVoucher.collect { voucher ->
                selectedPlatformDiscountVoucher = voucher
                updateVoucherDisplays()
                calculateTotals()
            }
        }
    }
    
    private fun updateVoucherDisplays() {
        // Get shipping price and product total outside binding scope
        val shippingPrice: Int = selectedShippingMethod?.price ?: 0
        var productTotal = 0
        orderShops.forEach { shop ->
            shop.products.forEach { product ->
                productTotal += product.currentPrice * product.quantity
            }
        }
        
        binding.apply {
            // Update platform voucher display (combined)
            val hasShippingVoucher = selectedPlatformShippingVoucher != null
            val hasDiscountVoucher = selectedPlatformDiscountVoucher != null
            
            when {
                hasShippingVoucher && hasDiscountVoucher -> {
                    val shippingDiscount = com.demo.pbl6_android.data.VoucherManager.calculateDiscount(
                        shippingPrice, 
                        selectedPlatformShippingVoucher
                    )
                    val productDiscount = com.demo.pbl6_android.data.VoucherManager.calculateDiscount(
                        productTotal, 
                        selectedPlatformDiscountVoucher
                    )
                    tvSelectPlatformVoucher.text = "2 voucher đã chọn"
                    tvSelectPlatformVoucher.setTextColor(android.graphics.Color.parseColor("#4318D1"))
                }
                hasShippingVoucher -> {
                    val discount = com.demo.pbl6_android.data.VoucherManager.calculateDiscount(
                        shippingPrice, 
                        selectedPlatformShippingVoucher
                    )
                    if (discount >= shippingPrice) {
                        tvSelectPlatformVoucher.text = "Miễn phí vận chuyển"
                    } else {
                        tvSelectPlatformVoucher.text = "-${formatPrice(discount)}"
                    }
                    tvSelectPlatformVoucher.setTextColor(android.graphics.Color.parseColor("#4CAF50"))
                }
                hasDiscountVoucher -> {
                    val discount = com.demo.pbl6_android.data.VoucherManager.calculateDiscount(
                        productTotal, 
                        selectedPlatformDiscountVoucher
                    )
                    tvSelectPlatformVoucher.text = "-${formatPrice(discount)}"
                    tvSelectPlatformVoucher.setTextColor(android.graphics.Color.parseColor("#9C27B0"))
                }
                else -> {
                    tvSelectPlatformVoucher.text = "Chọn mã"
                    tvSelectPlatformVoucher.setTextColor(android.graphics.Color.parseColor("#4318D1"))
                }
            }
            
            // Update shop voucher display in adapter
            if (::orderShopAdapter.isInitialized) {
                orderShopAdapter.updateShopVoucher(selectedShopVoucher)
            }
        }
    }

    private fun setupViews() {
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            tvChangeAddress.setOnClickListener {
                findNavController().navigate(R.id.action_orderFragment_to_addressSelectionFragment)
            }

            tvViewAllShipping.setOnClickListener {
                findNavController().navigate(R.id.action_orderFragment_to_shippingMethodFragment)
            }

            layoutPlatformVoucher.setOnClickListener {
                findNavController().navigate(R.id.action_orderFragment_to_platformVoucherSelectionFragment)
            }

            btnPlaceOrder.setOnClickListener {
                placeOrder()
            }
        }
    }

    private fun setupRecyclerView() {
        orderShopAdapter = OrderShopAdapter(
            shops = orderShops,
            onSelectShopVoucher = { shop ->
                findNavController().navigate(R.id.action_orderFragment_to_shopVoucherFragment)
            },
            onAddShopNote = { shop ->
                showShopNoteDialog(shop)
            }
        )

        binding.rvShopOrders.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderShopAdapter
        }
    }

    private fun showShopNoteDialog(shop: OrderShop) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_shop_note, null)
        
        val dialog = android.app.AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
        
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        
        val etNote = dialogView.findViewById<android.widget.EditText>(R.id.et_note)
        val tvCharCount = dialogView.findViewById<android.widget.TextView>(R.id.tv_char_count)
        val btnCancel = dialogView.findViewById<android.widget.Button>(R.id.btn_cancel)
        val btnSave = dialogView.findViewById<android.widget.Button>(R.id.btn_save)
        
        // Set existing note if any
        etNote.setText(shop.noteToShop ?: "")
        tvCharCount.text = "${etNote.text.length}/200"
        
        // Update character count
        etNote.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tvCharCount.text = "${s?.length ?: 0}/200"
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })
        
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        
        btnSave.setOnClickListener {
            val note = etNote.text.toString().trim()
            orderShopAdapter.updateShopNote(shop.shopId, note)
            dialog.dismiss()
        }
        
        dialog.show()
    }

    private fun loadSampleData() {
        shippingAddress = ShippingAddress(
            recipientName = "Nguyễn Văn A",
            phoneNumber = "0123456789",
            fullAddress = "123 Đường ABC, Phường XYZ, Quận 1, TP.HCM",
            isDefault = true
        )

        displayShippingAddress(shippingAddress!!)

        // Initialize or get saved shipping method
        selectedShippingMethod = ShippingMethodData.selectedMethod ?: ShippingMethod(
            id = "standard",
            name = "Giao hàng tiêu chuẩn",
            estimatedDays = "3-5 ngày",
            price = 30000,
            isSelected = true
        ).also {
            ShippingMethodData.selectedMethod = it
        }

        displayShippingMethod(selectedShippingMethod!!)

        orderShops.clear()

        // Get selected products from cart
        val cartShops = OrderData.selectedShops
        cartShops.forEach { cartShop ->
            val products = cartShop.products.map { cartProduct ->
                OrderProduct(
                    id = cartProduct.id,
                    name = cartProduct.name,
                    color = cartProduct.color,
                    size = cartProduct.size,
                    currentPrice = cartProduct.currentPrice,
                    originalPrice = cartProduct.originalPrice,
                    quantity = cartProduct.quantity,
                    imageUrl = cartProduct.imageUrl
                )
            }
            
            orderShops.add(
                OrderShop(
                    shopId = cartShop.id,
                    shopName = cartShop.name,
                    products = products
                )
            )
        }

        orderShopAdapter.notifyDataSetChanged()
    }

    private fun displayShippingAddress(address: ShippingAddress) {
        binding.apply {
            tvRecipientInfo.text = "${address.recipientName} | ${address.phoneNumber}"
            tvFullAddress.text = address.fullAddress
        }
    }

    private fun displayShippingMethod(method: ShippingMethod) {
        binding.apply {
            tvShippingName.text = method.name
            tvShippingDays.text = method.estimatedDays
            tvShippingPrice.text = formatPrice(method.price)
        }
    }

    private fun setupPaymentMethods() {
        binding.apply {
            layoutCod.setOnClickListener {
                selectPaymentMethod(PaymentMethod.CASH_ON_DELIVERY)
            }

            layoutBankTransfer.setOnClickListener {
                selectPaymentMethod(PaymentMethod.BANK_TRANSFER)
            }

            rbCod.setOnClickListener {
                selectPaymentMethod(PaymentMethod.CASH_ON_DELIVERY)
            }

            rbBankTransfer.setOnClickListener {
                selectPaymentMethod(PaymentMethod.BANK_TRANSFER)
            }
        }
    }

    private fun selectPaymentMethod(method: PaymentMethod) {
        selectedPaymentMethod = method

        binding.apply {
            rbCod.isChecked = method == PaymentMethod.CASH_ON_DELIVERY
            rbBankTransfer.isChecked = method == PaymentMethod.BANK_TRANSFER

            layoutCod.setBackgroundResource(
                if (method == PaymentMethod.CASH_ON_DELIVERY)
                    com.demo.pbl6_android.R.drawable.bg_payment_selected
                else
                    com.demo.pbl6_android.R.drawable.bg_payment_method
            )

            layoutBankTransfer.setBackgroundResource(
                if (method == PaymentMethod.BANK_TRANSFER)
                    com.demo.pbl6_android.R.drawable.bg_payment_selected
                else
                    com.demo.pbl6_android.R.drawable.bg_payment_method
            )
        }
    }

    private fun calculateTotals() {
        var productTotal = 0

        orderShops.forEach { shop ->
            shop.products.forEach { product ->
                productTotal += product.currentPrice * product.quantity
            }
        }

        val originalShippingTotal = selectedShippingMethod?.price ?: 0
        var shippingTotal = originalShippingTotal
        
        // Apply platform shipping voucher
        val shippingDiscount = com.demo.pbl6_android.data.VoucherManager.calculateDiscount(
            originalShippingTotal, 
            selectedPlatformShippingVoucher
        )
        shippingTotal -= shippingDiscount
        if (shippingTotal < 0) shippingTotal = 0
        
        // Apply platform discount voucher
        val platformProductDiscount = com.demo.pbl6_android.data.VoucherManager.calculateDiscount(
            productTotal, 
            selectedPlatformDiscountVoucher
        )
        
        // Apply shop voucher
        val shopDiscount = com.demo.pbl6_android.data.VoucherManager.calculateDiscount(
            productTotal, 
            selectedShopVoucher
        )
        
        // Calculate totals
        val totalProductDiscount = platformProductDiscount + shopDiscount
        val grandTotal = productTotal + shippingTotal - totalProductDiscount
        
        binding.apply {
            tvProductTotal.text = formatPrice(productTotal)
            
            // Show shipping with strikethrough if discounted
            if (shippingDiscount > 0 && shippingTotal < originalShippingTotal) {
                tvShippingTotal.text = android.text.Html.fromHtml(
                    "<strike>${formatPrice(originalShippingTotal)}</strike> ${formatPrice(shippingTotal)}",
                    android.text.Html.FROM_HTML_MODE_LEGACY
                )
            } else {
                tvShippingTotal.text = formatPrice(originalShippingTotal)
            }
            
            // Show shipping discount row
            if (shippingDiscount > 0) {
                layoutShippingDiscount.visibility = View.VISIBLE
                tvShippingDiscount.text = "-${formatPrice(shippingDiscount)}"
            } else {
                layoutShippingDiscount.visibility = View.GONE
            }
            
            // Show product discount row
            if (totalProductDiscount > 0) {
                layoutProductDiscount.visibility = View.VISIBLE
                tvProductDiscount.text = "-${formatPrice(totalProductDiscount)}"
            } else {
                layoutProductDiscount.visibility = View.GONE
            }
            
            tvGrandTotal.text = formatPrice(grandTotal)
            tvBottomGrandTotal.text = formatPrice(grandTotal)
        }
    }

    private fun formatPrice(price: Int): String {
        return "%,dđ".format(price).replace(",", ".")
    }

    private fun placeOrder() {
        // Validate inputs
        if (shippingAddress == null) {
            Toast.makeText(requireContext(), "Vui lòng chọn địa chỉ giao hàng", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedShippingMethod == null) {
            Toast.makeText(requireContext(), "Vui lòng chọn phương thức vận chuyển", Toast.LENGTH_SHORT).show()
            return
        }

        if (orderShops.isEmpty()) {
            Toast.makeText(requireContext(), "Giỏ hàng trống", Toast.LENGTH_SHORT).show()
            return
        }

        // Show loading dialog
        val progressDialog = ProgressDialog(requireContext()).apply {
            setMessage("Đang đặt hàng...")
            setCancelable(false)
            show()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            // Get user's address from AddressManager
            val address = AddressManager.getDefaultAddress()
            
            if (address == null) {
                progressDialog.dismiss()
                Toast.makeText(
                    requireContext(),
                    "Vui lòng thêm địa chỉ giao hàng trong cài đặt",
                    Toast.LENGTH_LONG
                ).show()
                return@launch
            }

            // Determine payment method
            val paymentMethodString = when (selectedPaymentMethod) {
                PaymentMethod.CASH_ON_DELIVERY -> "COD"
                PaymentMethod.BANK_TRANSFER -> "BANK_TRANSFER"
            }

            // Get promotion code if selected
            val promotionCode = selectedPlatformDiscountVoucher?.code

            // Call checkout API
            val result = OrderRepository.checkout(
                address = address,
                paymentMethod = paymentMethodString,
                note = null, // You can add a note input field if needed
                promotionCode = promotionCode
            )

            progressDialog.dismiss()

            when (result) {
                is ApiResult.Success -> {
                    // Clear cart after successful checkout
                    CartManager.clearCart()

                    // Get first order ID for navigation
                    val orderId = result.data.orders.firstOrNull()?.id ?: ""

                    Toast.makeText(
                        requireContext(),
                        "Đặt hàng thành công!",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Navigate to order status/success screen
                    val bundle = Bundle().apply {
                        putString("orderId", orderId)
                        putString("paymentMethod", paymentMethodString)
                    }

                    findNavController().navigate(
                        R.id.action_orderFragment_to_orderStatusFragment,
                        bundle
                    )
                }
                is ApiResult.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Lỗi đặt hàng: ${result.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                is ApiResult.Loading -> {
                    // Already showing loading dialog
                }
            }
        }
    }

    private fun generateOrderId(): String {
        return "ORDER${System.currentTimeMillis().toString().takeLast(8)}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Don't clear order data here to prevent data loss when navigating
        // OrderData.clearOrderData()
        // Don't clear shipping method to maintain selection
        // ShippingMethodData.selectedMethod = null
        _binding = null
    }
}

