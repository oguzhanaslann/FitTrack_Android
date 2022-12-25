package com.oguzhanaslann.feature_workouts.ui.workoutDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.oguzhanaslann.feature_workouts.R
import com.oguzhanaslann.feature_workouts.databinding.ItemWorkoutDailyPlanLayoutBinding

class WorkoutProgramAdapter(
    private inline val onClick: (DailyPlanShort) -> Unit = {},
) : ListAdapter<DailyPlanShort, WorkoutProgramAdapter.Holder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemWorkoutDailyPlanLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val currentItem = getItem(position)
        holder.onBind(currentItem, position)
    }

    inner class Holder(val binding: ItemWorkoutDailyPlanLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(currentItem: DailyPlanShort, position: Int) = binding.run {
            workoutDayText.text = root.context.getString(R.string.workout_daily_day, (position + 1))
            workoutNameText.text = currentItem.name
            caloriesText.text = root.context.getString(R.string.kcal_value_text,currentItem.calories)
            root.setOnClickListener {
                onClick(currentItem)
            }
        }
    }

    class DiffCallBack : DiffUtil.ItemCallback<DailyPlanShort>() {
        override fun areItemsTheSame(oldItem: DailyPlanShort, newItem: DailyPlanShort): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: DailyPlanShort, newItem: DailyPlanShort): Boolean =
            oldItem == newItem
    }
}
