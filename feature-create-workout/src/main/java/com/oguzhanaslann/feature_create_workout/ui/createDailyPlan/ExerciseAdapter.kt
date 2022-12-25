package com.oguzhanaslann.feature_create_workout.ui.createDailyPlan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.oguzhanaslann.feature_create_workout.databinding.ItemAddExerciseLayoutBinding
import com.oguzhanaslann.feature_create_workout.databinding.ItemExerciseLayoutBinding

class ExerciseAdapter(
    private inline val onAddExerciseClicked: () -> Unit = {},
) : ListAdapter<ExerciseSetUIModel, RecyclerView.ViewHolder>(DiffCallBack()) {

    init {
        submitList(emptyList())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            ADD_WORKOUT_DAILY_PLAN_TYPE -> {
                val binding = ItemAddExerciseLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                return AddExerciseHolder(binding)
            }

            WORKOUT_DAILY_PLAN_TYPE -> {
                val binding = ItemExerciseLayoutBinding.inflate(
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
        return when (item.name) {
            ADD_WORKOUT_EXERCISE_PLAN -> ADD_WORKOUT_DAILY_PLAN_TYPE
            else -> WORKOUT_DAILY_PLAN_TYPE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        when (holder) {
            is Holder -> holder.onBind(currentItem)
            is AddExerciseHolder -> holder.onBind()
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    inner class Holder(val binding: ItemExerciseLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(currentItem: ExerciseSetUIModel) = binding.run {
            exerciseImage.load(currentItem.imageUrl) {
                error(com.oguzhanaslann.commonui.R.drawable.placeholder_thumbnail)
            }

            exerciseName.text = currentItem.name

            val repsAndSets = if (currentItem.reps > 0 && currentItem.sets > 0) {
                "${currentItem.reps}x ${currentItem.sets} "
            } else {
                ""
            }

            exerciseDescription.text = repsAndSets
        }
    }

    inner class AddExerciseHolder(val binding: ItemAddExerciseLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind() = binding.run {
            root.setOnClickListener { onAddExerciseClicked() }
        }
    }

    override fun submitList(list: List<ExerciseSetUIModel>?) {
        val newList = mutableListOf<ExerciseSetUIModel>()
        newList.addAll(list ?: emptyList())
        newList.add(
            ExerciseSetUIModel(
                name = ADD_WORKOUT_EXERCISE_PLAN,
                imageUrl = ""
            )
        )
        super.submitList(newList)
    }

    class DiffCallBack : DiffUtil.ItemCallback<ExerciseSetUIModel>() {
        override fun areItemsTheSame(
            oldItem: ExerciseSetUIModel,
            newItem: ExerciseSetUIModel,
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: ExerciseSetUIModel,
            newItem: ExerciseSetUIModel,
        ): Boolean = oldItem == newItem
    }

    companion object {
        private const val ADD_WORKOUT_EXERCISE_PLAN = "tf8la2s05x"

        const val ADD_WORKOUT_DAILY_PLAN_TYPE = 0
        const val WORKOUT_DAILY_PLAN_TYPE = 1
    }
}
