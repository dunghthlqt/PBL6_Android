package com.demo.pbl6_android.ui.seller.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.demo.pbl6_android.databinding.ItemSellerTaskBinding
import com.demo.pbl6_android.ui.seller.model.SellerTask

class SellerTaskAdapter(
    private val onStartTask: (SellerTask) -> Unit
) : ListAdapter<SellerTask, SellerTaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemSellerTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskViewHolder(binding, onStartTask)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TaskViewHolder(
        private val binding: ItemSellerTaskBinding,
        private val onStartTask: (SellerTask) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: SellerTask) {
            binding.apply {
                tvTaskTitle.text = task.title
                tvTaskReward.text = task.reward
                tvTaskProgress.text = "${task.currentProgress}/${task.maxProgress}"
                progressTask.max = task.maxProgress
                progressTask.progress = task.currentProgress
                
                btnStartTask.setOnClickListener {
                    onStartTask(task)
                }
            }
        }
    }

    private class TaskDiffCallback : DiffUtil.ItemCallback<SellerTask>() {
        override fun areItemsTheSame(oldItem: SellerTask, newItem: SellerTask): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SellerTask, newItem: SellerTask): Boolean {
            return oldItem == newItem
        }
    }
}

