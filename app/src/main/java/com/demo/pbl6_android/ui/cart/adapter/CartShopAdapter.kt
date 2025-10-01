package com.demo.pbl6_android.ui.cart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.pbl6_android.databinding.ItemCartShopBinding
import com.demo.pbl6_android.ui.cart.SwipeToDeleteCallback
import com.demo.pbl6_android.ui.cart.model.CartProduct
import com.demo.pbl6_android.ui.cart.model.CartShop

class CartShopAdapter(
    private val onShopCheckChanged: (CartShop, Boolean) -> Unit,
    private val onProductCheckChanged: (CartProduct, Boolean) -> Unit,
    private val onQuantityChanged: (CartProduct, Int) -> Unit,
    private val onDeleteProduct: (CartProduct) -> Unit,
    private val onViewShop: (CartShop) -> Unit
) : RecyclerView.Adapter<CartShopAdapter.ShopViewHolder>() {

    private val shops = mutableListOf<CartShop>()

    fun submitList(newShops: List<CartShop>) {
        shops.clear()
        shops.addAll(newShops)
        notifyDataSetChanged()
    }

    inner class ShopViewHolder(
        private val binding: ItemCartShopBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(shop: CartShop) {
            binding.apply {
                // Set shop data
                tvShopName.text = shop.name
                
                // Shop checkbox listener
                checkboxShop.setOnCheckedChangeListener { _, isChecked ->
                    shop.isSelected = isChecked
                    shop.products.forEach { it.isSelected = isChecked }
                    onShopCheckChanged(shop, isChecked)
                    // Notify product adapter to update checkboxes
                    rvShopItems.adapter?.notifyDataSetChanged()
                }
                
                // Set checkbox state AFTER setting listener to avoid triggering it
                checkboxShop.isChecked = shop.isSelected
                
                // View shop button
                tvViewShop.setOnClickListener {
                    onViewShop(shop)
                }
                
                // Setup products RecyclerView
                val productAdapter = CartProductAdapter(
                    products = shop.products,
                    onProductCheckChanged = { product, isChecked ->
                        // Update shop checkbox based on products
                        updateShopCheckbox(shop)
                        onProductCheckChanged(product, isChecked)
                    },
                    onQuantityChanged = onQuantityChanged,
                    onDeleteProduct = onDeleteProduct
                )
                
                rvShopItems.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = productAdapter
                    
                    // Add swipe to delete
                    val swipeHandler = object : SwipeToDeleteCallback(context) {
                        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                            val position = viewHolder.adapterPosition
                            val product = shop.products[position]
                            onDeleteProduct(product)
                        }
                    }
                    val itemTouchHelper = ItemTouchHelper(swipeHandler)
                    itemTouchHelper.attachToRecyclerView(this)
                }
            }
        }

        private fun updateShopCheckbox(shop: CartShop) {
            val allSelected = shop.products.all { it.isSelected }
            
            shop.isSelected = allSelected
            
            // Temporarily remove listener to prevent triggering the callback
            binding.checkboxShop.setOnCheckedChangeListener(null)
            binding.checkboxShop.isChecked = allSelected
            
            // Re-attach listener
            binding.checkboxShop.setOnCheckedChangeListener { _, isChecked ->
                shop.isSelected = isChecked
                shop.products.forEach { it.isSelected = isChecked }
                onShopCheckChanged(shop, isChecked)
                // Notify product adapter to update checkboxes
                binding.rvShopItems.adapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val binding = ItemCartShopBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShopViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        holder.bind(shops[position])
    }

    override fun getItemCount(): Int = shops.size
}
