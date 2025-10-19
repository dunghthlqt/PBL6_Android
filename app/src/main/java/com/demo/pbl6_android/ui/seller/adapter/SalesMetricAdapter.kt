package com.demo.pbl6_android.ui.seller.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.demo.pbl6_android.databinding.ItemMetricCardBinding
import com.demo.pbl6_android.ui.seller.model.SalesMetric

class SalesMetricAdapter : ListAdapter<SalesMetric, SalesMetricAdapter.MetricViewHolder>(MetricDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MetricViewHolder {
        val binding = ItemMetricCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MetricViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MetricViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MetricViewHolder(
        private val binding: ItemMetricCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(metric: SalesMetric) {
            binding.apply {
                tvMetricLabel.text = metric.label
                tvMetricValue.text = metric.value
                tvMetricChange.text = metric.changePercent
                ivInfo.visibility = if (metric.hasInfoIcon) View.VISIBLE else View.GONE
            }
        }
    }

    private class MetricDiffCallback : DiffUtil.ItemCallback<SalesMetric>() {
        override fun areItemsTheSame(oldItem: SalesMetric, newItem: SalesMetric): Boolean {
            return oldItem.label == newItem.label
        }

        override fun areContentsTheSame(oldItem: SalesMetric, newItem: SalesMetric): Boolean {
            return oldItem == newItem
        }
    }
}

