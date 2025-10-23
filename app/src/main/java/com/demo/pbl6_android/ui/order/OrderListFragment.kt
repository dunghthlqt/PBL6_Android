package com.demo.pbl6_android.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.pbl6_android.data.OrderRepository
import com.demo.pbl6_android.data.api.ApiResult
import com.demo.pbl6_android.data.model.OrderStatus
import com.demo.pbl6_android.databinding.FragmentOrderListBinding
import com.demo.pbl6_android.ui.order.adapter.OrderHistoryAdapter
import kotlinx.coroutines.launch

class OrderListFragment : Fragment() {

    private var _binding: FragmentOrderListBinding? = null
    private val binding: FragmentOrderListBinding
        get() = _binding!!

    private lateinit var orderAdapter: OrderHistoryAdapter
    private var orderStatus: OrderStatus? = null

    companion object {
        private const val ARG_ORDER_STATUS = "order_status"

        fun newInstance(status: OrderStatus): OrderListFragment {
            val fragment = OrderListFragment()
            val args = Bundle()
            args.putString(ARG_ORDER_STATUS, status.name)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val statusName = it.getString(ARG_ORDER_STATUS)
            orderStatus = statusName?.let { name -> OrderStatus.valueOf(name) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadOrders()
    }

    private fun setupRecyclerView() {
        orderAdapter = OrderHistoryAdapter(
            onContactShopClick = { order ->
                // TODO: Implement contact shop
            },
            onCancelOrderClick = { order ->
                // TODO: Implement cancel order
            },
            onReceivedClick = { order ->
                // TODO: Implement mark as received
            },
            onReturnRefundClick = { order ->
                // TODO: Implement return/refund
            },
            onReviewClick = { order ->
                // TODO: Implement review
            },
            onBuyAgainClick = { order ->
                // TODO: Implement buy again
            },
            onViewDetailsClick = { order ->
                navigateToOrderDetail(order)
            },
            onPayNowClick = { order ->
                // TODO: Implement pay now
            }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }
    }

    private fun loadOrders() {
        showLoading(true)
        
        viewLifecycleOwner.lifecycleScope.launch {
            val result = orderStatus?.let { status ->
                OrderRepository.getOrdersByStatus(status, page = 1, size = 50)
            } ?: OrderRepository.getAllOrders(page = 1, size = 50)
            
            showLoading(false)
            
            when (result) {
                is ApiResult.Success -> {
                    val orders = result.data
                    orderAdapter.submitList(orders)
                    updateEmptyState(orders.isEmpty())
                }
                is ApiResult.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Lỗi tải đơn hàng: ${result.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateEmptyState(true)
                }
                is ApiResult.Loading -> {
                    // Already showing loading
                }
            }
        }
    }
    
    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                recyclerView.visibility = View.GONE
                emptyState.visibility = View.GONE
                // TODO: Add proper loading indicator if needed
            } else {
                // Visibility will be set by updateEmptyState
            }
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        binding.emptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun navigateToOrderDetail(order: com.demo.pbl6_android.data.model.Order) {
        val bundle = Bundle().apply {
            putString("orderId", order.orderId)
        }
        
        // Navigate from parent fragment (OrderHistoryFragment)
        parentFragment?.findNavController()?.navigate(
            com.demo.pbl6_android.R.id.action_orderHistoryFragment_to_orderDetailFragment,
            bundle
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

