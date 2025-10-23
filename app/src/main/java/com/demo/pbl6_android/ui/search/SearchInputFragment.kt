package com.demo.pbl6_android.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.demo.pbl6_android.R
import com.demo.pbl6_android.databinding.FragmentSearchInputBinding

class SearchInputFragment : Fragment() {

    private var _binding: FragmentSearchInputBinding? = null
    private val binding: FragmentSearchInputBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        
        // Disable search temporarily
        binding.etSearch.isEnabled = false
        binding.btnSearch.isEnabled = false
        binding.btnSearch.alpha = 0.5f
        
        // Show message
        Toast.makeText(
            requireContext(),
            "Chức năng tìm kiếm tạm thời bị vô hiệu hóa",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun setupViews() {
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnSearch.setOnClickListener {
                showDisabledMessage()
            }

            etSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    showDisabledMessage()
                    true
                } else {
                    false
                }
            }
        }
    }
    
    private fun showDisabledMessage() {
        Toast.makeText(
            requireContext(),
            "Chức năng tìm kiếm đang được bảo trì",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun performSearch() {
        // Temporarily disabled
        showDisabledMessage()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

