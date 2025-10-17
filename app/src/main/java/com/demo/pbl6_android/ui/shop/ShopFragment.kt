package com.demo.pbl6_android.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.pbl6_android.R
import com.demo.pbl6_android.databinding.FragmentShopBinding
import com.demo.pbl6_android.ui.shop.adapter.ShopProductAdapter
import com.demo.pbl6_android.ui.shop.adapter.ShopVoucherAdapter
import com.demo.pbl6_android.utils.formatFollowers
import com.demo.pbl6_android.utils.navigateToChatWithShop
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class ShopFragment : Fragment() {

    private var _binding: FragmentShopBinding? = null
    private val binding: FragmentShopBinding
        get() = _binding!!
    
    private val viewModel: ShopViewModel by viewModels()
    
    private lateinit var voucherAdapter: ShopVoucherAdapter
    private lateinit var productAdapter: ShopProductAdapter
    
    private var shopName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        shopName = arguments?.getString("shopName")
        
        setupToolbar()
        setupTabs()
        setupAdapters()
        setupListeners()
        observeUiState()
        
        // Load shop data using shop name
        viewModel.handleEvent(ShopEvent.LoadShop(shopName))
    }
    
    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                renderUiState(state)
            }
        }
    }
    
    private fun renderUiState(state: ShopUiState) {
        // Show/hide loading
        binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
        
        // Update shop info
        state.shopInfo?.let { shopInfo ->
            binding.shopName.text = shopInfo.name
            binding.shopRating.text = shopInfo.rating.toString()
            binding.shopFollowers.text = shopInfo.followers.formatFollowers()
            updateFollowButton(shopInfo.isFollowing)
        }
        
        // Update vouchers
        voucherAdapter.submitList(state.vouchers)
        
        // Update flash sale products
        productAdapter.submitList(state.flashSaleProducts)
        
        // Update suggested products
        if (state.suggestedProducts.isNotEmpty()) {
            // TODO: Setup suggested products adapter if needed
        }
        
        // Show error message
        state.errorMessage?.let { message ->
            showError(message)
        }
    }
    
    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
    
    private fun setupToolbar() {
        binding.toolbar.setOnClickListener {
            findNavController().navigateUp()
        }
    }
    
    private fun setupAdapters() {
        setupVouchers()
        setupProducts()
    }
    
    private fun setupTabs() {
        binding.shopTabs.addTab(binding.shopTabs.newTab().setText("Shop"))
        binding.shopTabs.addTab(binding.shopTabs.newTab().setText("Sản phẩm"))
        binding.shopTabs.addTab(binding.shopTabs.newTab().setText("Danh mục hàng"))
        
        binding.shopTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Handle tab selection
                when (tab?.position) {
                    0 -> showShopContent()
                    1 -> showProductsContent()
                    2 -> showCategoriesContent()
                }
            }
            
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
    
    private fun setupVouchers() {
        voucherAdapter = ShopVoucherAdapter { voucher ->
            viewModel.handleEvent(ShopEvent.SaveVoucher(voucher))
        }
        
        binding.vouchersRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = voucherAdapter
        }
    }
    
    private fun setupProducts() {
        productAdapter = ShopProductAdapter(
            onProductClick = { product ->
                navigateToProductDetail(product.id)
            },
            onRemindClick = { product ->
                viewModel.handleEvent(ShopEvent.RemindProduct(product.id))
            }
        )
        
        binding.productsRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = productAdapter
        }
    }
    
    private fun setupListeners() {
        binding.btnFollow.setOnClickListener {
            val currentShopInfo = viewModel.uiState.value.shopInfo
            currentShopInfo?.let { info ->
                viewModel.handleEvent(ShopEvent.ToggleFollow(info.id))
            }
        }
        
        binding.btnChat.setOnClickListener {
            val currentShopInfo = viewModel.uiState.value.shopInfo
            currentShopInfo?.let { info ->
                navigateToChatWithShop(info.name, info.id)
            }
        }
        
        binding.searchBox.setOnClickListener {
            // TODO: Open search in shop
        }
        
        binding.viewMoreFlashSale.setOnClickListener {
            // TODO: Show all flash sale products
        }
        
        binding.viewMoreSuggested.setOnClickListener {
            // TODO: Show more suggested products
        }
    }
    
    private fun showShopContent() {
        binding.shopContentContainer.visibility = View.VISIBLE
        // TODO: Show flash sale and suggested products
    }
    
    private fun showProductsContent() {
        binding.shopContentContainer.visibility = View.GONE
        // TODO: Show all products list
    }
    
    private fun showCategoriesContent() {
        binding.shopContentContainer.visibility = View.GONE
        // TODO: Show categories
    }
    
    private fun updateFollowButton(isFollowing: Boolean) {
        binding.btnFollow.apply {
            text = if (isFollowing) "Đã theo dõi" else "+ Theo dõi"
            strokeWidth = if (isFollowing) 2 else 0
        }
    }
    
    private fun navigateToProductDetail(productId: String) {
        val bundle = Bundle().apply {
            putString("productId", productId)
        }
        findNavController().navigate(R.id.action_shopFragment_to_productDetailFragment, bundle)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

