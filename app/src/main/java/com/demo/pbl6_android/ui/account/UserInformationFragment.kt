package com.demo.pbl6_android.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.demo.pbl6_android.LoginActivity
import com.demo.pbl6_android.R
import com.demo.pbl6_android.data.UserManager
import com.demo.pbl6_android.data.auth.AuthManager
import com.demo.pbl6_android.databinding.FragmentUserInformationBinding
import com.demo.pbl6_android.databinding.ItemInfoFieldBinding
import com.demo.pbl6_android.databinding.ItemSwitchSettingBinding
import kotlinx.coroutines.launch

class UserInformationFragment : Fragment() {

    private var _binding: FragmentUserInformationBinding? = null
    private val binding: FragmentUserInformationBinding
        get() = _binding!!
    
    private lateinit var authManager: AuthManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        authManager = AuthManager.getInstance(requireContext())
        
        setupViews()
        setupPersonalInfoFields()
        setupNotificationSettings()
        setupPrivacySettings()
        observeUserData()
    }

    private fun setupViews() {
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
            
            btnChangeAvatar.setOnClickListener {
                // TODO: Open image picker
                showToast("Chức năng đổi ảnh đại diện đang phát triển")
            }
            
            btnLogout.setOnClickListener {
                showLogoutConfirmation()
            }
            
            btnDeleteAccount.setOnClickListener {
                showDeleteAccountConfirmation()
            }
        }
    }

    private fun setupPersonalInfoFields() {
        binding.apply {
            // Full Name
            ItemInfoFieldBinding.bind(fieldFullName.root).apply {
                tvLabel.text = "Họ và tên"
                root.setOnClickListener { showToast("Chức năng chỉnh sửa họ tên đang phát triển") }
            }
            
            // Email
            ItemInfoFieldBinding.bind(fieldEmail.root).apply {
                tvLabel.text = "Email"
                root.setOnClickListener { showToast("Chức năng chỉnh sửa email đang phát triển") }
            }
            
            // Phone
            ItemInfoFieldBinding.bind(fieldPhone.root).apply {
                tvLabel.text = "Số điện thoại"
                root.setOnClickListener { showToast("Chức năng chỉnh sửa SĐT đang phát triển") }
            }
            
            // Birthday
            ItemInfoFieldBinding.bind(fieldBirthday.root).apply {
                tvLabel.text = "Ngày sinh"
                root.setOnClickListener { showToast("Chức năng chỉnh sửa ngày sinh đang phát triển") }
            }
            
            // Gender
            ItemInfoFieldBinding.bind(fieldGender.root).apply {
                tvLabel.text = "Giới tính"
                root.setOnClickListener { showToast("Chức năng chỉnh sửa giới tính đang phát triển") }
            }
            
            // Password
            ItemInfoFieldBinding.bind(fieldPassword.root).apply {
                tvLabel.text = "Mật khẩu"
                tvValue.text = "••••••••"
                root.setOnClickListener { showToast("Chức năng đổi mật khẩu đang phát triển") }
            }
        }
    }

    private fun setupNotificationSettings() {
        binding.apply {
            // Order Notification
            ItemSwitchSettingBinding.bind(settingOrderNotification.root).apply {
                tvTitle.text = "Thông báo đơn hàng"
                tvDescription.text = "Nhận thông báo về trạng thái đơn hàng"
                
                switchToggle.setOnCheckedChangeListener { _, isChecked ->
                    UserManager.updateOrderNotification(isChecked)
                }
            }
            
            // Promotion
            ItemSwitchSettingBinding.bind(settingPromotion.root).apply {
                tvTitle.text = "Khuyến mãi"
                tvDescription.text = "Nhận thông báo về ưu đãi và khuyến mãi"
                
                switchToggle.setOnCheckedChangeListener { _, isChecked ->
                    UserManager.updatePromotionNotification(isChecked)
                }
            }
            
            // News
            ItemSwitchSettingBinding.bind(settingNews.root).apply {
                tvTitle.text = "Tin tức"
                tvDescription.text = "Nhận thông báo về tin tức mới"
                
                switchToggle.setOnCheckedChangeListener { _, isChecked ->
                    UserManager.updateNewsNotification(isChecked)
                }
            }
        }
    }

    private fun setupPrivacySettings() {
        binding.apply {
            // Show Phone
            ItemSwitchSettingBinding.bind(settingShowPhone.root).apply {
                tvTitle.text = "Hiển thị số điện thoại"
                tvDescription.text = "Cho phép người khác xem số điện thoại"
                
                switchToggle.setOnCheckedChangeListener { _, isChecked ->
                    UserManager.updateShowPhone(isChecked)
                }
            }
            
            // Show Email
            ItemSwitchSettingBinding.bind(settingShowEmail.root).apply {
                tvTitle.text = "Hiển thị email"
                tvDescription.text = "Cho phép người khác xem email"
                
                switchToggle.setOnCheckedChangeListener { _, isChecked ->
                    UserManager.updateShowEmail(isChecked)
                }
            }
        }
    }

    private fun observeUserData() {
        viewLifecycleOwner.lifecycleScope.launch {
            // Observe user
            UserManager.currentUser.collect { user ->
                user?.let {
                    binding.apply {
                        // Profile card
                        tvUserName.text = it.fullName
                        tvMemberSince.text = it.memberSince
                        
                        // Personal info fields
                        ItemInfoFieldBinding.bind(fieldFullName.root).tvValue.text = it.fullName
                        ItemInfoFieldBinding.bind(fieldEmail.root).tvValue.text = it.email
                        ItemInfoFieldBinding.bind(fieldPhone.root).tvValue.text = it.phoneNumber
                        ItemInfoFieldBinding.bind(fieldBirthday.root).tvValue.text = it.birthday
                        ItemInfoFieldBinding.bind(fieldGender.root).tvValue.text = it.gender
                        
                        // TODO: Load avatar using image loading library
                        // Glide.with(this@UserInformationFragment).load(it.avatarUrl).into(ivAvatar)
                    }
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            // Observe settings
            UserManager.userSettings.collect { settings ->
                binding.apply {
                    // Update switches without triggering listeners
                    ItemSwitchSettingBinding.bind(settingOrderNotification.root).switchToggle.apply {
                        setOnCheckedChangeListener(null)
                        isChecked = settings.orderNotification
                        setOnCheckedChangeListener { _, isChecked ->
                            UserManager.updateOrderNotification(isChecked)
                        }
                    }
                    
                    ItemSwitchSettingBinding.bind(settingPromotion.root).switchToggle.apply {
                        setOnCheckedChangeListener(null)
                        isChecked = settings.promotionNotification
                        setOnCheckedChangeListener { _, isChecked ->
                            UserManager.updatePromotionNotification(isChecked)
                        }
                    }
                    
                    ItemSwitchSettingBinding.bind(settingNews.root).switchToggle.apply {
                        setOnCheckedChangeListener(null)
                        isChecked = settings.newsNotification
                        setOnCheckedChangeListener { _, isChecked ->
                            UserManager.updateNewsNotification(isChecked)
                        }
                    }
                    
                    ItemSwitchSettingBinding.bind(settingShowPhone.root).switchToggle.apply {
                        setOnCheckedChangeListener(null)
                        isChecked = settings.showPhone
                        setOnCheckedChangeListener { _, isChecked ->
                            UserManager.updateShowPhone(isChecked)
                        }
                    }
                    
                    ItemSwitchSettingBinding.bind(settingShowEmail.root).switchToggle.apply {
                        setOnCheckedChangeListener(null)
                        isChecked = settings.showEmail
                        setOnCheckedChangeListener { _, isChecked ->
                            UserManager.updateShowEmail(isChecked)
                        }
                    }
                }
            }
        }
    }

    private fun showLogoutConfirmation() {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Đăng xuất")
            .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
            .setPositiveButton("Đăng xuất") { _, _ ->
                // Clear auth data
                authManager.logout()
                UserManager.logout()
                
                showToast("Đã đăng xuất")
                
                // Navigate to login screen
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun showDeleteAccountConfirmation() {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Xóa tài khoản")
            .setMessage("Hành động này không thể hoàn tác. Tất cả dữ liệu của bạn sẽ bị xóa vĩnh viễn. Bạn có chắc chắn muốn xóa tài khoản không?")
            .setPositiveButton("Xóa") { _, _ ->
                showToast("Chức năng xóa tài khoản đang phát triển")
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

