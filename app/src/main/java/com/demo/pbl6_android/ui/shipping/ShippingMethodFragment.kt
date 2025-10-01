package com.demo.pbl6_android.ui.shipping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.pbl6_android.databinding.FragmentShippingMethodBinding
import com.demo.pbl6_android.ui.order.model.ShippingMethod
import com.demo.pbl6_android.ui.shipping.adapter.ShippingMethodAdapter

class ShippingMethodFragment : Fragment() {

    private var _binding: FragmentShippingMethodBinding? = null
    private val binding: FragmentShippingMethodBinding
        get() = _binding!!

    private lateinit var shippingMethodAdapter: ShippingMethodAdapter
    private val shippingMethods = mutableListOf<ShippingMethod>()
    private var selectedMethod: ShippingMethod? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShippingMethodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupRecyclerView()
        loadShippingMethods()
    }

    private fun setupViews() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnConfirm.setOnClickListener {
            selectedMethod?.let {
                ShippingMethodData.selectedMethod = it
                findNavController().navigateUp()
            }
        }
    }

    private fun setupRecyclerView() {
        shippingMethodAdapter = ShippingMethodAdapter(
            methods = shippingMethods,
            onMethodSelected = { method ->
                selectedMethod = method
            }
        )

        binding.rvShippingMethods.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = shippingMethodAdapter
        }
    }

    private fun loadShippingMethods() {
        shippingMethods.clear()
        
        shippingMethods.add(
            ShippingMethod(
                id = "express",
                name = "Hỏa tốc",
                estimatedDays = "1-2 ngày",
                price = 50000,
                isSelected = false
            )
        )
        
        shippingMethods.add(
            ShippingMethod(
                id = "fast",
                name = "Nhanh",
                estimatedDays = "2-3 ngày",
                price = 40000,
                isSelected = false
            )
        )
        
        shippingMethods.add(
            ShippingMethod(
                id = "standard",
                name = "Tiêu chuẩn",
                estimatedDays = "3-5 ngày",
                price = 30000,
                isSelected = false
            )
        )
        
        shippingMethods.add(
            ShippingMethod(
                id = "economy",
                name = "Tiết kiệm",
                estimatedDays = "5-7 ngày",
                price = 20000,
                isSelected = false
            )
        )

        // Get current selected method from OrderFragment
        val currentMethod = ShippingMethodData.selectedMethod
        if (currentMethod != null) {
            shippingMethods.find { it.id == currentMethod.id }?.isSelected = true
            selectedMethod = shippingMethods.find { it.id == currentMethod.id }
        } else {
            // Default to standard
            shippingMethods.find { it.id == "standard" }?.isSelected = true
            selectedMethod = shippingMethods.find { it.id == "standard" }
        }
        
        shippingMethodAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

