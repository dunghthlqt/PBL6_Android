package com.demo.pbl6_android.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.demo.pbl6_android.R
import com.demo.pbl6_android.data.CartManager
import com.demo.pbl6_android.data.CategoryRepository
import com.demo.pbl6_android.data.ProductRepository
import com.demo.pbl6_android.data.model.Category
import com.demo.pbl6_android.data.model.Product
import com.demo.pbl6_android.databinding.FragmentCategoriesBinding
import com.demo.pbl6_android.ui.categories.adapter.CategoryAdapter
import com.demo.pbl6_android.ui.categories.adapter.ProductGridAdapter
import kotlinx.coroutines.launch

class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding: FragmentCategoriesBinding
        get() = _binding!!

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductGridAdapter
    
    private val categories = mutableListOf<Category>()
    private val products = mutableListOf<Product>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupViews()
        setupCategoryRecyclerView()
        setupProductRecyclerView()
        loadCategories()
        loadPopularProducts()
        observeCart()
    }

    private fun setupViews() {
        binding.apply {
            // Search bar
            etSearch.setOnClickListener {
                showToast("Chức năng tìm kiếm đang phát triển")
            }
            
            // Cart button
            btnCart.setOnClickListener {
                findNavController().navigate(R.id.action_categoriesFragment_to_cartFragment)
            }
            
            // Flash sale banner
            cardFlashSale.setOnClickListener {
                showToast("Chức năng Flash Sale đang phát triển")
            }
        }
    }

    private fun setupCategoryRecyclerView() {
        categoryAdapter = CategoryAdapter(categories) { category ->
            handleCategoryClick(category)
        }

        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = categoryAdapter
        }
    }

    private fun setupProductRecyclerView() {
        productAdapter = ProductGridAdapter(products) { product ->
            navigateToProductDetail(product.id)
        }

        binding.rvPopularProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productAdapter
        }
    }

    private fun loadCategories() {
        categories.clear()
        categories.addAll(CategoryRepository.getAllCategories())
        categoryAdapter.notifyDataSetChanged()
    }

    private fun loadPopularProducts() {
        products.clear()
        // Load top 4 popular products
        products.addAll(ProductRepository.getAllProducts().take(4))
        productAdapter.notifyDataSetChanged()
    }

    private fun handleCategoryClick(category: Category) {
        // Update selected state
        categories.forEach { it.isSelected = false }
        category.isSelected = true
        categoryAdapter.notifyDataSetChanged()

        // Navigate to category products if not "All"
        if (category.id != "all") {
            val bundle = Bundle().apply {
                putString("categoryId", category.id)
                putString("categoryName", category.name)
            }
            findNavController().navigate(
                R.id.action_categoriesFragment_to_categoryProductsFragment,
                bundle
            )
        } else {
            // Show all products
            showToast("Hiển thị tất cả sản phẩm")
        }
    }

    private fun navigateToProductDetail(productId: String) {
        val bundle = Bundle().apply {
            putString("productId", productId)
        }
        findNavController().navigate(
            R.id.action_categoriesFragment_to_productDetailFragment,
            bundle
        )
    }

    private fun observeCart() {
        viewLifecycleOwner.lifecycleScope.launch {
            CartManager.cartItemCount.collect { count ->
                binding.apply {
                    if (count > 0) {
                        tvCartBadge.visibility = View.VISIBLE
                        tvCartBadge.text = if (count > 99) "99+" else count.toString()
                    } else {
                        tvCartBadge.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

