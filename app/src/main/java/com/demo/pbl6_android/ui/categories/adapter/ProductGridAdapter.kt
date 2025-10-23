package com.demo.pbl6_android.ui.categories.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.demo.pbl6_android.R
import com.demo.pbl6_android.data.model.Product
import com.demo.pbl6_android.databinding.ItemProductGridBinding

class ProductGridAdapter(
    private val products: List<Product>,
    private val onProductClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductGridAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductGridBinding.inflate(
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

    inner class ProductViewHolder(
        private val binding: ItemProductGridBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.apply {
                // Load product image with Glide
                // Use fitCenter to maintain aspect ratio with fixed width
                val imageUrl = product.images.firstOrNull() ?: ""
                Glide.with(ivProductImage.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter() // Maintain aspect ratio, show full image
                    .into(ivProductImage)

                tvProductName.text = product.name
                tvRating.text = product.rating.toString()
                tvSoldCount.text = product.soldCount.toString()
                tvCurrentPrice.text = formatPrice(product.currentPrice)

                if (product.discount > 0) {
                    tvOriginalPrice.apply {
                        visibility = View.VISIBLE
                        text = formatPrice(product.originalPrice)
                        paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    }

                    badgeDiscount.visibility = View.VISIBLE
                    tvDiscount.text = "-${product.discount}%"
                } else {
                    tvOriginalPrice.visibility = View.GONE
                    badgeDiscount.visibility = View.GONE
                }

                root.setOnClickListener {
                    onProductClick(product)
                }
            }
        }

        private fun formatPrice(price: Int): String {
            return "%,dđ".format(price).replace(",", ".")
        }
    }
}

