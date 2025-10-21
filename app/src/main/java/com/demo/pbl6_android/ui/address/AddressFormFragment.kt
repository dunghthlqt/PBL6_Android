package com.demo.pbl6_android.ui.address

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.demo.pbl6_android.R
import com.demo.pbl6_android.data.AddressManager
import com.demo.pbl6_android.data.VietnamAddressData
import com.demo.pbl6_android.data.model.Address
import com.demo.pbl6_android.databinding.FragmentAddressFormBinding
import com.demo.pbl6_android.ui.common.CustomToast
import java.util.UUID

class AddressFormFragment : Fragment() {

    private var _binding: FragmentAddressFormBinding? = null
    private val binding: FragmentAddressFormBinding
        get() = _binding!!

    private var addressId: String? = null
    private var selectedProvince: String? = null
    private var selectedDistrict: String? = null
    private var selectedWard: String? = null
    private var hasChanges: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupAddressPickers()
        loadAddressIfEditing()
        setupBackPressedHandler()
        setupTextChangeListeners()
    }

    private fun setupViews() {
        binding.apply {
            btnBack.setOnClickListener {
                handleBackPressed()
            }

            btnComplete.setOnClickListener {
                saveAddress()
            }
        }
    }

    private fun setupTextChangeListeners() {
        binding.apply {
            etName.addTextChangedListener(object : android.text.TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    hasChanges = true
                }
                override fun afterTextChanged(s: android.text.Editable?) {}
            })

            etPhone.addTextChangedListener(object : android.text.TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    hasChanges = true
                }
                override fun afterTextChanged(s: android.text.Editable?) {}
            })

            etStreet.addTextChangedListener(object : android.text.TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    hasChanges = true
                }
                override fun afterTextChanged(s: android.text.Editable?) {}
            })
        }
    }

    private fun setupAddressPickers() {
        VietnamAddressData.initialize(requireContext())
        setupProvincePicker()
    }

    private fun setupProvincePicker() {
        val provinces = VietnamAddressData.getProvinces()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, provinces)

        binding.actvProvince.apply {
            setAdapter(adapter)
            setOnItemClickListener { _, _, position, _ ->
                selectedProvince = provinces[position]
                selectedDistrict = null
                selectedWard = null
                hasChanges = true
                setupDistrictPicker(selectedProvince!!)
                binding.actvDistrict.text = null
                binding.actvWard.text = null
                binding.tilWard.visibility = View.GONE
            }
        }
    }

    private fun setupDistrictPicker(provinceName: String) {
        val districts = VietnamAddressData.getDistricts(provinceName)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, districts)

        binding.tilDistrict.visibility = View.VISIBLE
        binding.actvDistrict.apply {
            setAdapter(adapter)
            setOnItemClickListener { _, _, position, _ ->
                selectedDistrict = districts[position]
                selectedWard = null
                hasChanges = true
                setupWardPicker(provinceName, selectedDistrict!!)
                binding.actvWard.text = null
            }
        }
    }

    private fun setupWardPicker(provinceName: String, districtName: String) {
        val wards = VietnamAddressData.getWards(provinceName, districtName)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, wards)

        binding.tilWard.visibility = View.VISIBLE
        binding.actvWard.apply {
            setAdapter(adapter)
            setOnItemClickListener { _, _, position, _ ->
                selectedWard = wards[position]
                hasChanges = true
            }
        }
    }

    private fun loadAddressIfEditing() {
        addressId = arguments?.getString("addressId")
        addressId?.let { id ->
            binding.tvTitle.text = "Sửa địa chỉ"
            val address = AddressManager.addresses.value.find { it.id == id }
            address?.let {
                binding.etName.setText(it.recipientName)
                binding.etPhone.setText(it.phoneNumber)
                binding.etStreet.setText(it.street)

                selectedProvince = it.province
                selectedDistrict = it.district
                selectedWard = it.ward

                binding.actvProvince.setText(it.province, false)
                setupDistrictPicker(it.province)
                binding.actvDistrict.setText(it.district, false)
                setupWardPicker(it.province, it.district)
                binding.actvWard.setText(it.ward, false)
            }
        }
    }

    private fun setupBackPressedHandler() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun handleBackPressed() {
        if (hasChanges) {
            showDiscardChangesDialog()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun showDiscardChangesDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_confirm, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val tvTitle = dialogView.findViewById<android.widget.TextView>(R.id.tv_title)
        val tvMessage = dialogView.findViewById<android.widget.TextView>(R.id.tv_message)
        val btnCancel = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_cancel)
        val btnConfirm = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_confirm)

        tvTitle.text = "Cập nhật chưa được lưu"
        tvMessage.text = "Bạn có muốn hủy bỏ không?"
        btnCancel.text = "Thoát"
        btnConfirm.text = "Hủy thay đổi"

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            dialog.dismiss()
            findNavController().navigateUp()
        }

        dialog.show()
    }

    private fun saveAddress() {
        if (!validateInputs()) {
            return
        }

        val name = binding.etName.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val street = binding.etStreet.text.toString().trim()

        val address = Address(
            id = addressId ?: "addr_${UUID.randomUUID()}",
            recipientName = name,
            phoneNumber = phone,
            province = selectedProvince!!,
            district = selectedDistrict!!,
            ward = selectedWard!!,
            street = street,
            isDefault = false
        )

        if (addressId != null) {
            AddressManager.updateAddress(address)
            CustomToast.show(requireContext(), "Đã cập nhật địa chỉ")
        } else {
            AddressManager.addAddress(address)
            CustomToast.show(requireContext(), "Đã thêm địa chỉ mới")
        }

        hasChanges = false
        findNavController().navigateUp()
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        if (binding.etName.text.isNullOrBlank()) {
            binding.tilName.error = "Vui lòng nhập họ và tên"
            isValid = false
        } else {
            binding.tilName.error = null
        }

        val phone = binding.etPhone.text.toString().trim()
        if (phone.isBlank()) {
            binding.tilPhone.error = "Vui lòng nhập số điện thoại"
            isValid = false
        } else if (phone.length != 10) {
            binding.tilPhone.error = "Số điện thoại phải có 10 chữ số"
            isValid = false
        } else {
            binding.tilPhone.error = null
        }

        if (selectedProvince == null) {
            binding.tilProvince.error = "Vui lòng chọn tỉnh/thành phố"
            isValid = false
        } else {
            binding.tilProvince.error = null
        }

        if (selectedDistrict == null) {
            binding.tilDistrict.error = "Vui lòng chọn quận/huyện"
            isValid = false
        } else {
            binding.tilDistrict.error = null
        }

        if (selectedWard == null) {
            binding.tilWard.error = "Vui lòng chọn phường/xã"
            isValid = false
        } else {
            binding.tilWard.error = null
        }

        if (binding.etStreet.text.isNullOrBlank()) {
            binding.tilStreet.error = "Vui lòng nhập địa chỉ cụ thể"
            isValid = false
        } else {
            binding.tilStreet.error = null
        }

        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

