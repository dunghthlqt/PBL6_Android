package com.demo.pbl6_android.ui.categories.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
                // TODO: Load image using Glide or similar
                // Glide.with(root.context).load(product.images.firstOrNull()).into(ivProductImage)

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

