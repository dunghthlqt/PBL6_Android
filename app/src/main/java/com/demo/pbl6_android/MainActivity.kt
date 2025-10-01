package com.demo.pbl6_android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.demo.pbl6_android.data.ThemePreferences
import com.demo.pbl6_android.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var themePreferences: ThemePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        themePreferences = ThemePreferences(this)
        applyTheme()
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupNavigation()
        observeThemeChanges()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        
        // Connect BottomNavigationView with NavController
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    if (navController.currentDestination?.id != R.id.landingPageFragment) {
                        navController.navigate(R.id.landingPageFragment)
                    }
                    true
                }
                R.id.nav_category -> {
                    // TODO: Navigate to category
                    true
                }
                R.id.nav_notification -> {
                    // TODO: Navigate to notifications
                    true
                }
                R.id.nav_cart -> {
                    if (navController.currentDestination?.id != R.id.cartFragment) {
                        navController.navigate(R.id.cartFragment)
                    }
                    true
                }
                R.id.nav_account -> {
                    if (navController.currentDestination?.id != R.id.accountFragment) {
                        navController.navigate(R.id.accountFragment)
                    }
                    true
                }
                else -> false
            }
        }
        
        // Update selected item when destination changes
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.landingPageFragment -> binding.bottomNavigation.selectedItemId = R.id.nav_home
                R.id.accountFragment -> binding.bottomNavigation.selectedItemId = R.id.nav_account
                R.id.cartFragment -> binding.bottomNavigation.selectedItemId = R.id.nav_cart
            }
            
            // Hide bottom navigation for payment flow screens
            val paymentFlowScreens = setOf(
                R.id.orderFragment,
                R.id.shopVoucherFragment,
                R.id.platformVoucherFragment,
                R.id.shippingMethodFragment
            )
            
            if (destination.id in paymentFlowScreens) {
                binding.bottomNavigation.visibility = android.view.View.GONE
            } else {
                binding.bottomNavigation.visibility = android.view.View.VISIBLE
            }
        }
    }

    private fun applyTheme() {
        lifecycleScope.launch {
            themePreferences.isDarkModeEnabled.collect { isDarkMode ->
                val mode = if (isDarkMode) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
                AppCompatDelegate.setDefaultNightMode(mode)
            }
        }
    }

    private fun observeThemeChanges() {
        lifecycleScope.launch {
            themePreferences.isDarkModeEnabled.collect { isDarkMode ->
                val currentMode = when (AppCompatDelegate.getDefaultNightMode()) {
                    AppCompatDelegate.MODE_NIGHT_YES -> true
                    else -> false
                }
                
                if (currentMode != isDarkMode) {
                    val mode = if (isDarkMode) {
                        AppCompatDelegate.MODE_NIGHT_YES
                    } else {
                        AppCompatDelegate.MODE_NIGHT_NO
                    }
                    AppCompatDelegate.setDefaultNightMode(mode)
                }
            }
        }
    }
}