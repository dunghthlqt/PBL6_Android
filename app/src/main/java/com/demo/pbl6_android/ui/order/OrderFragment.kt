package com.demo.pbl6_android.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null
    private val binding: FragmentOrderBinding
        get() = _binding!!

    private lateinit var orderShopAdapter: OrderShopAdapter
    private val orderShops = mutableListOf<OrderShop>()

    private var shippingAddress: ShippingAddress? = null
    private var selectedShippingMethod: ShippingMethod? = null
    private var selectedPaymentMethod: PaymentMethod = PaymentMethod.BANK_TRANSFER

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
        calculateTotals()
    }

    override fun onResume() {
        super.onResume()
        // Update shipping method if changed
        ShippingMethodData.selectedMethod?.let { method ->
            selectedShippingMethod = method
            displayShippingMethod(method)
            calculateTotals()
        }
    }

    private fun setupViews() {
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            tvChangeAddress.setOnClickListener {
                // TODO: Navigate to address selection
            }

            tvViewAllShipping.setOnClickListener {
                findNavController().navigate(R.id.action_orderFragment_to_shippingMethodFragment)
            }

            tvSelectPlatformVoucher.setOnClickListener {
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
        if (cartShops.isNotEmpty()) {
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
        } else {
            // Fallback to sample data if no cart data
            val shop1Products = listOf(
                OrderProduct(
                    id = "1",
                    name = "Áo thun nam basic",
                    color = "Đen",
                    size = "L",
                    currentPrice = 299000,
                    originalPrice = 399000,
                    quantity = 2,
                    imageUrl = ""
                )
            )

            orderShops.add(
                OrderShop(
                    shopId = "shop1",
                    shopName = "Thời trang ABC Store",
                    products = shop1Products
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

        val shippingTotal = selectedShippingMethod?.price ?: 0
        val grandTotal = productTotal + shippingTotal

        binding.apply {
            tvProductTotal.text = formatPrice(productTotal)
            tvShippingTotal.text = formatPrice(shippingTotal)
            tvGrandTotal.text = formatPrice(grandTotal)
            tvBottomGrandTotal.text = formatPrice(grandTotal)
        }
    }

    private fun formatPrice(price: Int): String {
        return "%,dđ".format(price).replace(",", ".")
    }

    private fun placeOrder() {
        // TODO: Validate order data
        if (shippingAddress == null) {
            // Show error: Please select shipping address
            return
        }

        if (selectedShippingMethod == null) {
            // Show error: Please select shipping method
            return
        }

        if (orderShops.isEmpty()) {
            // Show error: Cart is empty
            return
        }

        // TODO: Create order object and send to backend
        // TODO: Navigate to order success screen
    }

    override fun onDestroyView() {
        super.onDestroyView()
        OrderData.clearOrderData()
        ShippingMethodData.selectedMethod = null
        _binding = null
    }
}

