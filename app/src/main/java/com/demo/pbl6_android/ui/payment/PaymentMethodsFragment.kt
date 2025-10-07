package com.demo.pbl6_android.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.pbl6_android.data.model.CardType
import com.demo.pbl6_android.data.model.PaymentMethod
import com.demo.pbl6_android.databinding.FragmentPaymentMethodsBinding
import com.demo.pbl6_android.ui.payment.adapter.PaymentMethodAdapter

class PaymentMethodsFragment : Fragment() {

    private var _binding: FragmentPaymentMethodsBinding? = null
    private val binding: FragmentPaymentMethodsBinding
        get() = _binding!!

    private lateinit var paymentMethodAdapter: PaymentMethodAdapter
    private val paymentMethods = mutableListOf<PaymentMethod>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentMethodsBinding.inflate(inflater, container, false)
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
            
            btnAddCard.setOnClickListener {
                showToast("Chức năng thêm thẻ đang phát triển")
            }
        }
    }

    private fun setupRecyclerView() {
        paymentMethodAdapter = PaymentMethodAdapter(
            paymentMethods = paymentMethods,
            onSetDefaultClick = { paymentMethod ->
                setDefaultPaymentMethod(paymentMethod)
            },
            onEditClick = { paymentMethod ->
                showToast("Chức năng chỉnh sửa thẻ đang phát triển")
            },
            onDeleteClick = { paymentMethod ->
                deletePaymentMethod(paymentMethod)
            }
        )

        binding.rvPaymentMethods.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = paymentMethodAdapter
        }
    }

    private fun loadSampleData() {
        paymentMethods.clear()
        paymentMethods.addAll(
            listOf(
                PaymentMethod(
                    id = "card_1",
                    cardType = CardType.VISA,
                    cardNumber = "4532123456789012",
                    cardHolderName = "NGUYEN VAN AN",
                    expiryDate = "12/25",
                    isDefault = true
                ),
                PaymentMethod(
                    id = "card_2",
                    cardType = CardType.MASTERCARD,
                    cardNumber = "5412345678901234",
                    cardHolderName = "TRAN THI BINH",
                    expiryDate = "08/26",
                    isDefault = false
                ),
                PaymentMethod(
                    id = "card_3",
                    cardType = CardType.JCB,
                    cardNumber = "3528123456789012",
                    cardHolderName = "LE VAN CUONG",
                    expiryDate = "03/27",
                    isDefault = false
                )
            )
        )

        paymentMethodAdapter.notifyDataSetChanged()
        updateEmptyState()
    }

    private fun setDefaultPaymentMethod(paymentMethod: PaymentMethod) {
        paymentMethods.forEachIndexed { index, method ->
            paymentMethods[index] = method.copy(isDefault = method.id == paymentMethod.id)
        }
        paymentMethodAdapter.notifyDataSetChanged()
        showToast("Đã đặt làm phương thức mặc định")
    }

    private fun deletePaymentMethod(paymentMethod: PaymentMethod) {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Xóa thẻ")
            .setMessage("Bạn có chắc chắn muốn xóa thẻ này không?")
            .setPositiveButton("Xóa") { _, _ ->
                paymentMethods.remove(paymentMethod)
                paymentMethodAdapter.notifyDataSetChanged()
                updateEmptyState()
                showToast("Đã xóa thẻ")
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun updateEmptyState() {
        binding.apply {
            if (paymentMethods.isEmpty()) {
                emptyState.visibility = View.VISIBLE
                rvPaymentMethods.visibility = View.GONE
            } else {
                emptyState.visibility = View.GONE
                rvPaymentMethods.visibility = View.VISIBLE
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

