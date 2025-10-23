package com.demo.pbl6_android.ui.voucher.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.demo.pbl6_android.R
import com.demo.pbl6_android.data.model.Voucher

class VoucherSelectableAdapter(
    private val vouchers: List<Voucher>,
    private var selectedVoucherId: String? = null,
    private val onVoucherSelected: (Voucher?) -> Unit
) : RecyclerView.Adapter<VoucherSelectableAdapter.VoucherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoucherViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_voucher_selectable, parent, false)
        return VoucherViewHolder(view)
    }

    override fun onBindViewHolder(holder: VoucherViewHolder, position: Int) {
        holder.bind(vouchers[position])
    }

    override fun getItemCount(): Int = vouchers.size

    fun updateSelectedVoucher(voucherId: String?) {
        val oldSelectedId = selectedVoucherId
        selectedVoucherId = voucherId
        
        // Notify changes for old and new selected items
        if (oldSelectedId != null) {
            val oldIndex = vouchers.indexOfFirst { it.id == oldSelectedId }
            if (oldIndex != -1) notifyItemChanged(oldIndex)
        }
        if (voucherId != null) {
            val newIndex = vouchers.indexOfFirst { it.id == voucherId }
            if (newIndex != -1) notifyItemChanged(newIndex)
        }
    }

    inner class VoucherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val layoutVoucherItem: View = itemView.findViewById(R.id.layout_voucher_item)
        private val rbVoucher: RadioButton = itemView.findViewById(R.id.rb_voucher)
        private val voucherBadge: View = itemView.findViewById(R.id.voucher_badge)
        private val tvVoucherTitle: TextView = itemView.findViewById(R.id.tv_voucher_title)
        private val tvVoucherCode: TextView = itemView.findViewById(R.id.tv_voucher_code)
        private val tvUsageInfo: TextView = itemView.findViewById(R.id.tv_usage_info)
        private val tvExpiryDate: TextView = itemView.findViewById(R.id.tv_expiry_date)
        private val btnDetails: TextView = itemView.findViewById(R.id.btn_details)
        private val disabledOverlay: View = itemView.findViewById(R.id.disabled_overlay)
        private val tvNotApplicable: TextView = itemView.findViewById(R.id.tv_not_applicable)

        fun bind(voucher: Voucher) {
            // Build title with max discount info if applicable
            val titleText = buildVoucherTitle(voucher)
            tvVoucherTitle.text = titleText
            tvVoucherCode.text = voucher.code
            tvExpiryDate.text = "HSD: ${voucher.expiryDate}"
            
            // Set badge color based on voucher type
            val isShippingVoucher = voucher.code.contains("SHIP", ignoreCase = true) || 
                                    voucher.title.contains("vận chuyển", ignoreCase = true) ||
                                    voucher.title.contains("ship", ignoreCase = true)
            
            voucherBadge.setBackgroundColor(
                if (isShippingVoucher) {
                    android.graphics.Color.parseColor("#4CAF50") // Green for shipping
                } else {
                    android.graphics.Color.parseColor("#9C27B0") // Purple for discount
                }
            )
            
            // Hide usage info for now (can be added later with real data)
            tvUsageInfo.visibility = View.GONE
            
            // Check if selected
            val isSelected = selectedVoucherId == voucher.id
            rbVoucher.isChecked = isSelected
            
            // Apply visual feedback for selected item
            if (isSelected) {
                layoutVoucherItem.setBackgroundResource(com.demo.pbl6_android.R.drawable.bg_voucher_selected)
            } else {
                layoutVoucherItem.setBackgroundResource(android.R.color.white)
            }
            
            // Handle item click
            itemView.setOnClickListener {
                if (isSelected) {
                    // Deselect if already selected
                    onVoucherSelected(null)
                } else {
                    // Select this voucher
                    onVoucherSelected(voucher)
                }
            }
            
            // Handle details button
            btnDetails.setOnClickListener {
                showVoucherDetails(voucher)
            }
            
            // Check if voucher is applicable (can be expanded with more logic)
            val isApplicable = true // TODO: Add logic to check if voucher is applicable
            if (!isApplicable) {
                disabledOverlay.visibility = View.VISIBLE
                tvNotApplicable.visibility = View.VISIBLE
                rbVoucher.isEnabled = false
            } else {
                disabledOverlay.visibility = View.GONE
                tvNotApplicable.visibility = View.GONE
                rbVoucher.isEnabled = true
            }
        }
        
        private fun buildVoucherTitle(voucher: Voucher): String {
            var title = voucher.title
            
            // Add max discount info if voucher has percentage discount and max discount
            if (voucher.discount.contains("%") && voucher.maxDiscount != null && voucher.maxDiscount > 0) {
                title += " (Giảm tối đa ${formatPrice(voucher.maxDiscount)})"
            }
            
            return title
        }
        
        private fun showVoucherDetails(voucher: Voucher) {
            // Show voucher details dialog
            val context = itemView.context
            val message = buildString {
                append("Mã: ${voucher.code}\n\n")
                append("Giảm giá: ${voucher.discount}\n\n")
                if (voucher.minAmount > 0) {
                    append("Đơn tối thiểu: ${formatPrice(voucher.minAmount)}\n\n")
                }
                if (voucher.maxDiscount != null) {
                    append("Giảm tối đa: ${formatPrice(voucher.maxDiscount)}\n\n")
                }
                append("Hạn sử dụng: ${voucher.expiryDate}")
            }
            
            android.app.AlertDialog.Builder(context)
                .setTitle("Điều kiện sử dụng")
                .setMessage(message)
                .setPositiveButton("Đóng", null)
                .show()
        }
        
        private fun formatPrice(price: Int): String {
            return "₫%,d".format(price).replace(",", ".")
        }
    }
}

