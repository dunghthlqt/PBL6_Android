package com.demo.pbl6_android.ui.landing.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.demo.pbl6_android.R
import com.demo.pbl6_android.data.model.Product
import com.demo.pbl6_android.databinding.ItemProductCardBinding

class ProductAdapter(
    private val products: List<Product>,
    private val onProductClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(
        private val binding: ItemProductCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.apply {
                tvProductName.text = product.name
                tvRatingValue.text = product.rating.toString()
                tvCurrentPrice.text = formatPrice(product.currentPrice)
                tvOriginalPrice.text = formatPrice(product.originalPrice)
                tvOriginalPrice.paintFlags = tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                
                // Load product image with Glide
                // Use fitCenter to maintain aspect ratio with fixed width
                val imageUrl = product.images.firstOrNull() ?: ""
                Glide.with(ivProductImage.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_foreground) // Temporary placeholder
                    .error(R.drawable.ic_launcher_foreground) // Error placeholder
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache images
                    .fitCenter() // Maintain aspect ratio, show full image
                    .into(ivProductImage)
                
                root.setOnClickListener {
                    onProductClick(product)
                }
            }
        }

        private fun formatPrice(price: Int): String {
            return "₫%,d".format(price).replace(",", ".")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size
}

