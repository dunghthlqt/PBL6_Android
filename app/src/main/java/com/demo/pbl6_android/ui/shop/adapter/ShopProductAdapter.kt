package com.demo.pbl6_android.ui.shop.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.demo.pbl6_android.data.model.Product
import com.demo.pbl6_android.databinding.ItemShopProductBinding
import com.demo.pbl6_android.utils.formatPrice

class ShopProductAdapter(
    private val onProductClick: (Product) -> Unit,
    private val onRemindClick: (Product) -> Unit
) : ListAdapter<Product, ShopProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemShopProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding, onProductClick, onRemindClick)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProductViewHolder(
        private val binding: ItemShopProductBinding,
        private val onProductClick: (Product) -> Unit,
        private val onRemindClick: (Product) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.originalPrice.apply {
                text = product.originalPrice.formatPrice()
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            
            binding.salePrice.text = product.currentPrice.formatPrice()
            
            // TODO: Load product image using Glide or Coil
            
            binding.root.setOnClickListener {
                onProductClick(product)
            }
            
            binding.btnRemind.setOnClickListener {
                onRemindClick(product)
            }
        }
    }

    private class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}

