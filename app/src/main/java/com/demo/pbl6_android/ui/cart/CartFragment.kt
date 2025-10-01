package com.demo.pbl6_android.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.pbl6_android.R
import com.demo.pbl6_android.databinding.FragmentCartBinding
import com.demo.pbl6_android.ui.cart.adapter.CartShopAdapter
import com.demo.pbl6_android.ui.cart.model.CartShop
import com.demo.pbl6_android.ui.cart.model.CartProduct

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding: FragmentCartBinding
        get() = _binding!!
    
    private lateinit var cartShopAdapter: CartShopAdapter
    private val cartShops = mutableListOf<CartShop>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupViews()
        setupRecyclerView()
        loadSampleData()
    }

    private fun setupViews() {
        // Select all checkbox
        binding.checkboxSelectAll.setOnCheckedChangeListener { _, isChecked ->
            selectAllItems(isChecked)
        }
        
        // Search button
        binding.btnSearch.setOnClickListener {
            // TODO: Navigate to search
        }
        
        // Delete all button
        binding.btnDeleteAll.setOnClickListener {
            deleteSelectedItems()
        }
        
        // Voucher section
        binding.tvSelectVoucher.setOnClickListener {
            // TODO: Navigate to voucher selection
        }
        
        // Checkout button
        binding.btnCheckout.setOnClickListener {
            checkout()
        }
    }

    private fun setupRecyclerView() {
        cartShopAdapter = CartShopAdapter(
            onShopCheckChanged = { shop, isChecked ->
                updateSelectAllCheckbox()
                updateTotalPrice()
            },
            onProductCheckChanged = { product, isChecked ->
                updateSelectAllCheckbox()
                updateTotalPrice()
            },
            onQuantityChanged = { product, newQuantity ->
                updateTotalPrice()
            },
            onDeleteProduct = { product ->
                deleteProduct(product)
            },
            onViewShop = { shop ->
                // TODO: Navigate to shop
            }
        )
        
        binding.rvCartItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartShopAdapter
        }
    }

    private fun loadSampleData() {
        cartShops.clear()
        
        // Shop 1
        val shop1Products = mutableListOf(
            CartProduct(
                id = "1",
                name = "Áo thun nam basic",
                color = "Đen",
                size = "L",
                currentPrice = 299000,
                originalPrice = 399000,
                quantity = 2,
                imageUrl = "",
                isSelected = false
            ),
            CartProduct(
                id = "2",
                name = "Quần jean nữ skinny",
                color = "Xanh đậm",
                size = "M",
                currentPrice = 599000,
                originalPrice = 799000,
                quantity = 1,
                imageUrl = "",
                isSelected = false
            )
        )
        
        cartShops.add(
            CartShop(
                id = "shop1",
                name = "Thời trang ABC Store",
                products = shop1Products,
                isSelected = false
            )
        )
        
        // Shop 2
        val shop2Products = mutableListOf(
            CartProduct(
                id = "3",
                name = "Giày sneaker unisex",
                color = "Trắng",
                size = "42",
                currentPrice = 899000,
                originalPrice = 1199000,
                quantity = 1,
                imageUrl = "",
                isSelected = false
            )
        )
        
        cartShops.add(
            CartShop(
                id = "shop2",
                name = "Giày dép XYZ Shop",
                products = shop2Products,
                isSelected = false
            )
        )
        
        cartShopAdapter.submitList(cartShops)
        updateTotalPrice()
    }

    private fun selectAllItems(isSelected: Boolean) {
        cartShops.forEach { shop ->
            shop.isSelected = isSelected
            shop.products.forEach { product ->
                product.isSelected = isSelected
            }
        }
        cartShopAdapter.notifyDataSetChanged()
        updateTotalPrice()
    }

    private fun deleteSelectedItems() {
        // TODO: Show confirmation dialog
        cartShops.forEach { shop ->
            shop.products.removeAll { it.isSelected }
        }
        cartShops.removeAll { it.products.isEmpty() }
        cartShopAdapter.submitList(cartShops.toList())
        updateTotalPrice()
    }

    private fun deleteProduct(product: CartProduct) {
        cartShops.forEach { shop ->
            shop.products.remove(product)
        }
        cartShops.removeAll { it.products.isEmpty() }
        cartShopAdapter.submitList(cartShops.toList())
        updateTotalPrice()
    }

    private fun updateSelectAllCheckbox() {
        val allProducts = cartShops.flatMap { it.products }
        val allSelected = allProducts.isNotEmpty() && allProducts.all { it.isSelected }
        
        // Temporarily remove listener to prevent infinite loop
        binding.checkboxSelectAll.setOnCheckedChangeListener(null)
        binding.checkboxSelectAll.isChecked = allSelected
        binding.checkboxSelectAll.setOnCheckedChangeListener { _, isChecked ->
            selectAllItems(isChecked)
        }
    }

    private fun updateTotalPrice() {
        var totalPrice = 0
        var selectedCount = 0
        
        cartShops.forEach { shop ->
            shop.products.forEach { product ->
                if (product.isSelected) {
                    totalPrice += product.currentPrice * product.quantity
                    selectedCount++
                }
            }
        }
        
        binding.tvTotalPrice.text = formatPrice(totalPrice)
        binding.tvItemCount.text = "($selectedCount sản phẩm)"
        binding.tvProductCount.text = "($selectedCount sản phẩm)"
    }

    private fun formatPrice(price: Int): String {
        return "%,dđ".format(price).replace(",", ".")
    }

    private fun checkout() {
        // Collect selected products and group by shop
        val selectedShopsWithProducts = mutableListOf<CartShop>()
        
        cartShops.forEach { shop ->
            val selectedProducts = shop.products.filter { it.isSelected }
            if (selectedProducts.isNotEmpty()) {
                selectedShopsWithProducts.add(
                    CartShop(
                        id = shop.id,
                        name = shop.name,
                        products = selectedProducts.toMutableList(),
                        isSelected = false
                    )
                )
            }
        }
        
        if (selectedShopsWithProducts.isEmpty()) {
            // TODO: Show message: please select at least one product
            return
        }
        
        // Pass data to OrderFragment via OrderData
        com.demo.pbl6_android.ui.order.OrderData.setOrderData(selectedShopsWithProducts)
        
        // Navigate to order/checkout screen
        findNavController().navigate(R.id.action_cartFragment_to_orderFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
