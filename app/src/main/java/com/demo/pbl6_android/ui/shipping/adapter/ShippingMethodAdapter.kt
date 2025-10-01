package com.demo.pbl6_android.ui.shipping.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.demo.pbl6_android.R
import com.demo.pbl6_android.databinding.ItemShippingMethodBinding
import com.demo.pbl6_android.ui.order.model.ShippingMethod

class ShippingMethodAdapter(
    private val methods: List<ShippingMethod>,
    private val onMethodSelected: (ShippingMethod) -> Unit
) : RecyclerView.Adapter<ShippingMethodAdapter.MethodViewHolder>() {

    private var selectedPosition = methods.indexOfFirst { it.isSelected }

    inner class MethodViewHolder(
        private val binding: ItemShippingMethodBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(method: ShippingMethod, isSelected: Boolean) {
            binding.apply {
                tvShippingName.text = method.name
                tvShippingDays.text = method.estimatedDays
                tvShippingPrice.text = formatPrice(method.price)
                
                rbShippingMethod.isChecked = isSelected
                
                val backgroundColor = if (isSelected) {
                    ContextCompat.getColor(root.context, R.color.product_selected_bg)
                } else {
                    ContextCompat.getColor(root.context, android.R.color.white)
                }
                layoutShippingMethod.setBackgroundColor(backgroundColor)
                
                root.setOnClickListener {
                    selectMethod(adapterPosition)
                    onMethodSelected(method)
                }
                
                rbShippingMethod.setOnClickListener {
                    selectMethod(adapterPosition)
                    onMethodSelected(method)
                }
            }
        }

        private fun formatPrice(price: Int): String {
            return "%,dđ".format(price).replace(",", ".")
        }
    }

    private fun selectMethod(position: Int) {
        val previousPosition = selectedPosition
        selectedPosition = position
        
        methods.forEach { it.isSelected = false }
        methods[position].isSelected = true
        
        notifyItemChanged(previousPosition)
        notifyItemChanged(selectedPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MethodViewHolder {
        val binding = ItemShippingMethodBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MethodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MethodViewHolder, position: Int) {
        holder.bind(methods[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = methods.size
}

