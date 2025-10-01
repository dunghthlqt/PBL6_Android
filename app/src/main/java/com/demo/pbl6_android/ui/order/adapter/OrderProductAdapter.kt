package com.demo.pbl6_android.ui.order.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.pbl6_android.databinding.ItemOrderProductBinding
import com.demo.pbl6_android.ui.order.model.OrderProduct

class OrderProductAdapter(
    private val products: List<OrderProduct>
) : RecyclerView.Adapter<OrderProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(
        private val binding: ItemOrderProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: OrderProduct) {
            binding.apply {
                tvProductName.text = product.name
                tvColorValue.text = product.color
                tvSizeValue.text = product.size
                tvCurrentPrice.text = formatPrice(product.currentPrice)
                tvOriginalPrice.text = formatPrice(product.originalPrice)
                tvOriginalPrice.paintFlags = tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                tvQuantity.text = product.quantity.toString()
            }
        }

        private fun formatPrice(price: Int): String {
            return "%,dđ".format(price).replace(",", ".")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemOrderProductBinding.inflate(
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

