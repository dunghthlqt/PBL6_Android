package com.demo.pbl6_android.ui.order.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.pbl6_android.data.model.OrderItem
import com.demo.pbl6_android.databinding.ItemOrderDetailProductBinding

class OrderDetailProductAdapter(
    private val items: List<OrderItem>
) : RecyclerView.Adapter<OrderDetailProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(
        private val binding: ItemOrderDetailProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OrderItem) {
            binding.apply {
                tvProductName.text = item.productName
                tvProductVariant.text = "Màu: ${item.color} | Size: ${item.size}"
                tvProductPrice.text = formatPrice(item.price)
                tvQuantity.text = "x${item.quantity}"
                
                // TODO: Load image with Glide or Coil
                // Glide.with(ivProductImage).load(item.productImage).into(ivProductImage)
            }
        }

        private fun formatPrice(price: Int): String {
            return "%,dđ".format(price).replace(",", ".")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemOrderDetailProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

