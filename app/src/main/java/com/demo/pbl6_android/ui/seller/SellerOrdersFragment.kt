package com.demo.pbl6_android.ui.seller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.demo.pbl6_android.databinding.FragmentSellerOrdersBinding

class SellerOrdersFragment : Fragment() {

    private var _binding: FragmentSellerOrdersBinding? = null
    private val binding: FragmentSellerOrdersBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellerOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOrders()
    }

    private fun setupOrders() {
        // TODO: Implement seller orders management UI
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

