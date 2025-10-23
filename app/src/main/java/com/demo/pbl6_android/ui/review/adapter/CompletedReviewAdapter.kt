package com.demo.pbl6_android.ui.review.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.demo.pbl6_android.R
import com.demo.pbl6_android.data.api.model.ReviewResponse
import com.demo.pbl6_android.databinding.ItemReviewCompletedBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Adapter for completed reviews (reviews that user has written)
 */
class CompletedReviewAdapter(
    private val onEditClick: (ReviewResponse) -> Unit,
    private val onBuyAgainClick: (ReviewResponse) -> Unit
) : ListAdapter<ReviewResponse, CompletedReviewAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReviewCompletedBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, onEditClick, onBuyAgainClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemReviewCompletedBinding,
        private val onEditClick: (ReviewResponse) -> Unit,
        private val onBuyAgainClick: (ReviewResponse) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(review: ReviewResponse) {
            binding.apply {
                // TODO: We need product info from API (name, price, image)
                // For now, use placeholder
                tvProductName.text = review.productVariantId // Placeholder
                tvProductPrice.text = "N/A" // Need product price from API
                
                // Format date
                tvReviewDate.text = formatDate(review.createdAt)
                
                // Show star rating
                tvRating.text = getStarString(review.rating)
                
                // Show comment
                tvComment.text = review.comment
                
                // Load product image (placeholder for now)
                Glide.with(ivProductImage.context)
                    .load(R.drawable.ic_launcher_background) // Placeholder
                    .centerCrop()
                    .into(ivProductImage)
                
                // Click listeners
                btnEdit.setOnClickListener { onEditClick(review) }
                btnBuyAgain.setOnClickListener { onBuyAgainClick(review) }
            }
        }

        private fun formatDate(dateString: String?): String {
            if (dateString == null) return ""
            
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = inputFormat.parse(dateString)
                outputFormat.format(date ?: Date())
            } catch (e: Exception) {
                dateString
            }
        }

        private fun getStarString(rating: Int): String {
            val fullStar = "★"
            val emptyStar = "☆"
            return buildString {
                repeat(rating) { append(fullStar) }
                repeat(5 - rating) { append(emptyStar) }
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<ReviewResponse>() {
        override fun areItemsTheSame(oldItem: ReviewResponse, newItem: ReviewResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ReviewResponse, newItem: ReviewResponse): Boolean {
            return oldItem == newItem
        }
    }
}

