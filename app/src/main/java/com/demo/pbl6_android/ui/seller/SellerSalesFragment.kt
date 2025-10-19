package com.demo.pbl6_android.ui.seller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.demo.pbl6_android.databinding.FragmentSellerSalesBinding
import com.demo.pbl6_android.ui.seller.adapter.SalesMetricAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class SellerSalesFragment : Fragment() {

    private var _binding: FragmentSellerSalesBinding? = null
    private val binding: FragmentSellerSalesBinding
        get() = _binding!!
    
    private val viewModel: SellerSalesViewModel by viewModels()
    private lateinit var metricAdapter: SalesMetricAdapter
    
    private var tabListener: TabLayout.OnTabSelectedListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellerSalesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupTabs()
        setupTimeFilters()
        setupMetricsRecyclerView()
        setupListeners()
        observeUiState()
    }

    private fun setupToolbar() {
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupTabs() {
        binding.tabLayout.apply {
            if (tabCount == 0) {
                addTab(newTab().setText("Doanh số"))
                addTab(newTab().setText("Sản Phẩm"))
            }
            tabListener?.let { removeOnTabSelectedListener(it) }
            tabListener = object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    val salesTab = if (tab.position == 0) SalesTab.REVENUE else SalesTab.PRODUCTS
                    viewModel.handleEvent(SellerSalesEvent.SelectTab(salesTab))
                }
                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            }
            addOnTabSelectedListener(tabListener!!)
        }
    }

    private fun setupTimeFilters() {
        binding.apply {
            chipToday.setOnClickListener {
                viewModel.handleEvent(SellerSalesEvent.SelectTimeFilter(TimeFilter.TODAY))
            }
            chipYesterday.setOnClickListener {
                viewModel.handleEvent(SellerSalesEvent.SelectTimeFilter(TimeFilter.YESTERDAY))
            }
            chip7days.setOnClickListener {
                viewModel.handleEvent(SellerSalesEvent.SelectTimeFilter(TimeFilter.LAST_7_DAYS))
            }
            chip30days.setOnClickListener {
                viewModel.handleEvent(SellerSalesEvent.SelectTimeFilter(TimeFilter.LAST_30_DAYS))
            }
            chipByDay.setOnClickListener {
                viewModel.handleEvent(SellerSalesEvent.SelectTimeFilter(TimeFilter.BY_DAY))
            }
            chipByWeek.setOnClickListener {
                viewModel.handleEvent(SellerSalesEvent.SelectTimeFilter(TimeFilter.BY_WEEK))
            }
            chipByMonth.setOnClickListener {
                viewModel.handleEvent(SellerSalesEvent.SelectTimeFilter(TimeFilter.BY_MONTH))
            }
        }
    }

    private fun setupMetricsRecyclerView() {
        metricAdapter = SalesMetricAdapter()
        binding.rvMetrics.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = metricAdapter
        }
    }

    private fun setupListeners() {
        binding.apply {
            filterDropdownContainer.setOnClickListener {
                viewModel.handleEvent(SellerSalesEvent.OpenFilterDropdown)
                showToast("Chọn loại đơn hàng")
            }
            btnCategoryFilter.setOnClickListener {
                viewModel.handleEvent(SellerSalesEvent.OpenCategoryFilter)
                showToast("Chọn ngành hàng")
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

    private fun renderUiState(state: SellerSalesUiState) {
        binding.apply {
            progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
            tvDateRange.text = state.dateRange
            metricAdapter.submitList(state.metrics)
            when (state.selectedTab) {
                SalesTab.REVENUE -> {
                    filterDropdownContainer.visibility = View.VISIBLE
                    productRankingSection.visibility = View.GONE
                }
                SalesTab.PRODUCTS -> {
                    filterDropdownContainer.visibility = View.GONE
                    productRankingSection.visibility = View.VISIBLE
                }
            }
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
        _binding?.let { binding ->
            tabListener?.let { binding.tabLayout.removeOnTabSelectedListener(it) }
        }
        tabListener = null
        _binding = null
    }
}

