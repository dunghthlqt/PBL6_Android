package com.demo.pbl6_android.ui.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.pbl6_android.data.model.Address
import com.demo.pbl6_android.databinding.FragmentShippingAddressBinding
import com.demo.pbl6_android.ui.address.adapter.AddressAdapter

class ShippingAddressFragment : Fragment() {

    private var _binding: FragmentShippingAddressBinding? = null
    private val binding: FragmentShippingAddressBinding
        get() = _binding!!

    private lateinit var addressAdapter: AddressAdapter
    private val addresses = mutableListOf<Address>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShippingAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupViews()
        setupRecyclerView()
        loadSampleData()
    }

    private fun setupViews() {
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
            
            btnAddAddress.setOnClickListener {
                showToast("Chức năng thêm địa chỉ đang phát triển")
            }
        }
    }

    private fun setupRecyclerView() {
        addressAdapter = AddressAdapter(
            addresses = addresses,
            onSetDefaultClick = { address ->
                setDefaultAddress(address)
            },
            onEditClick = { address ->
                showToast("Chức năng chỉnh sửa địa chỉ đang phát triển")
            },
            onDeleteClick = { address ->
                deleteAddress(address)
            }
        )

        binding.rvAddresses.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = addressAdapter
        }
    }

    private fun loadSampleData() {
        addresses.clear()
        addresses.addAll(
            listOf(
                Address(
                    id = "addr_1",
                    recipientName = "Nguyễn Văn An",
                    phoneNumber = "0123456789",
                    province = "TP. Hồ Chí Minh",
                    district = "Quận 1",
                    ward = "Phường Bến Nghé",
                    detailAddress = "123 Đường Lê Lợi",
                    isDefault = true
                ),
                Address(
                    id = "addr_2",
                    recipientName = "Trần Thị Bình",
                    phoneNumber = "0987654321",
                    province = "TP. Hồ Chí Minh",
                    district = "Quận 3",
                    ward = "Phường 7",
                    detailAddress = "456 Đường Cách Mạng Tháng 8",
                    isDefault = false
                ),
                Address(
                    id = "addr_3",
                    recipientName = "Lê Văn Cường",
                    phoneNumber = "0765432198",
                    province = "Hà Nội",
                    district = "Quận Hoàn Kiếm",
                    ward = "Phường Hàng Bạc",
                    detailAddress = "789 Phố Hàng Gai",
                    isDefault = false
                )
            )
        )

        addressAdapter.notifyDataSetChanged()
        updateEmptyState()
    }

    private fun setDefaultAddress(address: Address) {
        addresses.forEachIndexed { index, addr ->
            addresses[index] = addr.copy(isDefault = addr.id == address.id)
        }
        addressAdapter.notifyDataSetChanged()
        showToast("Đã đặt làm địa chỉ mặc định")
    }

    private fun deleteAddress(address: Address) {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Xóa địa chỉ")
            .setMessage("Bạn có chắc chắn muốn xóa địa chỉ này không?")
            .setPositiveButton("Xóa") { _, _ ->
                addresses.remove(address)
                addressAdapter.notifyDataSetChanged()
                updateEmptyState()
                showToast("Đã xóa địa chỉ")
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun updateEmptyState() {
        binding.apply {
            if (addresses.isEmpty()) {
                emptyState.visibility = View.VISIBLE
                rvAddresses.visibility = View.GONE
            } else {
                emptyState.visibility = View.GONE
                rvAddresses.visibility = View.VISIBLE
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

