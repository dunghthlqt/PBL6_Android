package com.demo.pbl6_android.ui.shop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.demo.pbl6_android.databinding.ItemShopVoucherBinding
import com.demo.pbl6_android.ui.shop.model.ShopVoucher

class ShopVoucherAdapter(
    private val onVoucherClick: (ShopVoucher) -> Unit
) : ListAdapter<ShopVoucher, ShopVoucherAdapter.VoucherViewHolder>(VoucherDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoucherViewHolder {
        val binding = ItemShopVoucherBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VoucherViewHolder(binding, onVoucherClick)
    }

    override fun onBindViewHolder(holder: VoucherViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class VoucherViewHolder(
        private val binding: ItemShopVoucherBinding,
        private val onVoucherClick: (ShopVoucher) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(voucher: ShopVoucher) {
            binding.voucherDiscount.text = "Giảm ${voucher.discount}"
            binding.voucherMinAmount.text = "Đơn tối thiểu ${voucher.minAmount}"
            binding.voucherExpiry.text = "HSD: ${voucher.expiryDate}"
            
            if (voucher.quantity > 1) {
                binding.voucherQuantityBadge.text = "x${voucher.quantity}"
            } else {
                binding.voucherQuantityBadge.text = ""
            }
            
            binding.btnSaveVoucher.text = if (voucher.isSaved) "Đã lưu" else "Lưu"
            binding.btnSaveVoucher.isEnabled = !voucher.isSaved
            
            binding.btnSaveVoucher.setOnClickListener {
                onVoucherClick(voucher)
            }
        }
    }

    private class VoucherDiffCallback : DiffUtil.ItemCallback<ShopVoucher>() {
        override fun areItemsTheSame(oldItem: ShopVoucher, newItem: ShopVoucher): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShopVoucher, newItem: ShopVoucher): Boolean {
            return oldItem == newItem
        }
    }
}

