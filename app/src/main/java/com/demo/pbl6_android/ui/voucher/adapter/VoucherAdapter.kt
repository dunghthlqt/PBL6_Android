package com.demo.pbl6_android.ui.voucher.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.demo.pbl6_android.R
import com.demo.pbl6_android.data.model.Voucher
import com.google.android.material.button.MaterialButton

class VoucherAdapter(
    private val vouchers: List<Voucher>,
    private val selectedVoucher: Voucher? = null,
    private val onVoucherSelected: (Voucher) -> Unit
) : RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoucherViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_voucher, parent, false)
        return VoucherViewHolder(view)
    }

    override fun onBindViewHolder(holder: VoucherViewHolder, position: Int) {
        holder.bind(vouchers[position])
    }

    override fun getItemCount(): Int = vouchers.size

    inner class VoucherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvVoucherTitle: TextView = itemView.findViewById(R.id.tv_voucher_title)
        private val tvVoucherCode: TextView = itemView.findViewById(R.id.tv_voucher_code)
        private val tvMinAmount: TextView = itemView.findViewById(R.id.tv_min_amount)
        private val tvExpiryDate: TextView = itemView.findViewById(R.id.tv_expiry_date)
        private val btnUseVoucher: MaterialButton = itemView.findViewById(R.id.btn_use_voucher)

        fun bind(voucher: Voucher) {
            tvVoucherTitle.text = voucher.title
            tvVoucherCode.text = voucher.code
            tvMinAmount.text = if (voucher.minAmount > 0) "Đơn tối thiểu ${formatPrice(voucher.minAmount)}" else "Không có đơn tối thiểu"
            tvExpiryDate.text = "HSD: ${voucher.expiryDate}"

            // Set color based on voucher type (shipping = green, discount = purple)
            val isShipping = voucher.code.contains("SHIP", ignoreCase = true) || 
                             voucher.title.contains("ship", ignoreCase = true) || 
                             voucher.title.contains("vận chuyển", ignoreCase = true)
            
            val color = if (isShipping) {
                android.graphics.Color.parseColor("#4CAF50") // Green for shipping
            } else {
                android.graphics.Color.parseColor("#9C27B0") // Purple for discount
            }
            
            tvVoucherCode.setTextColor(color)

            // Check if this voucher is currently selected
            val isSelected = selectedVoucher?.id == voucher.id
            if (isSelected) {
                btnUseVoucher.text = "Đang sử dụng"
                btnUseVoucher.isEnabled = false
                btnUseVoucher.alpha = 0.6f
            } else {
                btnUseVoucher.text = "Sử dụng"
                btnUseVoucher.isEnabled = true
                btnUseVoucher.alpha = 1.0f
            }

            btnUseVoucher.setOnClickListener {
                onVoucherSelected(voucher)
            }
        }

        private fun formatPrice(price: Int): String {
            return "₫%,d".format(price).replace(",", ".")
        }
    }
}

