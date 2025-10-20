package com.demo.pbl6_android.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.pbl6_android.data.NotificationRepository
import com.demo.pbl6_android.data.model.NotificationItem
import com.demo.pbl6_android.databinding.FragmentNotificationListBinding
import com.demo.pbl6_android.ui.notification.adapter.NotificationAdapter

class NotificationListFragment : Fragment() {

    private var _binding: FragmentNotificationListBinding? = null
    private val binding: FragmentNotificationListBinding
        get() = _binding!!

    private lateinit var notificationAdapter: NotificationAdapter
    private var notificationType: Int = TYPE_MY_NOTIFICATIONS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationType = arguments?.getInt(ARG_NOTIFICATION_TYPE, TYPE_MY_NOTIFICATIONS) ?: TYPE_MY_NOTIFICATIONS
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadNotifications()
    }

    private fun setupRecyclerView() {
        binding.rvNotifications.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun loadNotifications() {
        val notifications = when (notificationType) {
            TYPE_MY_NOTIFICATIONS -> NotificationRepository.getMyNotifications()
            TYPE_SELLER_NOTIFICATIONS -> NotificationRepository.getSellerNotifications()
            else -> emptyList()
        }

        if (notifications.isEmpty()) {
            binding.emptyState.visibility = View.VISIBLE
            binding.rvNotifications.visibility = View.GONE
        } else {
            binding.emptyState.visibility = View.GONE
            binding.rvNotifications.visibility = View.VISIBLE

            notificationAdapter = NotificationAdapter(notifications) { notification ->
                handleNotificationClick(notification)
            }
            binding.rvNotifications.adapter = notificationAdapter
        }
    }

    private fun handleNotificationClick(notification: NotificationItem) {
        Toast.makeText(
            requireContext(),
            "Clicked: ${notification.title}",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_NOTIFICATION_TYPE = "notification_type"
        const val TYPE_MY_NOTIFICATIONS = 0
        const val TYPE_SELLER_NOTIFICATIONS = 1

        fun newInstance(type: Int): NotificationListFragment {
            return NotificationListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_NOTIFICATION_TYPE, type)
                }
            }
        }
    }
}

