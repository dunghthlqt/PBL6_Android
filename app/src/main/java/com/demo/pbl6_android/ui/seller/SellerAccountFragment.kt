package com.demo.pbl6_android.ui.seller

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.demo.pbl6_android.MainActivity
import com.demo.pbl6_android.data.UserModeManager
import com.demo.pbl6_android.databinding.FragmentSellerAccountBinding
import kotlinx.coroutines.launch

class SellerAccountFragment : Fragment() {

    private var _binding: FragmentSellerAccountBinding? = null
    private val binding: FragmentSellerAccountBinding
        get() = _binding!!
    
    private lateinit var userModeManager: UserModeManager

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
        
        userModeManager = UserModeManager.getInstance(requireContext())
        setupSellerAccount()
        setupSwitchToBuyerButton()
    }

    private fun setupSellerAccount() {
        // TODO: Implement seller account UI - load shop information, stats, etc.
    }
    
    private fun setupSwitchToBuyerButton() {
        binding.btnSwitchToBuyer.setOnClickListener {
            userModeManager.switchToBuyer()
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

