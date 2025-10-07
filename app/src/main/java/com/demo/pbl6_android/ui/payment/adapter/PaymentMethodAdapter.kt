package com.demo.pbl6_android.ui.payment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.pbl6_android.R
import com.demo.pbl6_android.data.model.CardType
import com.demo.pbl6_android.data.model.PaymentMethod
import com.demo.pbl6_android.databinding.ItemPaymentMethodBinding

class PaymentMethodAdapter(
    private val paymentMethods: List<PaymentMethod>,
    private val onSetDefaultClick: (PaymentMethod) -> Unit,
    private val onEditClick: (PaymentMethod) -> Unit,
    private val onDeleteClick: (PaymentMethod) -> Unit
) : RecyclerView.Adapter<PaymentMethodAdapter.PaymentMethodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodViewHolder {
        val binding = ItemPaymentMethodBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PaymentMethodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentMethodViewHolder, position: Int) {
        holder.bind(paymentMethods[position])
    }

    override fun getItemCount(): Int = paymentMethods.size

    inner class PaymentMethodViewHolder(
        private val binding: ItemPaymentMethodBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(paymentMethod: PaymentMethod) {
            binding.apply {
                // Show/hide default badge
                tvDefaultBadge.visibility = if (paymentMethod.isDefault) View.VISIBLE else View.GONE
                
                // Set card icon based on type
                val cardIconRes = when (paymentMethod.cardType) {
                    CardType.VISA -> R.drawable.ic_account // TODO: Replace with actual Visa icon
                    CardType.MASTERCARD -> R.drawable.ic_account // TODO: Replace with actual Mastercard icon
                    CardType.JCB -> R.drawable.ic_account // TODO: Replace with actual JCB icon
                    CardType.MOMO -> R.drawable.ic_account // TODO: Replace with actual MoMo icon
                    CardType.ZALOPAY -> R.drawable.ic_account // TODO: Replace with actual ZaloPay icon
                    CardType.CASH_ON_DELIVERY -> R.drawable.ic_account
                }
                ivCardIcon.setImageResource(cardIconRes)
                
                // Set card type
                tvCardType.text = paymentMethod.cardType.displayName
                
                // Set masked card number
                tvCardNumber.text = paymentMethod.maskedCardNumber
                
                // Set card holder name
                tvCardHolder.text = paymentMethod.cardHolderName
                
                // Set expiry date
                tvExpiryDate.text = paymentMethod.expiryDate
                
                // Show/hide set default button
                btnSetDefault.visibility = if (!paymentMethod.isDefault) View.VISIBLE else View.GONE
                
                // Set click listeners
                btnSetDefault.setOnClickListener {
                    onSetDefaultClick(paymentMethod)
                }
                
                btnEdit.setOnClickListener {
                    onEditClick(paymentMethod)
                }
                
                btnDelete.setOnClickListener {
                    onDeleteClick(paymentMethod)
                }
            }
        }
    }
}

