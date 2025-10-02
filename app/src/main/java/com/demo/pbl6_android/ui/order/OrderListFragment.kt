package com.demo.pbl6_android.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.pbl6_android.data.OrderRepository
import com.demo.pbl6_android.data.model.OrderStatus
import com.demo.pbl6_android.databinding.FragmentOrderListBinding
import com.demo.pbl6_android.ui.order.adapter.OrderHistoryAdapter

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
                // TODO: Implement view details
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
        val orders = orderStatus?.let { status ->
            OrderRepository.getOrdersByStatus(status)
        } ?: emptyList()

        orderAdapter.submitList(orders)
        updateEmptyState(orders.isEmpty())
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        binding.emptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

