package com.oguzhanaslann.feature_home.ui.trace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.oguzhanaslann.feature_home.databinding.ItemExerciseLayoutBinding
import com.oguzhanaslann.feature_home.domain.model.TraceExercise

class TraceAdapter(
    private val onItemClicked: (TraceExercise) -> Unit,
) : ListAdapter<TraceExercise, TraceAdapter.Holder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemExerciseLayoutBinding.inflate(
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

    inner class Holder(val binding: ItemExerciseLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(currentItem: TraceExercise) = binding.run {
            exerciseImage.load(currentItem.exerciseSet.exercise.imageUrl) {
                error(com.oguzhanaslann.commonui.R.drawable.placeholder_thumbnail)
            }

            exerciseName.text = currentItem.exerciseSet.exercise.name

            val repsAndSets =
                if ((currentItem.exerciseSet.reps ?: 0) > 0 && (currentItem.exerciseSet.sets
                        ?: 0) > 0
                ) {
                    "${currentItem.exerciseSet.reps}x${currentItem.exerciseSet.sets} "
                } else {
                    ""
                }

            exerciseDescription.text = repsAndSets
            checkedIcon.isVisible = currentItem.isChecked

            root.setOnClickListener {
                onItemClicked(currentItem)
            }
        }
    }

    class DiffCallBack : DiffUtil.ItemCallback<TraceExercise>() {
        override fun areItemsTheSame(oldItem: TraceExercise, newItem: TraceExercise): Boolean =
            oldItem.exerciseSet.exercise.id == newItem.exerciseSet.exercise.id

        override fun areContentsTheSame(oldItem: TraceExercise, newItem: TraceExercise): Boolean =
            oldItem == newItem
    }
}
