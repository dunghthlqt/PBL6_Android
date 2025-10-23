package com.demo.pbl6_android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.demo.pbl6_android.data.CartManager
import com.demo.pbl6_android.data.ThemePreferences
import com.demo.pbl6_android.data.UserMode
import com.demo.pbl6_android.data.UserModeManager
import com.demo.pbl6_android.data.auth.AuthManager
import com.demo.pbl6_android.databinding.ActivityMainBinding
import com.google.android.material.badge.BadgeDrawable
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var themePreferences: ThemePreferences
    private lateinit var authManager: AuthManager
    private lateinit var userModeManager: UserModeManager
    private var cartBadge: BadgeDrawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply theme BEFORE super.onCreate() to prevent recreation
        themePreferences = ThemePreferences(this)
        applyThemeSync()
        
        super.onCreate(savedInstanceState)
        
        authManager = AuthManager.getInstance(this)
        userModeManager = UserModeManager.getInstance(this)
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupNavigationBasedOnMode()
        observeCartBadge()
        loadCartOnStart()
    }
    
    override fun onSaveInstanceState(outState: Bundle) {
        // Don't save navigation state to prevent crash when switching modes
        super.onSaveInstanceState(Bundle())
    }
    
    private fun setupNavigationBasedOnMode() {
        // Always setup buyer navigation - seller mode is accessed via navigation
        setupBuyerNavigation()
    }

    private fun setupBuyerNavigation() {
        // Set buyer menu
        binding.bottomNavigation.menu.clear()
        binding.bottomNavigation.inflateMenu(R.menu.bottom_nav_menu)
        
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        
        // Set buyer navigation graph
        navController.setGraph(R.navigation.nav_graph)
        
        // Set default selected item to Home
        binding.bottomNavigation.selectedItemId = R.id.nav_home
        
        // Connect BottomNavigationView with NavController for Buyer
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    if (navController.currentDestination?.id != R.id.landingPageFragment) {
                        navController.navigate(R.id.landingPageFragment)
                    }
                    true
                }
                R.id.nav_notification -> {
                    if (navController.currentDestination?.id != R.id.notificationFragment) {
                        navController.navigate(R.id.notificationFragment)
                    }
                    true
                }
                R.id.nav_message -> {
                    if (!authManager.isUserLoggedIn()) {
                        navigateToLogin()
                        false
                    } else {
                        if (navController.currentDestination?.id != R.id.messageListFragment) {
                            navController.navigate(R.id.messageListFragment)
                        }
                        true
                    }
                }
                R.id.nav_cart -> {
                    if (!authManager.isUserLoggedIn()) {
                        navigateToLogin()
                        false
                    } else {
                        if (navController.currentDestination?.id != R.id.cartFragment) {
                            navController.navigate(R.id.cartFragment)
                        }
                        true
                    }
                }
                R.id.nav_account -> {
                    if (!authManager.isUserLoggedIn()) {
                        navigateToLogin()
                        false
                    } else {
                        if (navController.currentDestination?.id != R.id.accountFragment) {
                            navController.navigate(R.id.accountFragment)
                        }
                        true
                    }
                }
                else -> false
            }
        }
        
        // Update selected item when destination changes
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.landingPageFragment -> binding.bottomNavigation.selectedItemId = R.id.nav_home
                R.id.notificationFragment -> binding.bottomNavigation.selectedItemId = R.id.nav_notification
                R.id.accountFragment -> binding.bottomNavigation.selectedItemId = R.id.nav_account
                R.id.cartFragment -> binding.bottomNavigation.selectedItemId = R.id.nav_cart
                R.id.messageListFragment -> binding.bottomNavigation.selectedItemId = R.id.nav_message
            }
            
            // Hide bottom navigation for payment flow screens and product detail
            val hideBottomNavScreens = setOf(
                R.id.orderFragment,
                R.id.shopVoucherFragment,
                R.id.platformVoucherFragment,
                R.id.platformVoucherSelectionFragment,
                R.id.shippingMethodFragment,
                R.id.productDetailFragment,
                R.id.orderHistoryFragment,
                R.id.orderDetailFragment,
                R.id.chatFragment,
                R.id.userInformationFragment,
                R.id.shippingAddressFragment,
                R.id.paymentMethodsFragment,
                R.id.categoryProductsFragment,
                R.id.shopFragment,
                R.id.sellerHomeFragment,
                R.id.sellerAccountFragment,
                R.id.sellerOrderHistoryFragment,
                R.id.sellerOrderDetailFragment,
                R.id.sellerCancelledOrderDetailFragment,
                R.id.sellerProductsFragment,
                R.id.sellerFinanceFragment,
                R.id.sellerSalesFragment,
                R.id.searchInputFragment,
                R.id.searchResultsFragment,
                R.id.productReviewsFragment,
                R.id.addressSelectionFragment,
                R.id.addressFormFragment,
                R.id.orderStatusFragment
            )
            
            if (destination.id in hideBottomNavScreens) {
                binding.bottomNavigation.visibility = android.view.View.GONE
            } else {
                binding.bottomNavigation.visibility = android.view.View.VISIBLE
            }
        }
    }

    /**
     * Apply theme synchronously to prevent activity recreation
     * This is called BEFORE super.onCreate()
     */
    private fun applyThemeSync() {
        // Get current theme preference (blocking call is OK here since it's during startup)
        val sharedPrefs = getSharedPreferences("theme_prefs", MODE_PRIVATE)
        val isDarkMode = sharedPrefs.getBoolean("dark_mode", false)
        
        val mode = if (isDarkMode) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        
        // Only apply if different from current mode
        val currentMode = AppCompatDelegate.getDefaultNightMode()
        if (currentMode != mode) {
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }
    
    private fun observeCartBadge() {
        cartBadge = binding.bottomNavigation.getOrCreateBadge(R.id.nav_cart)
        cartBadge?.isVisible = false
        
        lifecycleScope.launch {
            CartManager.cartItemCount.collect { count ->
                if (count > 0) {
                    cartBadge?.number = count
                    cartBadge?.isVisible = true
                } else {
                    cartBadge?.isVisible = false
                }
            }
        }
    }
    
    /**
     * Load cart on app start if user is logged in
     */
    private fun loadCartOnStart() {
        // Only load cart if user is logged in
        if (authManager.isUserLoggedIn()) {
            lifecycleScope.launch {
                android.util.Log.d("MainActivity", "🛒 Loading cart on app start...")
                val result = CartManager.loadCart()
                
                when (result) {
                    is com.demo.pbl6_android.data.api.ApiResult.Success -> {
                        android.util.Log.d("MainActivity", "✅ Cart loaded successfully on start")
                    }
                    is com.demo.pbl6_android.data.api.ApiResult.Error -> {
                        android.util.Log.e("MainActivity", "❌ Failed to load cart on start: ${result.message}")
                        // Don't show error to user on start - cart will be empty
                    }
                    is com.demo.pbl6_android.data.api.ApiResult.Loading -> {}
                }
            }
        } else {
            android.util.Log.d("MainActivity", "ℹ️ User not logged in, skipping cart load")
        }
    }
    
    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}