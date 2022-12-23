package com.oguzhanaslann.feature_reports.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.oguzhanaslann.feature_reports.databinding.ItemExerciseLayoutBinding
import com.oguzhanaslann.feature_reports.domain.ReportExercise

class ReportExerciseAdapter :
    ListAdapter<ReportExercise, ReportExerciseAdapter.Holder>(DiffCallBack()) {

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
        fun onBind(currentItem: ReportExercise) = binding.run {
            exerciseImage.load(currentItem.image) {
                placeholder(com.oguzhanaslann.commonui.R.drawable.placeholder_thumbnail)
                error(com.oguzhanaslann.commonui.R.drawable.placeholder_thumbnail)
            }

            exerciseNameText.text = currentItem.name
            exerciseDescriptionText.text = currentItem.description
            exerciseDoneImage.isVisible = currentItem.isDone
        }
    }

    class DiffCallBack : DiffUtil.ItemCallback<ReportExercise>() {
        override fun areItemsTheSame(oldItem: ReportExercise, newItem: ReportExercise): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ReportExercise, newItem: ReportExercise): Boolean =
            oldItem == newItem
    }
}
