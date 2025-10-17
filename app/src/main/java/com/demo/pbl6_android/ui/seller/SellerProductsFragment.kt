package com.demo.pbl6_android.ui.seller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.demo.pbl6_android.databinding.FragmentSellerProductsBinding

class SellerProductsFragment : Fragment() {

    private var _binding: FragmentSellerProductsBinding? = null
    private val binding: FragmentSellerProductsBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellerProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupProducts()
    }

    private fun setupProducts() {
        // TODO: Implement products management UI
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

