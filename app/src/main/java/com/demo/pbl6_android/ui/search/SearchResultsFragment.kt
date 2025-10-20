package com.demo.pbl6_android.ui.search

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
import com.demo.pbl6_android.databinding.FragmentSearchResultsBinding
import com.demo.pbl6_android.ui.landing.adapter.ProductAdapter

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
        // For now, we'll show all products as sample data
        // In the future, this will filter based on searchQuery
        val products = ProductRepository.getAllProducts()

        if (products.isEmpty()) {
            showEmptyState()
        } else {
            showResults(products)
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

