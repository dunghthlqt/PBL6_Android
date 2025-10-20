package com.demo.pbl6_android.ui.review.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.pbl6_android.data.model.Review
import com.demo.pbl6_android.databinding.ItemReviewBinding

class ReviewAdapter(
    private val reviews: List<Review>,
    private val onImageClick: (Int) -> Unit
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(review: Review) {
            binding.apply {
                tvUserName.text = review.userName
                tvComment.text = review.comment
                tvDate.text = review.date
                tvHelpfulCount.text = "Hữu ích(${review.helpfulCount})"
                
                // Display rating stars
                val stars = "★".repeat(review.rating.toInt()) + "☆".repeat(5 - review.rating.toInt())
                tvRatingStars.text = stars
                
                // Setup images
                if (review.images.isNotEmpty()) {
                    rvImages.visibility = View.VISIBLE
                    rvImages.layoutManager = LinearLayoutManager(
                        binding.root.context,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    rvImages.adapter = ReviewImageAdapter(review.images, onImageClick)
                } else {
                    rvImages.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    override fun getItemCount(): Int = reviews.size
}

