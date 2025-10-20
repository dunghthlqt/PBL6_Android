package com.demo.pbl6_android.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.demo.pbl6_android.R
import com.demo.pbl6_android.data.NotificationRepository
import com.demo.pbl6_android.data.auth.AuthManager
import com.demo.pbl6_android.databinding.FragmentNotificationBinding
import com.demo.pbl6_android.ui.notification.adapter.NotificationPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding: FragmentNotificationBinding
        get() = _binding!!

    private lateinit var pagerAdapter: NotificationPagerAdapter
    private lateinit var authManager: AuthManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authManager = AuthManager.getInstance(requireContext())
        setupViews()
        setupViewPager()
        setupTabLayout()
    }

    private fun setupViews() {
        binding.apply {
            btnCart.setOnClickListener {
                if (!authManager.isUserLoggedIn()) {
                    navigateToLogin()
                } else {
                    findNavController().navigate(R.id.action_notificationFragment_to_cartFragment)
                }
            }

            btnChat.setOnClickListener {
                if (!authManager.isUserLoggedIn()) {
                    navigateToLogin()
                } else {
                    findNavController().navigate(R.id.action_notificationFragment_to_messageListFragment)
                }
            }
        }
    }

    private fun setupViewPager() {
        pagerAdapter = NotificationPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter
    }

    private fun setupTabLayout() {
        val myNotificationsCount = NotificationRepository.getTotalMyNotificationsCount()
        val sellerNotificationsCount = NotificationRepository.getTotalSellerNotificationsCount()

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> if (myNotificationsCount > 0) {
                    "Thông báo của tôi $myNotificationsCount"
                } else {
                    "Thông báo của tôi"
                }
                1 -> if (sellerNotificationsCount > 0) {
                    "Cập nhật người bán $sellerNotificationsCount"
                } else {
                    "Cập nhật người bán $sellerNotificationsCount"
                }
                else -> ""
            }
        }.attach()
    }

    private fun navigateToLogin() {
        requireActivity().finish()
        val intent = android.content.Intent(
            requireContext(),
            com.demo.pbl6_android.LoginActivity::class.java
        )
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

