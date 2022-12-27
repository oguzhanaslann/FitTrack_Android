package com.oguzhanaslann.feature_home.ui.trace

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.oguzhanaslann.commonui.navController
import com.oguzhanaslann.commonui.showSuccessSnackbar
import com.oguzhanaslann.commonui.verticalLinearLayoutManaged
import com.oguzhanaslann.commonui.viewBinding
import com.oguzhanaslann.feature_home.R
import com.oguzhanaslann.feature_home.databinding.FragmentTraceWorkoutBinding
import com.oguzhanaslann.feature_home.domain.model.TraceExercise
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TraceWorkoutFragment : Fragment(R.layout.fragment_trace_workout) {

    private val binding: FragmentTraceWorkoutBinding by viewBinding(FragmentTraceWorkoutBinding::bind)

    private val viewModel: TraceWorkoutViewModel by viewModels()
    private val args by navArgs<TraceWorkoutFragmentArgs>()

    private val traceAdapter by lazy {
        TraceAdapter(
            onItemClicked = ::onItemClicked
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getDailyPlan(args.dailyPlanId)
        binding.rvWorkoutTrace.apply {
            verticalLinearLayoutManaged()
            adapter = traceAdapter
        }

        binding.confirmFab.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.are_you_sure))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    viewModel.completeDailyPlan(args.dailyPlanId)
                }
                .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.traceWorkoutViewState.observe(viewLifecycleOwner) {
            traceAdapter.submitList(it.traceExercises)
        }

        lifecycleScope.launch {
            viewModel.traceWorkoutEvent
                .flowWithLifecycle(lifecycle)
                .collect {
                    when (it) {
                        TraceWorkoutEvent.WorkoutCompleted -> showSuccessSnackbar(
                            container = binding.root,
                            message = getString(R.string.workout_completed),
                            onDialogDismissed = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
        }
    }

    private fun onItemClicked(traceExercise: TraceExercise) {
        viewModel.onExerciseClicked(traceExercise)
    }
}
