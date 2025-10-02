package com.demo.pbl6_android.ui.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.demo.pbl6_android.R
import com.demo.pbl6_android.data.ProductRepository
import com.demo.pbl6_android.databinding.FragmentLandingPageBinding
import com.demo.pbl6_android.ui.landing.adapter.ProductAdapter

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
        binding.searchEditText.setOnClickListener {
            // TODO: Navigate to search screen
        }
        
        binding.searchButton.setOnClickListener {
            // TODO: Navigate to search results
        }
    }
    
    private fun loadProducts() {
        val products = ProductRepository.getAllProducts()
        
        productAdapter = ProductAdapter(products) { product ->
            navigateToProductDetail(product.id)
        }
        
        binding.rvProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productAdapter
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
