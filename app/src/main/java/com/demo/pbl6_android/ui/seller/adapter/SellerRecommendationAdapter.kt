package com.demo.pbl6_android.ui.seller.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.demo.pbl6_android.databinding.ItemSellerRecommendationBinding
import com.demo.pbl6_android.ui.seller.model.SellerRecommendation

class SellerRecommendationAdapter(
    private val onTryNow: (SellerRecommendation) -> Unit
) : ListAdapter<SellerRecommendation, SellerRecommendationAdapter.RecommendationViewHolder>(
    RecommendationDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val binding = ItemSellerRecommendationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecommendationViewHolder(binding, onTryNow)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RecommendationViewHolder(
        private val binding: ItemSellerRecommendationBinding,
        private val onTryNow: (SellerRecommendation) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recommendation: SellerRecommendation) {
            binding.apply {
                tvRecommendationTitle.text = recommendation.title
                tvRecommendationDescription.text = recommendation.description
                ivRecommendationIcon.setImageResource(recommendation.iconResId)
                
                btnTryNow.setOnClickListener {
                    onTryNow(recommendation)
                }
            }
        }
    }

    private class RecommendationDiffCallback : DiffUtil.ItemCallback<SellerRecommendation>() {
        override fun areItemsTheSame(
            oldItem: SellerRecommendation,
            newItem: SellerRecommendation
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: SellerRecommendation,
            newItem: SellerRecommendation
        ): Boolean {
            return oldItem == newItem
        }
    }
}

