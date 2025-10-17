package com.demo.pbl6_android.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.demo.pbl6_android.MainActivity
import com.demo.pbl6_android.R
import com.demo.pbl6_android.data.ThemePreferences
import com.demo.pbl6_android.data.UserModeManager
import com.demo.pbl6_android.databinding.FragmentAccountBinding
import kotlinx.coroutines.launch

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding: FragmentAccountBinding
        get() = _binding!!
    
    private lateinit var themePreferences: ThemePreferences
    private lateinit var userModeManager: UserModeManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        themePreferences = ThemePreferences(requireContext())
        userModeManager = UserModeManager.getInstance(requireContext())
        
        setupMenuItems()
        setupSwitchToSellerButton()
        observeThemeSettings()
    }

    private fun setupMenuItems() {
        // View all orders - default to first tab (Pending Payment)
        binding.root.findViewById<View>(R.id.view_all_orders)?.setOnClickListener {
            navigateToOrderHistory(0)
        }
        
        // Setup order status buttons
        setupOrderStatusButtons()
        
        // Account management menus - setup with data
        setupMenuItem(
            R.id.menu_personal_info,
            R.drawable.ic_person,
            "Thông tin cá nhân",
            "Cập nhật thông tin tài khoản"
        ) {
            findNavController().navigate(R.id.action_accountFragment_to_userInformationFragment)
        }
        
        setupMenuItem(
            R.id.menu_address,
            R.drawable.ic_location,
            "Địa chỉ giao hàng",
            "Quản lý địa chỉ nhận hàng"
        ) {
            findNavController().navigate(R.id.action_accountFragment_to_shippingAddressFragment)
        }
        
        setupMenuItem(
            R.id.menu_payment,
            R.drawable.ic_payment,
            "Phương thức thanh toán",
            "Thẻ và ví điện tử"
        ) {
            findNavController().navigate(R.id.action_accountFragment_to_paymentMethodsFragment)
        }
        
        // Settings menus with switches
        setupMenuItemWithSwitch(
            R.id.menu_notification,
            R.drawable.ic_notification,
            "Thông báo",
            "Nhận thông báo khuyến mãi",
            true
        )
        
        setupDarkModeSwitch()
        
        setupMenuItem(
            R.id.menu_help,
            R.drawable.ic_help,
            "Trợ giúp & Hỗ trợ",
            "Câu hỏi thường gặp"
        ) {
            // TODO: Navigate to help & support
        }
        
        // Other menus
        setupMenuItem(
            R.id.menu_history,
            R.drawable.ic_history,
            "Lịch sử hoạt động",
            "Xem lại các hoạt động gần đây"
        ) {
            // TODO: Navigate to activity history
        }
        
        setupMenuItem(
            R.id.menu_share,
            R.drawable.ic_share,
            "Chia sẻ ứng dụng",
            "Giới thiệu cho bạn bè"
        ) {
            // TODO: Share app
        }
    }

    private fun setupOrderStatusButtons() {
        // Pending Payment - Tab 0
        setupOrderStatus(
            R.id.status_pending_confirmation,
            R.drawable.ic_order_payment,
            "Chờ thanh toán"
        ) {
            navigateToOrderHistory(0)
        }
        
        // Pending Pickup - Tab 1
        setupOrderStatus(
            R.id.status_pending_pickup,
            R.drawable.ic_order_pickup,
            "Chờ lấy hàng"
        ) {
            navigateToOrderHistory(1)
        }
        
        // Shipping - Tab 2
        setupOrderStatus(
            R.id.status_shipping,
            R.drawable.ic_order_shipping,
            "Chờ giao hàng"
        ) {
            navigateToOrderHistory(2)
        }
        
        // Return/Refund - Tab 4
        setupOrderStatus(
            R.id.status_return_refund,
            R.drawable.ic_order_return,
            "Trả hàng/Hoàn tiền"
        ) {
            navigateToOrderHistory(4)
        }
        
        // Review (Delivered) - Tab 3
        setupOrderStatus(
            R.id.status_review,
            R.drawable.ic_order_review,
            "Đánh giá"
        ) {
            navigateToOrderHistory(3)
        }
    }
    
    private fun setupOrderStatus(
        statusId: Int,
        iconRes: Int,
        label: String,
        onClick: () -> Unit
    ) {
        binding.root.findViewById<View>(statusId)?.apply {
            findViewById<android.widget.ImageView>(R.id.icon)?.setImageResource(iconRes)
            findViewById<android.widget.TextView>(R.id.label)?.text = label
            setOnClickListener { onClick() }
        }
    }
    
    private fun setupMenuItem(
        menuId: Int,
        iconRes: Int,
        title: String,
        subtitle: String,
        onClick: (() -> Unit)? = null
    ) {
        binding.root.findViewById<View>(menuId)?.apply {
            findViewById<android.widget.ImageView>(R.id.menu_icon)?.setImageResource(iconRes)
            findViewById<android.widget.TextView>(R.id.menu_title)?.text = title
            findViewById<android.widget.TextView>(R.id.menu_subtitle)?.text = subtitle
            onClick?.let { setOnClickListener { it() } }
        }
    }

    private fun setupMenuItemWithSwitch(
        menuId: Int,
        iconRes: Int,
        title: String,
        subtitle: String,
        isChecked: Boolean
    ) {
        binding.root.findViewById<View>(menuId)?.apply {
            findViewById<android.widget.ImageView>(R.id.menu_icon)?.setImageResource(iconRes)
            findViewById<android.widget.TextView>(R.id.menu_title)?.text = title
            findViewById<android.widget.TextView>(R.id.menu_subtitle)?.text = subtitle
            findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.menu_switch)?.isChecked = isChecked
        }
    }

    private fun setupNotificationSwitch() {
        val notificationSwitch = binding.root.findViewById<View>(R.id.menu_notification)
            ?.findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.menu_switch)
        
        notificationSwitch?.setOnCheckedChangeListener { _, isChecked ->
            // TODO: Save notification preference
        }
    }

    private fun setupDarkModeSwitch() {
        // Setup UI
        setupMenuItemWithSwitch(
            R.id.menu_dark_mode,
            R.drawable.ic_dark_mode,
            "Chế độ tối",
            "Giao diện tối cho mắt",
            false
        )
        
        // Setup listener
        val darkModeSwitch = binding.root.findViewById<View>(R.id.menu_dark_mode)
            ?.findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.menu_switch)
        
        darkModeSwitch?.setOnCheckedChangeListener { _, isChecked ->
            viewLifecycleOwner.lifecycleScope.launch {
                themePreferences.saveDarkModeEnabled(isChecked)
            }
        }
    }

    private fun observeThemeSettings() {
        viewLifecycleOwner.lifecycleScope.launch {
            themePreferences.isDarkModeEnabled.collect { isDarkMode ->
                val darkModeSwitch = binding.root.findViewById<View>(R.id.menu_dark_mode)
                    ?.findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.menu_switch)
                darkModeSwitch?.isChecked = isDarkMode
            }
        }
    }
    
    private fun navigateToOrderHistory(tabIndex: Int) {
        val bundle = Bundle().apply {
            putInt("initialTab", tabIndex)
        }
        findNavController().navigate(R.id.action_accountFragment_to_orderHistoryFragment, bundle)
    }
    
    private fun setupSwitchToSellerButton() {
        binding.btnSwitchToSeller.setOnClickListener {
            userModeManager.switchToSeller()
            restartActivity()
        }
    }
    
    private fun restartActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
