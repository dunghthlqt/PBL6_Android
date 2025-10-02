package com.demo.pbl6_android.ui.product.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.demo.pbl6_android.R
import com.demo.pbl6_android.databinding.ItemSizeOptionBinding

class ProductSizeAdapter(
    private val sizes: List<String>,
    private val onSizeSelected: (String) -> Unit
) : RecyclerView.Adapter<ProductSizeAdapter.SizeViewHolder>() {

    private var selectedPosition: Int = 0

    inner class SizeViewHolder(
        private val binding: ItemSizeOptionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(size: String, isSelected: Boolean) {
            binding.apply {
                tvSize.text = size
                
                val strokeColor = if (isSelected) {
                    ContextCompat.getColor(root.context, R.color.primary)
                } else {
                    ContextCompat.getColor(root.context, R.color.stroke_color)
                }
                
                val textColor = if (isSelected) {
                    ContextCompat.getColor(root.context, R.color.primary)
                } else {
                    ContextCompat.getColor(root.context, R.color.text_primary_light)
                }
                
                val strokeWidth = if (isSelected) 2 else 1
                cardSize.strokeColor = strokeColor
                cardSize.strokeWidth = strokeWidth
                tvSize.setTextColor(textColor)
                
                root.setOnClickListener {
                    selectSize(adapterPosition)
                    onSizeSelected(size)
                }
            }
        }
    }

    private fun selectSize(position: Int) {
        val previousPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(previousPosition)
        notifyItemChanged(selectedPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeViewHolder {
        val binding = ItemSizeOptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SizeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SizeViewHolder, position: Int) {
        holder.bind(sizes[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = sizes.size
    
    fun getSelectedSize(): String = sizes[selectedPosition]
}

