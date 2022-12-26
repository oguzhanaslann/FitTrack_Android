package com.oguzhanaslann.feature_create_workout.ui.createExercise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.oguzhanaslann.feature_create_workout.databinding.ItemExerciseSearchLayoutBinding
import com.oguzhanaslann.common_domain.Exercise

class ExerciseSearchAdapter(
    private val onExerciseClicked: (Exercise) -> Unit = {}
) : ListAdapter<Exercise, ExerciseSearchAdapter.Holder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemExerciseSearchLayoutBinding.inflate(
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

    inner class Holder(val binding: ItemExerciseSearchLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(currentItem: Exercise) = binding.run {
            exerciseSearchImage.load(currentItem.imageUrl) {
                error(com.oguzhanaslann.commonui.R.drawable.placeholder_thumbnail)
            }
            exerciseSearchName.text = currentItem.name

            root.setOnClickListener {
                onExerciseClicked(currentItem)
            }
        }

    }

    class DiffCallBack : DiffUtil.ItemCallback<Exercise>() {
        override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean = oldItem == newItem

    }

}
