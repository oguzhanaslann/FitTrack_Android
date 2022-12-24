package com.oguzhanaslann.feature_workouts.ui

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.oguzhanaslann.common.onSuccess
import com.oguzhanaslann.commonui.verticalLinearLayoutManaged
import com.oguzhanaslann.commonui.viewBinding
import com.oguzhanaslann.feature_workouts.R
import com.oguzhanaslann.feature_workouts.databinding.FragmentWorkoutsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkoutsFragment : Fragment(R.layout.fragment_workouts) {

    private val binding by viewBinding(FragmentWorkoutsBinding::bind)

    private val viewModel: WorkoutsViewModel by viewModels()

    private val workoutSearchAdapter by lazy {
        WorkoutSearchAdapter(
            onClick = ::onWorkoutClicked
        )
    }

    private fun onWorkoutClicked(workoutSearchItem: WorkoutSearchItem) {
        //TODO("Not yet implemented")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        subscribeObservers()
    }

    private fun initViews() {
        binding.run {

            workoutSearchEditText.setText(viewModel.currentQuery())
            workoutSearchEditText.doAfterTextChanged {
                val query = it?.toString() ?: ""
                viewModel.search(query)
            }

            rvWorkouts.apply {
                verticalLinearLayoutManaged()
                adapter = workoutSearchAdapter
            }
        }
    }

    private fun subscribeObservers() {
        viewModel.workouts.observe(viewLifecycleOwner) {
            it.onSuccess {
                workoutSearchAdapter.submitList(it)
            }
        }
    }
}
