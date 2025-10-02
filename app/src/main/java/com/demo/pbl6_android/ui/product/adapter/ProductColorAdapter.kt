package com.demo.pbl6_android.ui.product.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.demo.pbl6_android.R
import com.demo.pbl6_android.data.model.ProductColor
import com.demo.pbl6_android.databinding.ItemColorOptionBinding

class ProductColorAdapter(
    private val colors: List<ProductColor>,
    private val onColorSelected: (ProductColor) -> Unit
) : RecyclerView.Adapter<ProductColorAdapter.ColorViewHolder>() {

    private var selectedPosition: Int = 0

    inner class ColorViewHolder(
        private val binding: ItemColorOptionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(color: ProductColor, isSelected: Boolean) {
            binding.apply {
                viewColor.setBackgroundColor(Color.parseColor(color.hexCode))
                tvColorName.text = color.name
                
                val strokeColor = if (isSelected) {
                    ContextCompat.getColor(root.context, R.color.primary)
                } else {
                    ContextCompat.getColor(root.context, R.color.stroke_color)
                }
                
                val strokeWidth = if (isSelected) 3 else 1
                cardColor.strokeColor = strokeColor
                cardColor.strokeWidth = strokeWidth
                
                root.setOnClickListener {
                    selectColor(adapterPosition)
                    onColorSelected(color)
                }
            }
        }
    }

    private fun selectColor(position: Int) {
        val previousPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(previousPosition)
        notifyItemChanged(selectedPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val binding = ItemColorOptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ColorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(colors[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = colors.size
    
    fun getSelectedColor(): ProductColor = colors[selectedPosition]
}

