package com.oguzhanaslann.feature_create_workout.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.oguzhanaslann.feature_create_workout.R
import com.oguzhanaslann.feature_create_workout.databinding.ItemAddDailyPlanLayoutBinding
import com.oguzhanaslann.feature_create_workout.databinding.ItemDailyPlanLayoutBinding

class WorkoutDailyPlanAdapter(
    private inline val onAddDailyPlanClicked: () -> Unit,
) : ListAdapter<DailyPlanUIModel, RecyclerView.ViewHolder>(DiffCallBack()) {

    init {
        submitList(emptyList())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            ADD_WORKOUT_DAILY_PLAN_TYPE -> {
                val binding = ItemAddDailyPlanLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                return AddDailyPlanHolder(binding)
            }

            WORKOUT_DAILY_PLAN_TYPE -> {
                val binding = ItemDailyPlanLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                return Holder(binding)
            }

            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item.id) {
            ADD_WORKOUT_DAILY_PLAN -> ADD_WORKOUT_DAILY_PLAN_TYPE
            else -> WORKOUT_DAILY_PLAN_TYPE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        when (holder) {
            is Holder -> holder.onBind(currentItem)
            is AddDailyPlanHolder -> holder.onBind()
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    inner class Holder(val binding: ItemDailyPlanLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(currentItem: DailyPlanUIModel) = binding.run {
            planName.text = currentItem.name
            caloriesText.text =
                root.context.getString(R.string.kcal_value_text, currentItem.calories)
            planExeciseCountChip.text = root.context.resources.getQuantityString(
                R.plurals.exercises,
                currentItem.exerciseCount,
                currentItem.exerciseCount
            )
        }
    }

    inner class AddDailyPlanHolder(val binding: ItemAddDailyPlanLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind() = binding.run {
            root.setOnClickListener { onAddDailyPlanClicked() }
        }
    }

    override fun submitList(list: List<DailyPlanUIModel>?) {
        val newList = mutableListOf<DailyPlanUIModel>()
        newList.addAll(list ?: emptyList())
        newList.add(DailyPlanUIModel(ADD_WORKOUT_DAILY_PLAN, "", 0, 0))
        super.submitList(newList)
    }

    class DiffCallBack : DiffUtil.ItemCallback<DailyPlanUIModel>() {
        override fun areItemsTheSame(
            oldItem: DailyPlanUIModel,
            newItem: DailyPlanUIModel,
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: DailyPlanUIModel,
            newItem: DailyPlanUIModel,
        ): Boolean = oldItem == newItem
    }

    companion object {
        private const val ADD_WORKOUT_DAILY_PLAN = 78301

        const val ADD_WORKOUT_DAILY_PLAN_TYPE = 0
        const val WORKOUT_DAILY_PLAN_TYPE = 1
    }
}
