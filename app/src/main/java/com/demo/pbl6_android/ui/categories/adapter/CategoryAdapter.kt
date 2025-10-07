package com.demo.pbl6_android.ui.categories.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.demo.pbl6_android.R
import com.demo.pbl6_android.data.model.Category
import com.demo.pbl6_android.databinding.ItemCategoryBinding

class CategoryAdapter(
    private val categories: List<Category>,
    private val onCategoryClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size

    inner class CategoryViewHolder(
        private val binding: ItemCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.apply {
                tvIcon.text = category.icon
                tvName.text = category.name

                // Apply selected state
                val cardColor = if (category.isSelected) {
                    ContextCompat.getColor(root.context, R.color.primary)
                } else {
                    ContextCompat.getColor(root.context, R.color.white)
                }

                val textColor = if (category.isSelected) {
                    ContextCompat.getColor(root.context, R.color.white)
                } else {
                    ContextCompat.getColor(root.context, R.color.text_primary_light)
                }

                cardCategory.setCardBackgroundColor(cardColor)
                tvName.setTextColor(textColor)

                root.setOnClickListener {
                    onCategoryClick(category)
                }
            }
        }
    }
}

