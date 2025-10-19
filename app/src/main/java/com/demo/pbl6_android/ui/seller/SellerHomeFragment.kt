package com.demo.pbl6_android.ui.seller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.pbl6_android.R
import com.demo.pbl6_android.databinding.FragmentSellerHomeBinding
import com.demo.pbl6_android.ui.seller.adapter.SellerRecommendationAdapter
import com.demo.pbl6_android.ui.seller.adapter.SellerTaskAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class SellerHomeFragment : Fragment() {

    private var _binding: FragmentSellerHomeBinding? = null
    private val binding: FragmentSellerHomeBinding
        get() = _binding!!
    
    private val viewModel: SellerHomeViewModel by viewModels()
    

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellerHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupListeners()
        observeUiState()
    }

    private fun setupListeners() {
        binding.apply {
            btnBack.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            
            btnSettings.setOnClickListener {
                viewModel.handleEvent(SellerHomeEvent.OpenSettings)
                showToast("Mở cài đặt")
            }
            
            btnNotifications.setOnClickListener {
                viewModel.handleEvent(SellerHomeEvent.OpenNotifications)
                showToast("Mở thông báo")
            }
            
            btnViewShop.setOnClickListener {
                viewModel.handleEvent(SellerHomeEvent.ViewShop)
                showToast("Xem shop")
            }
            
            tvViewOrderHistory.setOnClickListener {
                findNavController().navigate(R.id.action_sellerHomeFragment_to_sellerOrderHistoryFragment)
            }
            
            orderPending.setOnClickListener {
                showToast("Chờ lấy hàng")
            }
            
            orderCancelled.setOnClickListener {
                showToast("Đơn hủy")
            }
            
            orderReturn.setOnClickListener {
                showToast("Trả hàng/Hoàn tiền")
            }
            
            orderReview.setOnClickListener {
                showToast("Phản hồi đánh giá")
            }
            
            btnProducts.setOnClickListener {
                findNavController().navigate(R.id.action_sellerHomeFragment_to_sellerProductsFragment)
            }
            
            btnFinance.setOnClickListener {
                findNavController().navigate(R.id.action_sellerHomeFragment_to_sellerFinanceFragment)
            }
            
            btnSales.setOnClickListener {
                findNavController().navigate(R.id.action_sellerHomeFragment_to_sellerSalesFragment)
            }
            
            btnAds.setOnClickListener {
                showToast("Quảng cáo Shopee")
            }
            
            btnMarketing.setOnClickListener {
                showToast("Kênh Marketing")
            }
            
            btnSupport.setOnClickListener {
                showToast("Trung tâm hỗ trợ")
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                renderUiState(state)
            }
        }
    }

    private fun renderUiState(state: SellerHomeUiState) {
        binding.apply {
            // Show/hide loading
            progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
            
            // Update shop profile
            state.shopProfile?.let { shop ->
                tvShopName.text = shop.name
                tvShopUrl.text = shop.url
                if (shop.notificationCount > 0) {
                    tvNotificationBadge.visibility = View.VISIBLE
                    tvNotificationBadge.text = shop.notificationCount.toString()
                } else {
                    tvNotificationBadge.visibility = View.GONE
                }
            }
            
            // Update stats
            tvPendingCount.text = state.stats.pendingOrders.toString()
            tvCancelledCount.text = state.stats.cancelledOrders.toString()
            tvReturnCount.text = state.stats.returnOrders.toString()
            tvReviewCount.text = state.stats.reviewsToRespond.toString()
            
            // Show error message
            state.errorMessage?.let { message ->
                showError(message)
            }
        }
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

