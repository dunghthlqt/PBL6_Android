package com.demo.pbl6_android.ui.voucher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.demo.pbl6_android.databinding.FragmentShopVoucherBinding

class ShopVoucherFragment : Fragment() {

    private var _binding: FragmentShopVoucherBinding? = null
    private val binding: FragmentShopVoucherBinding
        get() = _binding!!

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
        setupViews()
        // TODO: Load vouchers from API
        showEmptyState()
    }

    private fun setupViews() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
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

