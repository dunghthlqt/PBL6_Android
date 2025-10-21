package com.demo.pbl6_android.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.pbl6_android.R
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
        binding.apply {
            // Update shipping voucher display
            if (selectedPlatformShippingVoucher != null) {
                val discount = com.demo.pbl6_android.data.VoucherManager.calculateDiscount(
                    selectedShippingMethod?.price ?: 0, 
                    selectedPlatformShippingVoucher
                )
                if (discount >= (selectedShippingMethod?.price ?: 0)) {
                    tvSelectShippingVoucher.text = "Miễn Phí Vận Chuyển"
                } else {
                    tvSelectShippingVoucher.text = "-${formatPrice(discount)}"
                }
                tvSelectShippingVoucher.setTextColor(android.graphics.Color.parseColor("#4CAF50"))
            } else {
                tvSelectShippingVoucher.text = "Chọn mã"
                tvSelectShippingVoucher.setTextColor(android.graphics.Color.parseColor("#4318D1"))
            }
            
            // Update discount voucher display
            if (selectedPlatformDiscountVoucher != null) {
                var productTotal = 0
                orderShops.forEach { shop ->
                    shop.products.forEach { product ->
                        productTotal += product.currentPrice * product.quantity
                    }
                }
                val discount = com.demo.pbl6_android.data.VoucherManager.calculateDiscount(
                    productTotal, 
                    selectedPlatformDiscountVoucher
                )
                tvSelectDiscountVoucher.text = "-${formatPrice(discount)}"
                tvSelectDiscountVoucher.setTextColor(android.graphics.Color.parseColor("#9C27B0"))
            } else {
                tvSelectDiscountVoucher.text = "Chọn mã"
                tvSelectDiscountVoucher.setTextColor(android.graphics.Color.parseColor("#4318D1"))
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

            layoutShippingVoucher.setOnClickListener {
                findNavController().navigate(R.id.action_orderFragment_to_platformVoucherFragment)
            }
            
            layoutDiscountVoucher.setOnClickListener {
                findNavController().navigate(R.id.action_orderFragment_to_platformVoucherFragment)
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
        if (shippingAddress == null) {
            android.widget.Toast.makeText(requireContext(), "Vui lòng chọn địa chỉ giao hàng", android.widget.Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedShippingMethod == null) {
            android.widget.Toast.makeText(requireContext(), "Vui lòng chọn phương thức vận chuyển", android.widget.Toast.LENGTH_SHORT).show()
            return
        }

        if (orderShops.isEmpty()) {
            android.widget.Toast.makeText(requireContext(), "Giỏ hàng trống", android.widget.Toast.LENGTH_SHORT).show()
            return
        }

        val orderId = generateOrderId()
        val paymentMethodString = when (selectedPaymentMethod) {
            PaymentMethod.CASH_ON_DELIVERY -> "CASH_ON_DELIVERY"
            PaymentMethod.BANK_TRANSFER -> "BANK_TRANSFER"
        }
        
        // Calculate totals
        var productTotal = 0
        orderShops.forEach { shop ->
            shop.products.forEach { product ->
                productTotal += product.currentPrice * product.quantity
            }
        }
        
        var shippingTotal = selectedShippingMethod?.price ?: 0
        val originalShippingTotal = shippingTotal
        
        // Apply discounts
        val platformDiscount = if (selectedPlatformVoucher != null) {
            val isShippingVoucher = selectedPlatformVoucher!!.code.contains("SHIP", ignoreCase = true) || 
                                    selectedPlatformVoucher!!.title.contains("vận chuyển", ignoreCase = true)
            
            if (isShippingVoucher) {
                val discount = com.demo.pbl6_android.data.VoucherManager.calculateDiscount(shippingTotal, selectedPlatformVoucher)
                shippingTotal -= discount
                if (shippingTotal < 0) shippingTotal = 0
                discount
            } else {
                com.demo.pbl6_android.data.VoucherManager.calculateDiscount(productTotal, selectedPlatformVoucher)
            }
        } else {
            0
        }
        
        val shopDiscount = com.demo.pbl6_android.data.VoucherManager.calculateDiscount(productTotal, selectedShopVoucher)
        val totalDiscount = platformDiscount + shopDiscount
        val grandTotal = productTotal + shippingTotal - totalDiscount

        // Create order object
        val firstShop = orderShops.firstOrNull()
        val order = com.demo.pbl6_android.data.model.Order(
            orderId = orderId,
            orderCode = orderId,
            orderDate = System.currentTimeMillis(),
            status = if (selectedPaymentMethod == PaymentMethod.CASH_ON_DELIVERY) 
                        com.demo.pbl6_android.data.model.OrderStatus.PENDING_PICKUP 
                     else 
                        com.demo.pbl6_android.data.model.OrderStatus.PENDING_PICKUP,
            items = orderShops.flatMap { shop ->
                shop.products.map { product ->
                    com.demo.pbl6_android.data.model.OrderItem(
                        productId = product.id,
                        productName = product.name,
                        productImage = product.imageUrl,
                        color = product.color,
                        size = product.size,
                        price = product.currentPrice,
                        quantity = product.quantity
                    )
                }
            },
            totalAmount = grandTotal,
            shopId = firstShop?.shopId ?: "",
            shopName = firstShop?.shopName ?: "",
            shippingAddress = shippingAddress!!.fullAddress,
            paymentMethod = if (selectedPaymentMethod == PaymentMethod.CASH_ON_DELIVERY) "Thanh toán khi nhận hàng" else "Chuyển khoản ngân hàng"
        )
        
        // Save order to history
        com.demo.pbl6_android.data.OrderHistoryManager.addOrder(order)

        val bundle = Bundle().apply {
            putString("paymentMethod", paymentMethodString)
            putString("orderId", orderId)
        }

        findNavController().navigate(R.id.action_orderFragment_to_orderStatusFragment, bundle)
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

