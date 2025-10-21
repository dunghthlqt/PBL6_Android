package com.demo.pbl6_android.ui.voucher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.pbl6_android.data.VoucherRepository
import com.demo.pbl6_android.databinding.FragmentPlatformVoucherBinding
import com.demo.pbl6_android.ui.common.CustomToast
import com.demo.pbl6_android.ui.voucher.adapter.VoucherAdapter

class PlatformVoucherFragment : Fragment() {

    private var _binding: FragmentPlatformVoucherBinding? = null
    private val binding: FragmentPlatformVoucherBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlatformVoucherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        loadVouchers()
    }

    private fun setupViews() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun loadVouchers() {
        val vouchers = VoucherRepository.getPlatformVouchers()
        
        if (vouchers.isEmpty()) {
            showEmptyState()
        } else {
            showVouchers(vouchers)
        }
    }

    private fun showVouchers(vouchers: List<com.demo.pbl6_android.data.model.Voucher>) {
        // Separate shipping and discount vouchers
        val shippingVouchers = vouchers.filter { it.code.contains("SHIP", ignoreCase = true) || it.title.contains("ship", ignoreCase = true) || it.title.contains("vận chuyển", ignoreCase = true) }
        val discountVouchers = vouchers.filter { !shippingVouchers.contains(it) }
        
        val sortedVouchers = shippingVouchers + discountVouchers
        
        // Get currently selected vouchers
        val selectedShippingVoucher = com.demo.pbl6_android.data.VoucherManager.selectedPlatformShippingVoucher.value
        val selectedDiscountVoucher = com.demo.pbl6_android.data.VoucherManager.selectedPlatformDiscountVoucher.value
        
        val adapter = VoucherAdapter(sortedVouchers, selectedShippingVoucher ?: selectedDiscountVoucher) { voucher ->
            com.demo.pbl6_android.data.VoucherManager.selectPlatformVoucher(voucher)
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

