package com.demo.pbl6_android.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.demo.pbl6_android.R
import com.demo.pbl6_android.data.model.OrderStatus
import com.demo.pbl6_android.databinding.FragmentOrderHistoryBinding
import com.demo.pbl6_android.ui.order.adapter.OrderHistoryPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OrderHistoryFragment : Fragment() {

    private var _binding: FragmentOrderHistoryBinding? = null
    private val binding: FragmentOrderHistoryBinding
        get() = _binding!!

    private lateinit var pagerAdapter: OrderHistoryPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupViewPager()
        setupTabLayout()
    }

    private fun setupViews() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSearch.setOnClickListener {
            // TODO: Implement search functionality
        }

        binding.btnChat.setOnClickListener {
            // TODO: Implement chat functionality
        }
    }

    private fun setupViewPager() {
        pagerAdapter = OrderHistoryPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter
    }

    private fun setupTabLayout() {
        val statuses = listOf(
            OrderStatus.PENDING_PAYMENT,
            OrderStatus.PENDING_PICKUP,
            OrderStatus.SHIPPING,
            OrderStatus.DELIVERED,
            OrderStatus.RETURN_REFUND,
            OrderStatus.CANCELLED
        )

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val status = statuses[position]
            tab.text = "${status.displayName} (${getOrderCountForStatus(status)})"
        }.attach()
    }

    private fun getOrderCountForStatus(status: OrderStatus): Int {
        return com.demo.pbl6_android.data.OrderRepository.getOrderCountByStatus(status)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

