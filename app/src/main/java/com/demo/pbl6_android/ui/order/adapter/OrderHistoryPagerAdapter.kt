package com.demo.pbl6_android.ui.order.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.demo.pbl6_android.data.model.OrderStatus
import com.demo.pbl6_android.ui.order.OrderListFragment

class OrderHistoryPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val statuses = listOf(
        OrderStatus.PENDING_PAYMENT,
        OrderStatus.PENDING_PICKUP,
        OrderStatus.SHIPPING,
        OrderStatus.DELIVERED,
        OrderStatus.RETURN_REFUND,
        OrderStatus.CANCELLED
    )

    override fun getItemCount(): Int = statuses.size

    override fun createFragment(position: Int): Fragment {
        return OrderListFragment.newInstance(statuses[position])
    }
}

