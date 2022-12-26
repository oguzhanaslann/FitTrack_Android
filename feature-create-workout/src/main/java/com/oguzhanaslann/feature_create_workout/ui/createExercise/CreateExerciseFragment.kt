package com.oguzhanaslann.feature_create_workout.ui.createExercise

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.oguzhanaslann.common.onSuccess
import com.oguzhanaslann.commonui.navController
import com.oguzhanaslann.commonui.verticalLinearLayoutManaged
import com.oguzhanaslann.commonui.viewBinding
import com.oguzhanaslann.feature_create_workout.R
import com.oguzhanaslann.feature_create_workout.databinding.FragmentCreateExerciseBinding
import com.oguzhanaslann.feature_create_workout.domain.Exercise
import dagger.hilt.android.AndroidEntryPoint

const val EXERCISE_SET_CREATE_REQUEST_KEY = "EXERCISE_SET_CREATE_REQUEST_KEY"
const val EXERCISE_SET_KEY = "EXERCISE_SET_KEY"

@AndroidEntryPoint
class CreateExerciseFragment : Fragment(R.layout.fragment_create_exercise) {

    private val binding by viewBinding(FragmentCreateExerciseBinding::bind)

    private val viewModel: CreateExerciseViewModel by viewModels()

    private val exerciseSearchAdapter by lazy {
        ExerciseSearchAdapter(
            onExerciseClicked = ::onExerciseClicked
        )
    }

    private fun onExerciseClicked(exercise: Exercise) {
        viewModel.setCandidateExercise(exercise)
        navController.navigate(
            CreateExerciseFragmentDirections.actionCreateExerciseFragmentToExerciseSetCustomizeDialog()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setExerciseCustomizationResultListener()
    }

    private fun setExerciseCustomizationResultListener() {
        setFragmentResultListener(
            requestKey = EXERCISE_CUSTOMIZATION_REQUEST_KEY
        ) { _, bundle ->
            val reps = bundle.getInt(REP_KEY)
            val sets = bundle.getInt(SET_KEY)

            val exercise = viewModel.getCandidateExercise(reps, sets)
            exercise?.let {
                setFragmentResult(
                    requestKey = EXERCISE_SET_CREATE_REQUEST_KEY,
                    result = Bundle().apply {
                        putParcelable(EXERCISE_SET_KEY, it)
                    }
                )
                navController.popBackStack()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        subscribeObservers()
    }

    private fun initViews() {
        binding.exerciseSearchEditText.setText(
            viewModel.getQuery()
        )
        binding.exerciseSearchEditText.doAfterTextChanged {
            viewModel.onSearchQueryChanged(it.toString())
        }

        binding.rvExerciseSearch.apply {
            verticalLinearLayoutManaged()
            adapter = exerciseSearchAdapter
        }
    }

    private fun subscribeObservers() {
        viewModel.exercises.observe(viewLifecycleOwner) {
            it.onSuccess {
                exerciseSearchAdapter.submitList(it)
            }
        }
    }
}
