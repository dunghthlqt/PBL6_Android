package com.demo.pbl6_android.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.demo.pbl6_android.R
import com.demo.pbl6_android.data.ThemePreferences
import com.demo.pbl6_android.databinding.FragmentAccountBinding
import kotlinx.coroutines.launch

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding: FragmentAccountBinding
        get() = _binding!!
    
    private lateinit var themePreferences: ThemePreferences

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
        
        setupMenuItems()
        observeThemeSettings()
    }

    private fun setupMenuItems() {
        // View all orders
        binding.root.findViewById<View>(R.id.view_all_orders)?.setOnClickListener {
            // TODO: Navigate to order history
        }
        
        // Account management menus - setup with data
        setupMenuItem(
            R.id.menu_personal_info,
            R.drawable.ic_person,
            "Thông tin cá nhân",
            "Cập nhật thông tin tài khoản"
        ) {
            // TODO: Navigate to personal info
        }
        
        setupMenuItem(
            R.id.menu_address,
            R.drawable.ic_location,
            "Địa chỉ giao hàng",
            "Quản lý địa chỉ nhận hàng"
        ) {
            // TODO: Navigate to address management
        }
        
        setupMenuItem(
            R.id.menu_payment,
            R.drawable.ic_payment,
            "Phương thức thanh toán",
            "Thẻ và ví điện tử"
        ) {
            // TODO: Navigate to payment methods
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
            lifecycleScope.launch {
                themePreferences.saveDarkModeEnabled(isChecked)
            }
        }
    }

    private fun observeThemeSettings() {
        lifecycleScope.launch {
            themePreferences.isDarkModeEnabled.collect { isDarkMode ->
                val darkModeSwitch = binding.root.findViewById<View>(R.id.menu_dark_mode)
                    ?.findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.menu_switch)
                darkModeSwitch?.isChecked = isDarkMode
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
