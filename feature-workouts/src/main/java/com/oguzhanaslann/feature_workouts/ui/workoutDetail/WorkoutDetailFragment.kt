package com.oguzhanaslann.feature_workouts.ui.workoutDetail

import android.os.Bundle
import android.view.View
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.oguzhanaslann.common.onSuccess
import com.oguzhanaslann.common_domain.DailyPlan
import com.oguzhanaslann.commonui.navController
import com.oguzhanaslann.commonui.verticalLinearLayoutManaged
import com.oguzhanaslann.commonui.viewBinding
import com.oguzhanaslann.feature_workouts.R
import com.oguzhanaslann.feature_workouts.databinding.FragmentWorkoutDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WorkoutDetailFragment : Fragment(R.layout.fragment_workout_detail) {

    private val binding by viewBinding(FragmentWorkoutDetailBinding::bind)

    private val viewModel: WorkoutDetailViewModel by viewModels()

    private val args by navArgs<WorkoutDetailFragmentArgs>()

    private val programAdapter by lazy {
        WorkoutProgramAdapter(
            onClick = ::onDailyPlanClick
        )
    }

    private fun onDailyPlanClick(dailyPlanShort: DailyPlan) {
        // TODO("Not yet implemented")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        subscribeObservers()
        viewModel.getWorkoutDetail(args.workoutId)
    }

    private fun initViews() {
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        binding.startWorkoutButton.setOnClickListener {
            viewModel.onStartWorkoutRequested(args.workoutId)
        }

        binding.rvPrograms.apply {
            verticalLinearLayoutManaged()
            adapter = programAdapter
        }
    }

    private fun subscribeObservers() {
        lifecycleScope.launch {
            viewModel.workoutDetailState
                .flowWithLifecycle(lifecycle)
                .collectLatest {
                    binding.startWorkoutButton.isVisible = !it.isActive
                    it.workoutDetail.onSuccess {
                        binding.toolbar.title = it.name
                        binding.toolbarBookListImage.load(it.imageUrl) {
                            error(com.oguzhanaslann.commonui.R.drawable.placeholder_thumbnail)
                        }
                        binding.textviewWorkoutDescription.text = it.description
                        programAdapter.submitList(it.programs)
                        setUpChipsBy(it)
                        binding.textviewWorkoutStatDuration.text = it.programs.size.toString()
                        binding.textviewWorkoutStatDurationDays.text = binding.root.context.resources.getQuantityString(
                            com.oguzhanaslann.commonui.R.plurals.days,
                            it.programs.size,
                            it.programs.size
                        )
                        binding.textviewWorkoutStatCalories.text = it.calories.toString()
                    }
                }
        }
    }

    private fun setUpChipsBy(workoutDetail: WorkoutDetail) {
        val currentChips = binding.chipGroup.children
            .filter { it is Chip }
            .map { it as Chip }
            .map { it.text }
            .distinct()
            .toList()

        val newChips = workoutDetail.tags.filter { !currentChips.contains(it) }

        newChips.forEach { tag ->
            binding.chipGroup.addView(tag)
        }
    }

    private fun String.toChip(): Chip {
        val chip = Chip(requireContext())
        chip.text = this
        chip.isClickable = false
        chip.isCheckable = false
        chip.setChipBackgroundColorResource(com.oguzhanaslann.commonui.R.color.white)
        return chip
    }

    private fun ChipGroup.addView(label: String) {
        addView(label.toChip())
    }
}
