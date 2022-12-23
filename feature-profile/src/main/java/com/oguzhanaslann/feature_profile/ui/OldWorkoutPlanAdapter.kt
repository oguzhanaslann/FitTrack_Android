package com.oguzhanaslann.feature_profile.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.oguzhanaslann.feature_profile.databinding.OldWorkoutPlanLayoutBinding
import com.oguzhanaslann.domain_profile.domain.model.OldWorkoutPlanOverView

class OldWorkoutPlanAdapter(
    private inline val onSeeDetailsClick: (OldWorkoutPlanOverView) -> Unit = {}
) : ListAdapter<OldWorkoutPlanOverView, OldWorkoutPlanAdapter.Holder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = OldWorkoutPlanLayoutBinding.inflate(
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

    inner class Holder(val binding: OldWorkoutPlanLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(currentItem: OldWorkoutPlanOverView) = binding.run {
            oldWorkoutPlanName.text = currentItem.name
            oldWorkoutPlanImage.load(currentItem.imageUrl)
            oldWorkoutStartButton.setOnClickListener { onSeeDetailsClick(currentItem) }
        }
    }

    class DiffCallBack : DiffUtil.ItemCallback<OldWorkoutPlanOverView>() {
        override fun areItemsTheSame(
            oldItem: OldWorkoutPlanOverView,
            newItem: OldWorkoutPlanOverView
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: OldWorkoutPlanOverView,
            newItem: OldWorkoutPlanOverView
        ): Boolean = oldItem == newItem

    }

}
