package com.demo.pbl6_android.ui.voucher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.pbl6_android.data.VoucherRepository
import com.demo.pbl6_android.data.api.ApiResult
import com.demo.pbl6_android.data.model.Voucher
import com.demo.pbl6_android.data.repository.PromotionApiRepository
import com.demo.pbl6_android.databinding.FragmentShopVoucherBinding
import com.demo.pbl6_android.ui.common.CustomToast
import com.demo.pbl6_android.ui.voucher.adapter.VoucherAdapter
import kotlinx.coroutines.launch

class ShopVoucherFragment : Fragment() {

    private var _binding: FragmentShopVoucherBinding? = null
    private val binding: FragmentShopVoucherBinding
        get() = _binding!!

    private var storeId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopVoucherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Get storeId from arguments (if passed)
        storeId = arguments?.getString("storeId")
        
        setupViews()
        loadVouchers()
    }

    private fun setupViews() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        
        binding.btnClearSelection.setOnClickListener {
            clearSelection()
        }
        
        // Check if there's a selected voucher and show/hide clear button
        updateClearButtonVisibility()
    }
    
    private fun updateClearButtonVisibility() {
        val selectedVoucher = com.demo.pbl6_android.data.VoucherManager.selectedShopVoucher.value
        binding.btnClearSelection.visibility = if (selectedVoucher != null) {
            android.view.View.VISIBLE
        } else {
            android.view.View.GONE
        }
    }
    
    private fun clearSelection() {
        com.demo.pbl6_android.data.VoucherManager.selectShopVoucher(null)
        CustomToast.show(requireContext(), "Đã bỏ chọn voucher")
        findNavController().navigateUp()
    }

    private fun loadVouchers() {
        val currentStoreId = storeId
        
        if (currentStoreId != null) {
            // Load vouchers from API
            loadVouchersFromApi(currentStoreId)
        } else {
            // Fallback to mock data if no storeId provided
            val vouchers = VoucherRepository.getShopVouchers()
            
            if (vouchers.isEmpty()) {
                showEmptyState()
            } else {
                showVouchers(vouchers)
            }
        }
    }

    private fun loadVouchersFromApi(storeId: String) {
        showLoading(true)
        
        viewLifecycleOwner.lifecycleScope.launch {
            when (val result = PromotionApiRepository.getActivePromotionsByStore(storeId)) {
                is ApiResult.Success -> {
                    showLoading(false)
                    
                    val promotions = result.data
                    if (promotions.isEmpty()) {
                        showEmptyState()
                    } else {
                        // Convert PromotionResponse to Voucher
                        val vouchers = promotions.map { promotion ->
                            Voucher(
                                id = promotion.id,
                                code = promotion.title, // Use title as code for display
                                discount = promotion.discountValue?.toInt() ?: 0,
                                description = "Giảm giá ${promotion.discountValue}%",
                                minOrderValue = promotion.minOrderValue?.toInt() ?: 0,
                                maxDiscount = promotion.maxDiscountValue?.toInt(),
                                validUntil = promotion.endDate,
                                isShopVoucher = true,
                                storeId = promotion.storeId
                            )
                        }
                        showVouchers(vouchers)
                    }
                }
                
                is ApiResult.Error -> {
                    showLoading(false)
                    Toast.makeText(
                        requireContext(),
                        "Lỗi tải voucher: ${result.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    
                    // Fallback to mock data on error
                    val vouchers = VoucherRepository.getShopVouchers()
                    if (vouchers.isEmpty()) {
                        showEmptyState()
                    } else {
                        showVouchers(vouchers)
                    }
                }
                
                is ApiResult.Loading -> {
                    showLoading(true)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                rvVouchers.visibility = View.GONE
                emptyState.visibility = View.GONE
                // TODO: Add loading indicator if needed
            }
        }
    }

    private fun showVouchers(vouchers: List<com.demo.pbl6_android.data.model.Voucher>) {
        val selectedVoucher = com.demo.pbl6_android.data.VoucherManager.selectedShopVoucher.value
        
        val adapter = VoucherAdapter(vouchers, selectedVoucher) { voucher ->
            com.demo.pbl6_android.data.VoucherManager.selectShopVoucher(voucher)
            CustomToast.show(requireContext(), "Đã áp dụng mã ${voucher.code}")
            findNavController().navigateUp()
        }

        binding.apply {
            rvVouchers.layoutManager = LinearLayoutManager(requireContext())
            rvVouchers.adapter = adapter
            rvVouchers.visibility = View.VISIBLE
            emptyState.visibility = View.GONE
        }
    }

    private fun showEmptyState() {
        binding.apply {
            emptyState.visibility = View.VISIBLE
            rvVouchers.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

