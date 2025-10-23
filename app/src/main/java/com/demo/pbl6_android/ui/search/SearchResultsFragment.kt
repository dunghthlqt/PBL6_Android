package com.demo.pbl6_android.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.demo.pbl6_android.R
import com.demo.pbl6_android.data.ProductRepository
import com.demo.pbl6_android.data.model.Product
import com.demo.pbl6_android.databinding.FragmentSearchResultsBinding
import com.demo.pbl6_android.ui.landing.adapter.ProductAdapter
import kotlinx.coroutines.launch

class SearchResultsFragment : Fragment() {

    private var _binding: FragmentSearchResultsBinding? = null
    private val binding: FragmentSearchResultsBinding
        get() = _binding!!

    private lateinit var productAdapter: ProductAdapter
    private var searchQuery: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        searchQuery = arguments?.getString("searchQuery") ?: ""
        
        setupViews()
        setupRecyclerView()
        performSearch()
    }

    private fun setupViews() {
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            tvSearchQuery.text = searchQuery

            searchQueryContainer.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun performSearch() {
        viewLifecycleOwner.lifecycleScope.launch {
            // Use search API if query is not empty, otherwise show all products
            val result = if (searchQuery.isNotEmpty()) {
                ProductRepository.searchProducts(searchQuery)
            } else {
                ProductRepository.getAllProducts()
            }
            
            if (_binding == null) return@launch

            when (result) {
                is com.demo.pbl6_android.data.api.ApiResult.Success -> {
                    if (result.data.isEmpty()) {
                        showEmptyState()
                    } else {
                        showResults(result.data)
                    }
                }
                is com.demo.pbl6_android.data.api.ApiResult.Error -> {
                    showEmptyState()
                    android.widget.Toast.makeText(
                        requireContext(),
                        "Có lỗi xảy ra: ${result.message}",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
                is com.demo.pbl6_android.data.api.ApiResult.Loading -> {
                    // Show loading if needed
                }
            }
        }
    }

    private fun showResults(products: List<Product>) {
        binding.apply {
            emptyState.visibility = View.GONE
            rvProducts.visibility = View.VISIBLE
            tvResultsInfo.visibility = View.VISIBLE
            
            tvResultsInfo.text = "Tìm thấy ${products.size} sản phẩm"

            productAdapter = ProductAdapter(products) { product ->
                navigateToProductDetail(product.id)
            }
            rvProducts.adapter = productAdapter
        }
    }

    private fun showEmptyState() {
        binding.apply {
            emptyState.visibility = View.VISIBLE
            rvProducts.visibility = View.GONE
            tvResultsInfo.visibility = View.GONE
        }
    }

    private fun navigateToProductDetail(productId: String) {
        val bundle = Bundle().apply {
            putString("productId", productId)
        }
        findNavController().navigate(
            R.id.action_searchResultsFragment_to_productDetailFragment,
            bundle
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

