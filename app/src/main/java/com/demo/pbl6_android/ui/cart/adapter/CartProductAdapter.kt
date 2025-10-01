package com.demo.pbl6_android.ui.cart.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.demo.pbl6_android.R
import com.demo.pbl6_android.databinding.ItemCartProductBinding
import com.demo.pbl6_android.ui.cart.model.CartProduct

class CartProductAdapter(
    private val products: List<CartProduct>,
    private val onProductCheckChanged: (CartProduct, Boolean) -> Unit,
    private val onQuantityChanged: (CartProduct, Int) -> Unit,
    private val onDeleteProduct: (CartProduct) -> Unit
) : RecyclerView.Adapter<CartProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(
        private val binding: ItemCartProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: CartProduct) {
            binding.apply {
                // Set product data
                checkboxProduct.isChecked = product.isSelected
                tvProductName.text = product.name
                tvColorValue.text = product.color
                tvSizeValue.text = product.size
                tvCurrentPrice.text = formatPrice(product.currentPrice)
                tvOriginalPrice.text = formatPrice(product.originalPrice)
                tvOriginalPrice.paintFlags = tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                tvQuantity.text = product.quantity.toString()
                
                // Apply selected background
                val backgroundColor = if (product.isSelected) {
                    ContextCompat.getColor(root.context, R.color.product_selected_bg)
                } else {
                    ContextCompat.getColor(root.context, android.R.color.white)
                }
                productCard.setCardBackgroundColor(backgroundColor)
                
                // Checkbox listener
                checkboxProduct.setOnCheckedChangeListener { _, isChecked ->
                    product.isSelected = isChecked
                    onProductCheckChanged(product, isChecked)
                }
                
                // Quantity controls
                btnDecrease.setOnClickListener {
                    if (product.quantity > 1) {
                        product.quantity--
                        tvQuantity.text = product.quantity.toString()
                        onQuantityChanged(product, product.quantity)
                    }
                }
                
                btnIncrease.setOnClickListener {
                    product.quantity++
                    tvQuantity.text = product.quantity.toString()
                    onQuantityChanged(product, product.quantity)
                }
            }
        }

        private fun formatPrice(price: Int): String {
            return "%,dđ".format(price).replace(",", ".")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemCartProductBinding.inflate(
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
