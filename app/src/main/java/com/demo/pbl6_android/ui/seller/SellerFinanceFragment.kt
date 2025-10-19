package com.demo.pbl6_android.ui.seller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.demo.pbl6_android.databinding.FragmentSellerFinanceBinding
import com.demo.pbl6_android.utils.formatCurrency
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class SellerFinanceFragment : Fragment() {

    private var _binding: FragmentSellerFinanceBinding? = null
    private val binding: FragmentSellerFinanceBinding
        get() = _binding!!
    
    private val viewModel: SellerFinanceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellerFinanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupListeners()
        observeUiState()
    }

    private fun setupToolbar() {
        binding.apply {
            btnBack.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            btnSettings.setOnClickListener {
                viewModel.handleEvent(SellerFinanceEvent.OpenSettings)
                showToast("Cài đặt tài chính")
            }
        }
    }

    private fun setupListeners() {
        binding.apply {
            tvTransactionHistory.setOnClickListener {
                viewModel.handleEvent(SellerFinanceEvent.ViewTransactionHistory)
                showToast("Lịch sử giao dịch")
            }
            btnWithdraw.setOnClickListener {
                viewModel.handleEvent(SellerFinanceEvent.WithdrawMoney)
                showToast("Rút tiền")
            }
            revenueSection.setOnClickListener {
                viewModel.handleEvent(SellerFinanceEvent.ViewRevenue)
                showToast("Doanh thu đơn hàng")
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

    private fun renderUiState(state: SellerFinanceUiState) {
        binding.apply {
            progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
            tvTotalBalance.text = state.totalBalance.formatCurrency()
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

