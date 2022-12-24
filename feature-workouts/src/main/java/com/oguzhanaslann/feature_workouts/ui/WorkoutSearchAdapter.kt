package com.oguzhanaslann.feature_workouts.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.oguzhanaslann.feature_workouts.databinding.ItemWorkoutLayoutBinding

class WorkoutSearchAdapter(
    private inline val onClick: (WorkoutSearchItem) -> Unit = {},
) : ListAdapter<WorkoutSearchItem, WorkoutSearchAdapter.Holder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemWorkoutLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val currentItem = getItem(position)
        holder.onBind(currentItem)
    }

    inner class Holder(val binding: ItemWorkoutLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(currentItem: WorkoutSearchItem) = binding.run {
            binding.workoutTitleText.text = currentItem.name
            binding.workoutDescriptionText.text = currentItem.description
            binding.workoutImage.load(currentItem.image)

            root.setOnClickListener {
                onClick(currentItem)
            }
        }
    }

    class DiffCallBack : DiffUtil.ItemCallback<WorkoutSearchItem>() {
        override fun areItemsTheSame(
            oldItem: WorkoutSearchItem,
            newItem: WorkoutSearchItem,
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: WorkoutSearchItem,
            newItem: WorkoutSearchItem,
        ): Boolean = oldItem == newItem
    }
}
