package com.demo.pbl6_android.ui.seller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.pbl6_android.R
import com.demo.pbl6_android.databinding.FragmentSellerOrderHistoryBinding
import com.demo.pbl6_android.ui.seller.adapter.SellerOrderAdapter
import com.demo.pbl6_android.ui.seller.model.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class SellerOrderHistoryFragment : Fragment() {

    private var _binding: FragmentSellerOrderHistoryBinding? = null
    private val binding: FragmentSellerOrderHistoryBinding
        get() = _binding!!
    
    private val viewModel: SellerOrderHistoryViewModel by viewModels()
    private lateinit var orderAdapter: SellerOrderAdapter
    
    private var mainTabListener: TabLayout.OnTabSelectedListener? = null
    private var subTabListener: TabLayout.OnTabSelectedListener? = null
    private var currentMainTab: SellerOrderStatus? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellerOrderHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupToolbar()
        setupMainTabs()
        setupRecyclerView()
        setupFilters()
        observeUiState()
    }

    private fun setupToolbar() {
        binding.apply {
            btnBack.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            
            btnSearch.setOnClickListener {
                showToast("Tìm kiếm đơn hàng")
            }
            
            btnChat.setOnClickListener {
                showToast("Tin nhắn")
            }
        }
    }

    private fun setupMainTabs() {
        binding.mainTabs.apply {
            // Add tabs only once
            if (tabCount == 0) {
                SellerOrderStatus.values().forEach { status ->
                    addTab(newTab().setText(status.displayName))
                }
            }
            
            // Remove old listener if exists
            mainTabListener?.let { removeOnTabSelectedListener(it) }
            
            // Create and add new listener
            mainTabListener = object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    val status = SellerOrderStatus.values()[tab.position]
                    viewModel.handleEvent(SellerOrderHistoryEvent.SelectMainTab(status))
                }
                
                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            }
            addOnTabSelectedListener(mainTabListener!!)
        }
    }

    private fun setupSubTabs(mainStatus: SellerOrderStatus) {
        binding.subTabs.apply {
            // Remove old listener first to prevent duplicates
            subTabListener?.let { removeOnTabSelectedListener(it) }
            
            // Clear and add new tabs
            removeAllTabs()
            
            when (mainStatus) {
                SellerOrderStatus.PENDING_PICKUP -> {
                    PendingPickupSubStatus.values().forEach { subStatus ->
                        addTab(newTab().setText(subStatus.displayName))
                    }
                }
                SellerOrderStatus.CANCELLED -> {
                    CancelledSubStatus.values().forEach { subStatus ->
                        addTab(newTab().setText(subStatus.displayName))
                    }
                }
                SellerOrderStatus.RETURN_REFUND -> {
                    ReturnRefundSubStatus.values().forEach { subStatus ->
                        addTab(newTab().setText(subStatus.displayName))
                    }
                }
                SellerOrderStatus.DELIVERY_FAILED -> {
                    DeliveryFailedSubStatus.values().forEach { subStatus ->
                        addTab(newTab().setText(subStatus.displayName))
                    }
                }
                else -> {}
            }
            
            // Create and add new listener
            subTabListener = object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    when (mainStatus) {
                        SellerOrderStatus.PENDING_PICKUP -> {
                            val subStatus = PendingPickupSubStatus.values()[tab.position]
                            viewModel.handleEvent(SellerOrderHistoryEvent.SelectPendingPickupSubTab(subStatus))
                        }
                        SellerOrderStatus.CANCELLED -> {
                            val subStatus = CancelledSubStatus.values()[tab.position]
                            viewModel.handleEvent(SellerOrderHistoryEvent.SelectCancelledSubTab(subStatus))
                        }
                        SellerOrderStatus.RETURN_REFUND -> {
                            val subStatus = ReturnRefundSubStatus.values()[tab.position]
                            viewModel.handleEvent(SellerOrderHistoryEvent.SelectReturnRefundSubTab(subStatus))
                        }
                        SellerOrderStatus.DELIVERY_FAILED -> {
                            val subStatus = DeliveryFailedSubStatus.values()[tab.position]
                            viewModel.handleEvent(SellerOrderHistoryEvent.SelectDeliveryFailedSubTab(subStatus))
                        }
                        else -> {}
                    }
                }
                
                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            }
            addOnTabSelectedListener(subTabListener!!)
        }
    }

    private fun setupRecyclerView() {
        orderAdapter = SellerOrderAdapter { order ->
            // Navigate to appropriate detail screen based on order status
            when (order.status) {
                SellerOrderStatus.CANCELLED -> {
                    findNavController().navigate(R.id.action_sellerOrderHistoryFragment_to_sellerCancelledOrderDetailFragment)
                }
                else -> {
                    findNavController().navigate(R.id.action_sellerOrderHistoryFragment_to_sellerOrderDetailFragment)
                }
            }
        }
        
        binding.rvOrders.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }
    }

    private fun setupFilters() {
        binding.shippingFilterContainer.setOnClickListener {
            showShippingMethodDialog()
        }
        
        binding.sortFilterContainer.setOnClickListener {
            showSortTypeDialog()
        }
    }

    private fun showShippingMethodDialog() {
        val methods = viewModel.getShippingMethodsForCurrentTab()
        val methodNames = methods.map { it.displayName }.toTypedArray()
        val currentMethod = viewModel.uiState.value.selectedShippingMethod
        val selectedIndex = methods.indexOf(currentMethod)
        
        AlertDialog.Builder(requireContext())
            .setTitle("Đơn vị vận chuyển")
            .setSingleChoiceItems(methodNames, selectedIndex) { dialog, which ->
                viewModel.handleEvent(
                    SellerOrderHistoryEvent.SelectShippingMethod(methods[which])
                )
                dialog.dismiss()
            }
            .show()
    }

    private fun showSortTypeDialog() {
        val sortTypes = OrderSortType.values()
        val sortTypeNames = sortTypes.map { it.displayName }.toTypedArray()
        val currentSortType = viewModel.uiState.value.selectedSortType
        val selectedIndex = sortTypes.indexOf(currentSortType)
        
        AlertDialog.Builder(requireContext())
            .setTitle("Sắp xếp theo")
            .setSingleChoiceItems(sortTypeNames, selectedIndex) { dialog, which ->
                viewModel.handleEvent(
                    SellerOrderHistoryEvent.SelectSortType(sortTypes[which])
                )
                dialog.dismiss()
            }
            .show()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                renderUiState(state)
            }
        }
    }

    private fun renderUiState(state: SellerOrderHistoryUiState) {
        binding.apply {
            // Show/hide loading
            progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
            
            // Show/hide sub tabs - only setup if main tab changed
            if (viewModel.hasSubTabs()) {
                subTabs.visibility = View.VISIBLE
                // Only setup sub tabs if main tab actually changed
                if (currentMainTab != state.selectedMainTab) {
                    currentMainTab = state.selectedMainTab
                    setupSubTabs(state.selectedMainTab)
                }
            } else {
                subTabs.visibility = View.GONE
                currentMainTab = state.selectedMainTab
            }
            
            // Show/hide filters
            if (state.selectedMainTab != SellerOrderStatus.RETURN_REFUND) {
                filterContainer.visibility = View.VISIBLE
                
                // Update filter texts
                tvShippingFilter.text = state.selectedShippingMethod.displayName
                
                // Show/hide sort filter (only for pending pickup)
                if (viewModel.hasSortFilter()) {
                    sortFilterContainer.visibility = View.VISIBLE
                    tvSortFilter.text = state.selectedSortType.displayName
                } else {
                    sortFilterContainer.visibility = View.GONE
                }
            } else {
                filterContainer.visibility = View.GONE
            }
            
            // Update order list
            orderAdapter.submitList(state.orders)
            
            // Show/hide empty state
            if (state.orders.isEmpty() && !state.isLoading) {
                emptyState.visibility = View.VISIBLE
                rvOrders.visibility = View.GONE
            } else {
                emptyState.visibility = View.GONE
                rvOrders.visibility = View.VISIBLE
            }
            
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
        
        // Clean up listeners safely
        _binding?.let { binding ->
            mainTabListener?.let { binding.mainTabs.removeOnTabSelectedListener(it) }
            subTabListener?.let { binding.subTabs.removeOnTabSelectedListener(it) }
        }
        
        mainTabListener = null
        subTabListener = null
        currentMainTab = null
        _binding = null
    }
}

