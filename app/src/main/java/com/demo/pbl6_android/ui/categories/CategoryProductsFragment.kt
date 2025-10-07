package com.demo.pbl6_android.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.demo.pbl6_android.R
import com.demo.pbl6_android.data.ProductRepository
import com.demo.pbl6_android.data.model.Product
import com.demo.pbl6_android.databinding.FragmentCategoryProductsBinding
import com.demo.pbl6_android.ui.categories.adapter.ProductGridAdapter

class CategoryProductsFragment : Fragment() {

    private var _binding: FragmentCategoryProductsBinding? = null
    private val binding: FragmentCategoryProductsBinding
        get() = _binding!!

    private lateinit var productAdapter: ProductGridAdapter
    private val products = mutableListOf<Product>()

    private var categoryId: String? = null
    private var categoryName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryId = it.getString("categoryId")
            categoryName = it.getString("categoryName")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupViews()
        setupRecyclerView()
        loadProducts()
    }

    private fun setupViews() {
        binding.apply {
            tvTitle.text = categoryName ?: "Sản phẩm"
            
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
            
            btnCart.setOnClickListener {
                findNavController().navigate(R.id.action_categoryProductsFragment_to_cartFragment)
            }
        }
    }

    private fun setupRecyclerView() {
        productAdapter = ProductGridAdapter(products) { product ->
            navigateToProductDetail(product.id)
        }

        binding.rvProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productAdapter
        }
    }

    private fun loadProducts() {
        products.clear()
        
        // For now, load all products (later filter by category)
        val allProducts = ProductRepository.getAllProducts()
        products.addAll(allProducts)
        
        productAdapter.notifyDataSetChanged()
        updateProductCount()
        updateEmptyState()
    }

    private fun updateProductCount() {
        binding.tvProductCount.text = "${products.size} sản phẩm"
    }

    private fun updateEmptyState() {
        binding.apply {
            if (products.isEmpty()) {
                emptyState.visibility = View.VISIBLE
                rvProducts.visibility = View.GONE
            } else {
                emptyState.visibility = View.GONE
                rvProducts.visibility = View.VISIBLE
            }
        }
    }

    private fun navigateToProductDetail(productId: String) {
        val bundle = Bundle().apply {
            putString("productId", productId)
        }
        findNavController().navigate(
            R.id.action_categoryProductsFragment_to_productDetailFragment,
            bundle
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

