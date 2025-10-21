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

    private var selectedShopVoucher: com.demo.pbl6_android.data.model.Voucher? = null

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
                updateVoucherDisplay()
            }
        }
        
        private fun updateVoucherDisplay() {
            binding.apply {
                if (selectedShopVoucher != null) {
                    // Calculate product total for this shop
                    var productTotal = 0
                    shops.forEach { shop ->
                        shop.products.forEach { product ->
                            productTotal += product.currentPrice * product.quantity
                        }
                    }
                    
                    val discount = com.demo.pbl6_android.data.VoucherManager.calculateDiscount(
                        productTotal,
                        selectedShopVoucher
                    )
                    tvSelectShopVoucher.text = "-${formatPrice(discount)}"
                    tvSelectShopVoucher.setTextColor(android.graphics.Color.parseColor("#F59E0B"))
                } else {
                    tvSelectShopVoucher.text = "Chọn mã"
                    tvSelectShopVoucher.setTextColor(android.graphics.Color.parseColor("#4318D1"))
                }
            }
        }
        
        private fun formatPrice(price: Int): String {
            return "%,dđ".format(price).replace(",", ".")
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
    
    fun updateShopVoucher(voucher: com.demo.pbl6_android.data.model.Voucher?) {
        selectedShopVoucher = voucher
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

