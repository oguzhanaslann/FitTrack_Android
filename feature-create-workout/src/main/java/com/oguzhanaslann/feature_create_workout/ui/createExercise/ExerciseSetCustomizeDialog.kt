package com.oguzhanaslann.feature_create_workout.ui.createExercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.oguzhanaslann.commonui.navController
import com.oguzhanaslann.commonui.viewBinding
import com.oguzhanaslann.feature_create_workout.R
import com.oguzhanaslann.feature_create_workout.databinding.FragmentExerciseSetCustomizeDialogBinding

const val EXERCISE_CUSTOMIZATION_REQUEST_KEY = "EXERCISE_CUSTOMIZATION_REQUEST_KEY"
const val REP_KEY = "REP_KEY"
const val SET_KEY = "SET_KEY"

class ExerciseSetCustomizeDialog : Fragment(R.layout.fragment_exercise_set_customize_dialog) {

    private val binding by viewBinding(FragmentExerciseSetCustomizeDialogBinding::bind)

    private val viewModel by viewModels<ExerciseSetCustomizeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        initViews()
    }

    private fun initViews() {
        binding.confirmButton.setOnClickListener {
            setFragmentResult(
                requestKey = EXERCISE_CUSTOMIZATION_REQUEST_KEY,
                result = Bundle().apply {
                    putInt(REP_KEY, viewModel.getCurrentReps())
                    putInt(SET_KEY, viewModel.getCurrentSets())
                }
            )
            navController.popBackStack()
        }
    }
}
