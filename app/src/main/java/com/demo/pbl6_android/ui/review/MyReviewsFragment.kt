package com.demo.pbl6_android.ui.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.pbl6_android.R
import com.demo.pbl6_android.data.api.ApiResult
import com.demo.pbl6_android.data.repository.ReviewApiRepository
import com.demo.pbl6_android.databinding.FragmentMyReviewsBinding
import com.demo.pbl6_android.ui.review.adapter.CompletedReviewAdapter
import kotlinx.coroutines.launch

/**
 * Fragment for "My Reviews" screen
 * Shows 2 tabs:
 * - Tab 1: "Chưa đánh giá" (Pending reviews - products waiting for review)
 * - Tab 2: "Đã đánh giá" (Completed reviews - reviews already written)
 */
class MyReviewsFragment : Fragment() {

    private var _binding: FragmentMyReviewsBinding? = null
    private val binding: FragmentMyReviewsBinding
        get() = _binding!!

    private lateinit var completedReviewAdapter: CompletedReviewAdapter
    private var currentTab: ReviewTab = ReviewTab.PENDING

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupTabs()
        loadReviews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViews() {
        binding.apply {
            // Back button
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            // Setup RecyclerView
            rvReviews.layoutManager = LinearLayoutManager(requireContext())
            completedReviewAdapter = CompletedReviewAdapter(
                onEditClick = { review ->
                    // TODO: Navigate to edit review screen
                    Toast.makeText(requireContext(), "Chỉnh sửa đánh giá: ${review.id}", Toast.LENGTH_SHORT).show()
                },
                onBuyAgainClick = { review ->
                    // TODO: Add product to cart and navigate to cart/product detail
                    Toast.makeText(requireContext(), "Mua lại sản phẩm", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun setupTabs() {
        binding.apply {
            tabPending.setOnClickListener {
                switchTab(ReviewTab.PENDING)
            }

            tabCompleted.setOnClickListener {
                switchTab(ReviewTab.COMPLETED)
            }
        }
    }

    private fun switchTab(tab: ReviewTab) {
        currentTab = tab
        
        binding.apply {
            when (tab) {
                ReviewTab.PENDING -> {
                    // Update tab UI
                    tvTabPending.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
                    tvTabCompleted.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary))
                    viewTabPendingIndicator.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary))
                    viewTabCompletedIndicator.setBackgroundColor(android.graphics.Color.TRANSPARENT)
                    
                    // Show pending reviews (products waiting for review)
                    showPendingReviews()
                }
                
                ReviewTab.COMPLETED -> {
                    // Update tab UI
                    tvTabPending.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary))
                    tvTabCompleted.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
                    viewTabPendingIndicator.setBackgroundColor(android.graphics.Color.TRANSPARENT)
                    viewTabCompletedIndicator.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary))
                    
                    // Show completed reviews
                    loadCompletedReviews()
                }
            }
        }
    }

    private fun loadReviews() {
        // Load initial tab (Pending)
        showPendingReviews()
    }

    private fun showPendingReviews() {
        // TODO: Load orders with delivered status that haven't been reviewed yet
        // For now, show empty state with message
        binding.apply {
            rvReviews.visibility = View.GONE
            layoutEmpty.visibility = View.VISIBLE
            tvEmptyMessage.text = "Chưa có sản phẩm nào cần đánh giá\n\n(Chức năng đánh giá sản phẩm yêu cầu có đơn hàng đã giao)"
        }
    }

    private fun loadCompletedReviews() {
        showLoading(true)
        
        viewLifecycleOwner.lifecycleScope.launch {
            val result = ReviewApiRepository.getMyReviews()
            
            showLoading(false)
            
            when (result) {
                is ApiResult.Success -> {
                    val reviews = result.data
                    
                    if (reviews.isEmpty()) {
                        showEmptyState("Bạn chưa có đánh giá nào")
                    } else {
                        showReviews(reviews.size)
                        completedReviewAdapter.submitList(reviews)
                        binding.rvReviews.adapter = completedReviewAdapter
                    }
                }
                
                is ApiResult.Error -> {
                    showEmptyState("Lỗi tải đánh giá: ${result.message}")
                    Toast.makeText(
                        requireContext(),
                        "Lỗi: ${result.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                
                is ApiResult.Loading -> {
                    // Already showing loading
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            rvReviews.visibility = if (isLoading) View.GONE else View.VISIBLE
            layoutEmpty.visibility = View.GONE
        }
    }

    private fun showEmptyState(message: String) {
        binding.apply {
            rvReviews.visibility = View.GONE
            layoutEmpty.visibility = View.VISIBLE
            tvEmptyMessage.text = message
        }
    }

    private fun showReviews(count: Int) {
        binding.apply {
            rvReviews.visibility = View.VISIBLE
            layoutEmpty.visibility = View.GONE
            
            android.util.Log.d("MyReviewsFragment", "✅ Showing $count reviews")
        }
    }

    enum class ReviewTab {
        PENDING,    // Chưa đánh giá
        COMPLETED   // Đã đánh giá
    }
}

