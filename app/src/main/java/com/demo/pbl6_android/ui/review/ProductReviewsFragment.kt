package com.demo.pbl6_android.ui.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.pbl6_android.R
import com.demo.pbl6_android.data.ReviewRepository
import com.demo.pbl6_android.data.model.Review
import com.demo.pbl6_android.databinding.FragmentProductReviewsBinding
import com.demo.pbl6_android.ui.review.adapter.ReviewAdapter

class ProductReviewsFragment : Fragment() {

    private var _binding: FragmentProductReviewsBinding? = null
    private val binding: FragmentProductReviewsBinding
        get() = _binding!!

    private lateinit var reviewAdapter: ReviewAdapter
    private var productId: String = ""
    private var allReviews: List<Review> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        productId = arguments?.getString("productId") ?: ""
        
        setupViews()
        setupRecyclerView()
        loadReviews()
        setupFilters()
    }

    private fun setupViews() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        binding.rvReviews.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun loadReviews() {
        allReviews = ReviewRepository.getReviewsByProduct(productId)
        displayReviews(allReviews)
        updateSummary()
    }

    private fun updateSummary() {
        val averageRating = ReviewRepository.getAverageRating(productId)
        val totalReviews = ReviewRepository.getTotalReviewsCount(productId)
        
        binding.apply {
            tvAverageRating.text = String.format("%.1f", averageRating)
            
            val fullStars = averageRating.toInt()
            val stars = "★".repeat(fullStars) + "☆".repeat(5 - fullStars)
            tvSummaryStars.text = stars
            
            tvTotalReviews.text = "$totalReviews đánh giá"
        }
    }

    private fun setupFilters() {
        binding.chipGroupFilters.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isEmpty()) return@setOnCheckedStateChangeListener
            
            when (checkedIds[0]) {
                R.id.chip_all -> displayReviews(allReviews)
                R.id.chip_with_media -> {
                    val filtered = ReviewRepository.getReviewsWithImages(productId)
                    displayReviews(filtered)
                }
                R.id.chip_5_star -> displayReviewsByStar(5)
                R.id.chip_4_star -> displayReviewsByStar(4)
                R.id.chip_3_star -> displayReviewsByStar(3)
                R.id.chip_2_star -> displayReviewsByStar(2)
                R.id.chip_1_star -> displayReviewsByStar(1)
            }
        }
    }

    private fun displayReviewsByStar(rating: Int) {
        val filtered = ReviewRepository.getReviewsByRating(productId, rating)
        displayReviews(filtered)
    }

    private fun displayReviews(reviews: List<Review>) {
        if (reviews.isEmpty()) {
            binding.emptyState.visibility = View.VISIBLE
            binding.rvReviews.visibility = View.GONE
        } else {
            binding.emptyState.visibility = View.GONE
            binding.rvReviews.visibility = View.VISIBLE
            
            reviewAdapter = ReviewAdapter(reviews) { imageRes ->
                // Handle image click - can open full screen image viewer
            }
            binding.rvReviews.adapter = reviewAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

