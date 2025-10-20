package com.demo.pbl6_android.ui.notification.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.demo.pbl6_android.ui.notification.NotificationListFragment

class NotificationPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NotificationListFragment.newInstance(NotificationListFragment.TYPE_MY_NOTIFICATIONS)
            1 -> NotificationListFragment.newInstance(NotificationListFragment.TYPE_SELLER_NOTIFICATIONS)
            else -> NotificationListFragment.newInstance(NotificationListFragment.TYPE_MY_NOTIFICATIONS)
        }
    }
}

