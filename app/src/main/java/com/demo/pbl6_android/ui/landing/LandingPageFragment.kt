package com.demo.pbl6_android.ui.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.demo.pbl6_android.databinding.FragmentLandingPageBinding

class LandingPageFragment : Fragment() {

    private var _binding: FragmentLandingPageBinding? = null
    private val binding: FragmentLandingPageBinding
        get() = _binding!!

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
    }

    private fun setupViews() {
        // Setup search functionality
        binding.searchEditText.setOnClickListener {
            // TODO: Navigate to search screen
        }
        
        // Setup search button
        binding.searchButton.setOnClickListener {
            // TODO: Navigate to search results
        }
        
        // Setup product cards click listeners
        binding.productCard1.setOnClickListener {
            // TODO: Navigate to product detail
        }
        
        binding.productCard2.setOnClickListener {
            // TODO: Navigate to product detail
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
