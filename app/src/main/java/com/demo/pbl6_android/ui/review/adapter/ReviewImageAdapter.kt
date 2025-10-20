package com.demo.pbl6_android.ui.review.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.pbl6_android.databinding.ItemReviewImageBinding

class ReviewImageAdapter(
    private val images: List<Int>,
    private val onImageClick: (Int) -> Unit
) : RecyclerView.Adapter<ReviewImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(private val binding: ItemReviewImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageRes: Int) {
            binding.ivReviewImage.setImageResource(imageRes)
            binding.root.setOnClickListener {
                onImageClick(imageRes)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemReviewImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size
}

