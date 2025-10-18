package com.demo.pbl6_android.ui.seller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.demo.pbl6_android.databinding.FragmentSellerAccountBinding

class SellerAccountFragment : Fragment() {

    private var _binding: FragmentSellerAccountBinding? = null
    private val binding: FragmentSellerAccountBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellerAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupListeners()
    }
    
    private fun setupListeners() {
        binding.apply {
            btnSwitchToBuyer.setOnClickListener {
                // Navigate back to buyer mode (pop back stack to account fragment)
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            
            menuShopInfo.setOnClickListener {
                // TODO: Navigate to shop info
            }
            
            menuNotifications.setOnClickListener {
                // TODO: Navigate to notification settings
            }
            
            menuSettings.setOnClickListener {
                // TODO: Navigate to general settings
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

