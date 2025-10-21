package com.demo.pbl6_android.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.pbl6_android.R
import com.demo.pbl6_android.data.CartManager
import com.demo.pbl6_android.data.ProductRepository
import com.demo.pbl6_android.data.model.Product
import com.demo.pbl6_android.data.model.ProductColor
import com.demo.pbl6_android.databinding.FragmentProductDetailBinding
import com.demo.pbl6_android.ui.product.adapter.ProductColorAdapter
import com.demo.pbl6_android.ui.product.adapter.ProductImageAdapter
import com.demo.pbl6_android.ui.product.adapter.ProductSizeAdapter
import com.demo.pbl6_android.ui.product.adapter.RelatedProductAdapter
import com.demo.pbl6_android.ui.product.adapter.SpecificationAdapter
import com.demo.pbl6_android.utils.navigateToShop
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding: FragmentProductDetailBinding
        get() = _binding!!
    
    private var product: Product? = null
    private var quantity: Int = 1
    private var colorAdapter: ProductColorAdapter? = null
    private var sizeAdapter: ProductSizeAdapter? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        loadProduct()
        observeCart()
    }
    
    private fun setupViews() {
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
            
            btnShare.setOnClickListener {
                copyProductLinkToClipboard()
            }
            
            btnCart.setOnClickListener {
                findNavController().navigate(R.id.action_productDetailFragment_to_cartFragment)
            }
            
            btnMenu.setOnClickListener {
                showMenuDialog()
            }
            
            btnViewShop.setOnClickListener {
                product?.let { navigateToShop(it.shopName) }
            }
            
            btnDecrease.setOnClickListener {
                decreaseQuantity()
            }
            
            btnIncrease.setOnClickListener {
                increaseQuantity()
            }
            
            btnChat.setOnClickListener {
                openChatWithShop()
            }
            
            btnAddToCart.setOnClickListener {
                addToCart()
            }
            
            btnBuyNow.setOnClickListener {
                buyNow()
            }
            
            llReviewsSection.setOnClickListener {
                navigateToReviews()
            }
            
            llProductDetails.setOnClickListener {
                showProductDetailsBottomSheet()
            }
        }
    }
    
    private fun navigateToReviews() {
        product?.let {
            val bundle = Bundle().apply {
                putString("productId", it.id)
            }
            findNavController().navigate(
                R.id.action_productDetailFragment_to_productReviewsFragment,
                bundle
            )
        }
    }
    
    private fun copyProductLinkToClipboard() {
        val clipboard = requireContext().getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("Product Link", "https://example.com/product/${product?.id}")
        clipboard.setPrimaryClip(clip)
        com.demo.pbl6_android.ui.common.CustomToast.show(requireContext(), "Đã sao chép đường dẫn vào bộ nhớ")
    }
    
    private fun showMenuDialog() {
        val popup = androidx.appcompat.widget.PopupMenu(requireContext(), binding.btnMenu)
        popup.menuInflater.inflate(R.menu.menu_product_detail, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_go_home -> {
                    navigateToHome()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }
    
    private fun navigateToHome() {
        findNavController().navigate(R.id.landingPageFragment)
    }
    
    private fun loadProduct() {
        val productId = arguments?.getString("productId") ?: return
        
        product = ProductRepository.getProductById(productId)
        
        product?.let { product ->
            displayProduct(product)
            loadRelatedProducts(product.id)
        }
    }
    
    private fun displayProduct(product: Product) {
        binding.apply {
            tvProductName.text = product.name
            tvRating.text = product.rating.toString()
            tvReviewCount.text = "(${product.reviewCount} đánh giá)"
            tvSoldCount.text = "Đã bán ${formatSoldCount(product.soldCount)}"
            tvCurrentPrice.text = formatPrice(product.currentPrice)
            tvOriginalPrice.text = formatPrice(product.originalPrice)
            tvOriginalPrice.paintFlags = tvOriginalPrice.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
            tvDiscount.text = "-${product.discount}%"
            tvDescription.text = product.description
            tvShopName.text = product.shopName
            
            setupImagePager(product.images)
            setupColorSelection(product.colors)
            setupSizeSelection(product.sizes)
            setupSpecifications(product)
            setupReviewSection(product.id)
        }
    }
    
    private fun setupReviewSection(productId: String) {
        val averageRating = com.demo.pbl6_android.data.ReviewRepository.getAverageRating(productId)
        val totalReviews = com.demo.pbl6_android.data.ReviewRepository.getTotalReviewsCount(productId)
        
        binding.apply {
            tvRatingNumber.text = String.format("%.1f", averageRating)
            tvReviewSummary.text = "${"%.1f".format(averageRating)} ($totalReviews)"
            
            val fullStars = averageRating.toInt()
            val stars = "★".repeat(fullStars) + "☆".repeat(5 - fullStars)
            tvRatingStars.text = stars
        }
    }
    
    private fun setupImagePager(images: List<String>) {
        val imageAdapter = ProductImageAdapter(images)
        binding.vpProductImages.adapter = imageAdapter
        
        TabLayoutMediator(binding.tabIndicator, binding.vpProductImages) { _, _ ->
        }.attach()
    }
    
    private fun setupColorSelection(colors: List<ProductColor>) {
        if (colors.isEmpty()) return
        
        colorAdapter = ProductColorAdapter(colors) { selectedColor ->
            // Color changed
        }
        
        binding.rvColors.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = colorAdapter
        }
    }
    
    private fun setupSizeSelection(sizes: List<String>) {
        if (sizes.isEmpty()) {
            binding.tvSizeLabel.visibility = View.GONE
            binding.rvSizes.visibility = View.GONE
            return
        }
        
        binding.tvSizeLabel.visibility = View.VISIBLE
        binding.rvSizes.visibility = View.VISIBLE
        
        sizeAdapter = ProductSizeAdapter(sizes) { selectedSize ->
            // Size changed
        }
        
        binding.rvSizes.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = sizeAdapter
        }
    }
    
    private fun setupSpecifications(product: Product) {
        // Specifications will be shown in bottom sheet
        // Nothing to do here
    }
    
    private fun showProductDetailsBottomSheet() {
        val currentProduct = product ?: return
        
        val bottomSheetDialog = com.google.android.material.bottomsheet.BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_product_details, null)
        bottomSheetDialog.setContentView(bottomSheetView)
        
        val rvSpecifications = bottomSheetView.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_specifications)
        val btnAgree = bottomSheetView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_agree)
        
        val specs = mutableListOf<Pair<String, String>>()
        specs.add(Pair("Thương hiệu:", currentProduct.brand))
        specs.add(Pair("Chất liệu:", currentProduct.material))
        specs.add(Pair("Xuất xứ:", currentProduct.origin))
        
        currentProduct.specifications.forEach { (key, value) ->
            specs.add(Pair("$key:", value))
        }
        
        val specAdapter = SpecificationAdapter(specs)
        rvSpecifications.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = specAdapter
        }
        
        btnAgree.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        
        bottomSheetDialog.show()
    }
    
    private fun loadRelatedProducts(currentProductId: String) {
        val relatedProducts = ProductRepository.getRelatedProducts(currentProductId, 4)
        
        val relatedAdapter = RelatedProductAdapter(relatedProducts) { product ->
            navigateToProduct(product.id)
        }
        
        binding.rvRelatedProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = relatedAdapter
        }
    }
    
    private fun decreaseQuantity() {
        if (quantity > 1) {
            quantity--
            binding.tvQuantity.text = quantity.toString()
        }
    }
    
    private fun increaseQuantity() {
        quantity++
        binding.tvQuantity.text = quantity.toString()
    }
    
    private fun addToCart() {
        val currentProduct = product ?: return
        
        val selectedColor = colorAdapter?.getSelectedColor()?.name ?: "Default"
        val selectedSize = sizeAdapter?.getSelectedSize() ?: "Default"
        
        CartManager.addToCart(currentProduct, selectedColor, selectedSize, quantity)
        com.demo.pbl6_android.ui.common.CustomToast.show(requireContext(), "Đã thêm vào giỏ hàng")
    }
    
    private fun buyNow() {
        val currentProduct = product ?: return
        
        val selectedColor = colorAdapter?.getSelectedColor()?.name ?: "Default"
        val selectedSize = sizeAdapter?.getSelectedSize() ?: "Default"
        
        val cartProduct = com.demo.pbl6_android.ui.cart.model.CartProduct(
            id = "${currentProduct.id}_${selectedColor}_${selectedSize}",
            name = currentProduct.name,
            color = selectedColor,
            size = selectedSize,
            currentPrice = currentProduct.currentPrice,
            originalPrice = currentProduct.originalPrice,
            quantity = quantity,
            imageUrl = currentProduct.images.firstOrNull() ?: "",
            isSelected = true
        )
        
        val cartShop = com.demo.pbl6_android.ui.cart.model.CartShop(
            id = currentProduct.shopId,
            name = currentProduct.shopName,
            products = mutableListOf(cartProduct),
            isSelected = true
        )
        
        com.demo.pbl6_android.ui.order.OrderData.setOrderData(listOf(cartShop))
        
        findNavController().navigate(R.id.action_productDetailFragment_to_orderFragment)
    }
    
    private fun observeCart() {
        viewLifecycleOwner.lifecycleScope.launch {
            CartManager.cartItemCount.collect { count ->
                binding.tvCartBadge.apply {
                    if (count > 0) {
                        visibility = View.VISIBLE
                        text = count.toString()
                    } else {
                        visibility = View.GONE
                    }
                }
            }
        }
    }
    
    private fun navigateToProduct(productId: String) {
        val bundle = Bundle().apply {
            putString("productId", productId)
        }
        findNavController().navigate(R.id.productDetailFragment, bundle)
    }
    
    private fun formatPrice(price: Int): String {
        return "₫%,d".format(price).replace(",", ".")
    }
    
    private fun formatSoldCount(count: Int): String {
        return when {
            count >= 1000 -> "%.1fk".format(count / 1000.0)
            else -> count.toString()
        }
    }
    
    private fun openChatWithShop() {
        val currentProduct = product ?: return
        
        // Find or create conversation with this shop
        val conversations = com.demo.pbl6_android.data.ChatManager.conversations.value
        val existingConversation = conversations.find { it.shopId == currentProduct.shopId }
        
        val conversationId = existingConversation?.id ?: "conv_${currentProduct.shopId}"
        
        val bundle = Bundle().apply {
            putString("conversationId", conversationId)
        }
        findNavController().navigate(R.id.action_productDetailFragment_to_chatFragment, bundle)
    }
    
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

