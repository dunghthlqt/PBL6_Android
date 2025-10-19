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
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.pbl6_android.databinding.FragmentSellerProductsBinding
import com.demo.pbl6_android.ui.seller.adapter.SellerProductAdapter
import com.demo.pbl6_android.ui.seller.model.SellerProduct
import com.demo.pbl6_android.ui.seller.model.SellerProductStatus
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class SellerProductsFragment : Fragment() {

    private var _binding: FragmentSellerProductsBinding? = null
    private val binding: FragmentSellerProductsBinding
        get() = _binding!!
    
    private val viewModel: SellerProductsViewModel by viewModels()
    private lateinit var productAdapter: SellerProductAdapter
    
    private var tabListener: TabLayout.OnTabSelectedListener? = null

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
        setupToolbar()
        setupTabs()
        setupRecyclerView()
        setupWarningBanner()
        setupAddProductButton()
        observeUiState()
    }

    private fun setupToolbar() {
        binding.apply {
            btnBack.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            btnSearch.setOnClickListener {
                showToast("Tìm kiếm sản phẩm")
            }
            btnChat.setOnClickListener {
                showToast("Tin nhắn")
            }
        }
    }

    private fun setupTabs() {
        binding.tabLayout.apply {
            if (tabCount == 0) {
                SellerProductStatus.values().forEach { status ->
                    addTab(newTab().setText(status.displayName))
                }
            }
            tabListener?.let { removeOnTabSelectedListener(it) }
            tabListener = object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    val status = SellerProductStatus.values()[tab.position]
                    viewModel.handleEvent(SellerProductsEvent.SelectTab(status))
                }
                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            }
            addOnTabSelectedListener(tabListener!!)
        }
    }

    private fun setupRecyclerView() {
        productAdapter = SellerProductAdapter(
            onEditClick = { product ->
                viewModel.handleEvent(SellerProductsEvent.EditProduct(product))
                showToast("Sửa sản phẩm: ${product.name}")
            },
            onHideClick = { product ->
                showHideProductDialog(product)
            },
            onMenuClick = { product ->
                showProductMenu(product)
            }
        )
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productAdapter
        }
    }

    private fun setupWarningBanner() {
        binding.apply {
            btnWarningAction.setOnClickListener {
                viewModel.handleEvent(SellerProductsEvent.HandleWarningAction)
                showToast("Chuyển đến trang nạp tiền")
            }
        }
    }

    private fun setupAddProductButton() {
        binding.btnAddProduct.setOnClickListener {
            viewModel.handleEvent(SellerProductsEvent.AddNewProduct)
            showToast("Thêm sản phẩm mới")
        }
    }

    private fun showHideProductDialog(product: SellerProduct) {
        AlertDialog.Builder(requireContext())
            .setTitle("Ẩn sản phẩm")
            .setMessage("Bạn có chắc muốn ẩn sản phẩm \"${product.name}\"?")
            .setPositiveButton("Ẩn") { _, _ ->
                viewModel.handleEvent(SellerProductsEvent.HideProduct(product))
                showToast("Đã ẩn sản phẩm")
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun showProductMenu(product: SellerProduct) {
        val options = arrayOf(
            "Xem chi tiết",
            "Sao chép",
            "Xóa sản phẩm"
        )
        AlertDialog.Builder(requireContext())
            .setTitle(product.name)
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showToast("Xem chi tiết sản phẩm")
                    1 -> showToast("Sao chép sản phẩm")
                    2 -> showDeleteProductDialog(product)
                }
            }
            .show()
    }

    private fun showDeleteProductDialog(product: SellerProduct) {
        AlertDialog.Builder(requireContext())
            .setTitle("Xóa sản phẩm")
            .setMessage("Bạn có chắc muốn xóa sản phẩm \"${product.name}\"? Hành động này không thể hoàn tác.")
            .setPositiveButton("Xóa") { _, _ ->
                showToast("Đã xóa sản phẩm")
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                renderUiState(state)
            }
        }
    }

    private fun renderUiState(state: SellerProductsUiState) {
        binding.apply {
            progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
            updateTabCounts(state.productCountByStatus)
            productAdapter.submitList(state.products)
            if (state.products.isEmpty() && !state.isLoading) {
                emptyState.visibility = View.VISIBLE
                rvProducts.visibility = View.GONE
            } else {
                emptyState.visibility = View.GONE
                rvProducts.visibility = View.VISIBLE
            }
            if (state.showWarningBanner && state.warningMessage != null) {
                warningBanner.visibility = View.VISIBLE
                tvWarningMessage.text = state.warningMessage
            } else {
                warningBanner.visibility = View.GONE
            }
            state.errorMessage?.let { message ->
                showError(message)
            }
        }
    }

    private fun updateTabCounts(productCountByStatus: Map<SellerProductStatus, Int>) {
        binding.tabLayout.apply {
            SellerProductStatus.values().forEachIndexed { index, status ->
                val count = productCountByStatus[status] ?: 0
                getTabAt(index)?.text = "${status.displayName} ($count)"
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

