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
import com.demo.pbl6_android.data.VoucherManager
import com.demo.pbl6_android.data.VoucherRepository
import com.demo.pbl6_android.data.api.ApiResult
import com.demo.pbl6_android.data.model.Voucher
import com.demo.pbl6_android.data.repository.PromotionApiRepository
import com.demo.pbl6_android.databinding.FragmentPlatformVoucherSelectionBinding
import com.demo.pbl6_android.ui.common.CustomToast
import com.demo.pbl6_android.ui.voucher.adapter.VoucherSelectableAdapter
import kotlinx.coroutines.launch

class PlatformVoucherSelectionFragment : Fragment() {

    private var _binding: FragmentPlatformVoucherSelectionBinding? = null
    private val binding: FragmentPlatformVoucherSelectionBinding
        get() = _binding!!

    private lateinit var shippingVoucherAdapter: VoucherSelectableAdapter
    private lateinit var discountVoucherAdapter: VoucherSelectableAdapter
    
    private var selectedShippingVoucher: Voucher? = null
    private var selectedDiscountVoucher: Voucher? = null
    
    private var shippingVouchers = listOf<Voucher>()
    private var discountVouchers = listOf<Voucher>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlatformVoucherSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadCurrentSelections()
        setupViews()
        loadVouchers()
    }
    
    private fun loadCurrentSelections() {
        // Get current selected vouchers from VoucherManager
        selectedShippingVoucher = VoucherManager.selectedPlatformShippingVoucher.value
        selectedDiscountVoucher = VoucherManager.selectedPlatformDiscountVoucher.value
    }

    private fun setupViews() {
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
            
            btnApplyCode.setOnClickListener {
                applyVoucherCode()
            }
            
            btnConfirm.setOnClickListener {
                confirmSelection()
            }
        }
    }

    private fun loadVouchers() {
        // Try loading from API first
        loadVouchersFromApi()
    }

    private fun loadVouchersFromApi() {
        viewLifecycleOwner.lifecycleScope.launch {
            when (val result = PromotionApiRepository.getActivePromotions()) {
                is ApiResult.Success -> {
                    val promotions = result.data
                    
                    if (promotions.isNotEmpty()) {
                        // Convert PromotionResponse to Voucher
                        val allVouchers = promotions.map { promotion ->
                            Voucher(
                                id = promotion.id,
                                code = promotion.title,
                                discount = promotion.discountValue?.toInt() ?: 0,
                                description = "Giảm giá ${promotion.discountValue}%",
                                minOrderValue = promotion.minOrderValue?.toInt() ?: 0,
                                maxDiscount = promotion.maxDiscountValue?.toInt(),
                                validUntil = promotion.endDate,
                                isShopVoucher = false,
                                storeId = null
                            )
                        }
                        
                        processVouchers(allVouchers)
                    } else {
                        // Fallback to mock data if API returns empty
                        loadMockVouchers()
                    }
                }
                
                is ApiResult.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Lỗi tải voucher: ${result.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Fallback to mock data on error
                    loadMockVouchers()
                }
                
                is ApiResult.Loading -> {
                    // Show loading if needed
                }
            }
        }
    }

    private fun loadMockVouchers() {
        val allVouchers = VoucherRepository.getPlatformVouchers()
        processVouchers(allVouchers)
    }

    private fun processVouchers(allVouchers: List<Voucher>) {
        // Separate shipping and discount vouchers
        shippingVouchers = allVouchers.filter { 
            it.code.contains("SHIP", ignoreCase = true) || 
            it.title.contains("ship", ignoreCase = true) || 
            it.title.contains("vận chuyển", ignoreCase = true) 
        }
        
        discountVouchers = allVouchers.filter { !shippingVouchers.contains(it) }
        
        // Setup shipping vouchers
        if (shippingVouchers.isEmpty()) {
            showEmptyShippingState()
        } else {
            showShippingVouchers()
        }
        
        // Setup discount vouchers
        if (discountVouchers.isEmpty()) {
            showEmptyDiscountState()
        } else {
            showDiscountVouchers()
        }
    }

    private fun showShippingVouchers() {
        shippingVoucherAdapter = VoucherSelectableAdapter(
            vouchers = shippingVouchers,
            selectedVoucherId = selectedShippingVoucher?.id
        ) { voucher ->
            handleShippingVoucherSelection(voucher)
        }
        
        binding.apply {
            rvShippingVouchers.layoutManager = LinearLayoutManager(requireContext())
            rvShippingVouchers.adapter = shippingVoucherAdapter
            rvShippingVouchers.visibility = View.VISIBLE
            emptyStateShipping.visibility = View.GONE
        }
    }

    private fun showDiscountVouchers() {
        discountVoucherAdapter = VoucherSelectableAdapter(
            vouchers = discountVouchers,
            selectedVoucherId = selectedDiscountVoucher?.id
        ) { voucher ->
            handleDiscountVoucherSelection(voucher)
        }
        
        binding.apply {
            rvDiscountVouchers.layoutManager = LinearLayoutManager(requireContext())
            rvDiscountVouchers.adapter = discountVoucherAdapter
            rvDiscountVouchers.visibility = View.VISIBLE
            emptyStateDiscount.visibility = View.GONE
        }
    }

    private fun showEmptyShippingState() {
        binding.apply {
            emptyStateShipping.visibility = View.VISIBLE
            rvShippingVouchers.visibility = View.GONE
        }
    }

    private fun showEmptyDiscountState() {
        binding.apply {
            emptyStateDiscount.visibility = View.VISIBLE
            rvDiscountVouchers.visibility = View.GONE
        }
    }

    private fun handleShippingVoucherSelection(voucher: Voucher?) {
        selectedShippingVoucher = voucher
        shippingVoucherAdapter.updateSelectedVoucher(voucher?.id)
    }

    private fun handleDiscountVoucherSelection(voucher: Voucher?) {
        selectedDiscountVoucher = voucher
        discountVoucherAdapter.updateSelectedVoucher(voucher?.id)
    }

    private fun applyVoucherCode() {
        val code = binding.etVoucherCode.text.toString().trim()
        
        if (code.isEmpty()) {
            CustomToast.show(requireContext(), "Vui lòng nhập mã voucher")
            return
        }
        
        // Try to find voucher by code
        val allVouchers = VoucherRepository.getPlatformVouchers()
        val voucher = allVouchers.firstOrNull { 
            it.code.equals(code, ignoreCase = true) 
        }
        
        if (voucher == null) {
            CustomToast.show(requireContext(), "Mã voucher không hợp lệ")
            return
        }
        
        // Check if it's shipping or discount voucher
        val isShippingVoucher = voucher.code.contains("SHIP", ignoreCase = true) || 
                                voucher.title.contains("vận chuyển", ignoreCase = true)
        
        if (isShippingVoucher) {
            handleShippingVoucherSelection(voucher)
        } else {
            handleDiscountVoucherSelection(voucher)
        }
        
        binding.etVoucherCode.setText("")
    }
    
    private fun confirmSelection() {
        // Save selections to VoucherManager
        VoucherManager.selectPlatformVoucher(selectedShippingVoucher)
        VoucherManager.selectPlatformVoucher(selectedDiscountVoucher)
        
        val message = buildString {
            when {
                selectedShippingVoucher != null && selectedDiscountVoucher != null -> 
                    append("Đã áp dụng 2 voucher")
                selectedShippingVoucher != null -> 
                    append("Đã áp dụng voucher vận chuyển")
                selectedDiscountVoucher != null -> 
                    append("Đã áp dụng voucher giảm giá")
                else -> 
                    append("Đã bỏ chọn voucher")
            }
        }
        
        CustomToast.show(requireContext(), message)
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

