package com.demo.pbl6_android.ui.seller.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.demo.pbl6_android.databinding.ItemSellerProductBinding
import com.demo.pbl6_android.ui.seller.model.SellerProduct
import com.demo.pbl6_android.utils.formatCurrency

class SellerProductAdapter(
    private val onEditClick: (SellerProduct) -> Unit,
    private val onHideClick: (SellerProduct) -> Unit,
    private val onMenuClick: (SellerProduct) -> Unit
) : ListAdapter<SellerProduct, SellerProductAdapter.SellerProductViewHolder>(SellerProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerProductViewHolder {
        val binding = ItemSellerProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SellerProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SellerProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SellerProductViewHolder(
        private val binding: ItemSellerProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: SellerProduct) {
            binding.apply {
                tvProductName.text = product.name
                tvProductPrice.text = product.price.formatCurrency()
                tvStockCount.text = product.stockQuantity.toString()
                tvSoldCount.text = product.soldCount.toString()
                tvLikeCount.text = product.likeCount.toString()
                tvViewCount.text = product.viewCount.toString()
                
                // TODO: Load product image using Glide or similar
                // Glide.with(ivProductImage.context)
                //     .load(product.imageUrl)
                //     .into(ivProductImage)
                
                btnEdit.setOnClickListener {
                    onEditClick(product)
                }
                
                btnHide.setOnClickListener {
                    onHideClick(product)
                }
                
                btnMenu.setOnClickListener {
                    onMenuClick(product)
                }
            }
        }
    }

    private class SellerProductDiffCallback : DiffUtil.ItemCallback<SellerProduct>() {
        override fun areItemsTheSame(oldItem: SellerProduct, newItem: SellerProduct): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SellerProduct, newItem: SellerProduct): Boolean {
            return oldItem == newItem
        }
    }
}

