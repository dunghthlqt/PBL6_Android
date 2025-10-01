package com.demo.pbl6_android.ui.order.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.pbl6_android.databinding.ItemOrderShopBinding
import com.demo.pbl6_android.ui.order.model.OrderShop

class OrderShopAdapter(
    private val shops: List<OrderShop>,
    private val onSelectShopVoucher: (OrderShop) -> Unit,
    private val onAddShopNote: (OrderShop) -> Unit
) : RecyclerView.Adapter<OrderShopAdapter.ShopViewHolder>() {

    inner class ShopViewHolder(
        private val binding: ItemOrderShopBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(shop: OrderShop) {
            binding.apply {
                tvShopName.text = shop.shopName

                val productAdapter = OrderProductAdapter(shop.products)
                rvOrderProducts.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = productAdapter
                }

                layoutShopVoucher.setOnClickListener {
                    onSelectShopVoucher(shop)
                }

                layoutShopNote.setOnClickListener {
                    onAddShopNote(shop)
                }

                updateNoteDisplay(shop)
            }
        }

        private fun updateNoteDisplay(shop: OrderShop) {
            binding.apply {
                if (shop.noteToShop.isNullOrEmpty()) {
                    tvAddShopNote.visibility = View.VISIBLE
                    ivNoteArrow.visibility = View.GONE
                    tvNotePreview.visibility = View.GONE
                } else {
                    tvAddShopNote.visibility = View.GONE
                    ivNoteArrow.visibility = View.VISIBLE
                    tvNotePreview.visibility = View.VISIBLE
                    tvNotePreview.text = shop.noteToShop
                }
            }
        }
    }

    fun updateShopNote(shopId: String, note: String) {
        shops.find { it.shopId == shopId }?.noteToShop = note
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val binding = ItemOrderShopBinding.inflate(
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

