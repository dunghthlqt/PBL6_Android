package com.demo.pbl6_android.ui.seller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.demo.pbl6_android.databinding.FragmentSellerDashboardBinding

class SellerDashboardFragment : Fragment() {

    private var _binding: FragmentSellerDashboardBinding? = null
    private val binding: FragmentSellerDashboardBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellerDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDashboard()
    }

    private fun setupDashboard() {
        // TODO: Implement dashboard UI
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

