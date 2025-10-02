package com.demo.pbl6_android.ui.product.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.pbl6_android.databinding.ItemSpecificationBinding

class SpecificationAdapter(
    private val specifications: List<Pair<String, String>>
) : RecyclerView.Adapter<SpecificationAdapter.SpecViewHolder>() {

    inner class SpecViewHolder(
        private val binding: ItemSpecificationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(spec: Pair<String, String>) {
            binding.apply {
                tvSpecLabel.text = spec.first
                tvSpecValue.text = spec.second
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecViewHolder {
        val binding = ItemSpecificationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SpecViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SpecViewHolder, position: Int) {
        holder.bind(specifications[position])
    }

    override fun getItemCount(): Int = specifications.size
}

