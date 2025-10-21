package com.demo.pbl6_android.ui.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.pbl6_android.R
import com.demo.pbl6_android.data.AddressManager
import com.demo.pbl6_android.data.model.Address
import com.demo.pbl6_android.databinding.FragmentAddressSelectionBinding
import com.demo.pbl6_android.ui.address.adapter.AddressAdapter
import kotlinx.coroutines.launch

class AddressSelectionFragment : Fragment() {

    private var _binding: FragmentAddressSelectionBinding? = null
    private val binding: FragmentAddressSelectionBinding
        get() = _binding!!

    private lateinit var addressAdapter: AddressAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeAddresses()
    }

    private fun setupViews() {
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnAddAddress.setOnClickListener {
                navigateToAddAddress()
            }
        }
    }

    private fun observeAddresses() {
        viewLifecycleOwner.lifecycleScope.launch {
            AddressManager.addresses.collect { addresses ->
                displayAddresses(addresses)
            }
        }
    }

    private fun displayAddresses(addresses: List<Address>) {
        addressAdapter = AddressAdapter(
            addresses = addresses,
            onAddressSelected = { address ->
                AddressManager.selectAddress(address)
                findNavController().navigateUp()
            },
            onEditAddress = { address ->
                navigateToEditAddress(address)
            }
        )

        binding.rvAddresses.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = addressAdapter
        }
    }

    private fun navigateToAddAddress() {
        findNavController().navigate(R.id.action_addressSelectionFragment_to_addressFormFragment)
    }

    private fun navigateToEditAddress(address: Address) {
        val bundle = Bundle().apply {
            putString("addressId", address.id)
        }
        findNavController().navigate(
            R.id.action_addressSelectionFragment_to_addressFormFragment,
            bundle
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

