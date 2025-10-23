package com.demo.pbl6_android.ui.landing

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
import com.demo.pbl6_android.data.ProductRepository
import com.demo.pbl6_android.databinding.FragmentLandingPageBinding
import com.demo.pbl6_android.ui.landing.adapter.ProductAdapter
import kotlinx.coroutines.launch

class LandingPageFragment : Fragment() {

    private var _binding: FragmentLandingPageBinding? = null
    private val binding: FragmentLandingPageBinding
        get() = _binding!!
    
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLandingPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        loadProducts()
    }

    private fun setupViews() {
        binding.searchContainer.setOnClickListener {
            findNavController().navigate(R.id.action_landingPageFragment_to_searchInputFragment)
        }
        
        // Setup RecyclerView first
        binding.rvProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }
    
    private fun loadProducts() {
        showLoading(true)
        
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val result = ProductRepository.getAllProducts()
                
                // Check if view is still alive before updating UI
                if (_binding == null) return@launch
                
                showLoading(false)
                
                when (result) {
                    is com.demo.pbl6_android.data.api.ApiResult.Success -> {
                        val products = result.data
                        if (products.isEmpty()) {
                            showEmptyState(true)
                        } else {
                            showEmptyState(false)
                            productAdapter = ProductAdapter(products) { product ->
                                navigateToProductDetail(product.id)
                            }
                            binding.rvProducts.adapter = productAdapter
                        }
                    }
                    is com.demo.pbl6_android.data.api.ApiResult.Error -> {
                        showError(result.message)
                    }
                    is com.demo.pbl6_android.data.api.ApiResult.Loading -> {
                        // Already showing loading
                    }
                }
            } catch (e: Exception) {
                if (_binding == null) return@launch
                showLoading(false)
                showError("Có lỗi xảy ra: ${e.message}")
            }
        }
    }
    
    private fun showLoading(isLoading: Boolean) {
        _binding?.rvProducts?.visibility = if (isLoading) View.GONE else View.VISIBLE
        // You can add a ProgressBar to the layout and show/hide it here
    }
    
    private fun showEmptyState(isEmpty: Boolean) {
        // You can add an empty state view to the layout and show/hide it here
        if (isEmpty) {
            showError("Không có sản phẩm nào")
        }
    }
    
    private fun showError(message: String) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun navigateToProductDetail(productId: String) {
        val bundle = Bundle().apply {
            putString("productId", productId)
        }
        findNavController().navigate(R.id.action_landingPageFragment_to_productDetailFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
