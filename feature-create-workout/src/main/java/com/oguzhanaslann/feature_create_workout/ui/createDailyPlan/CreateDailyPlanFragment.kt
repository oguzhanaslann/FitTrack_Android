package com.oguzhanaslann.feature_create_workout.ui.createDailyPlan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.load
import com.oguzhanaslann.commonui.navController
import com.oguzhanaslann.commonui.showErrorSnackbar
import com.oguzhanaslann.commonui.verticalLinearLayoutManaged
import com.oguzhanaslann.commonui.viewBinding
import com.oguzhanaslann.feature_create_workout.R
import com.oguzhanaslann.feature_create_workout.databinding.FragmentCreateDailyPlanBinding
import com.oguzhanaslann.feature_create_workout.domain.DailyPlan
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

const val DailyPlanCreatedRequestKey = "DailyPlanCreatedRequestKey"
const val DailyPlanCreatedKey = "DailyPlanCreatedRequestKey"

@AndroidEntryPoint
class CreateDailyPlanFragment : Fragment(R.layout.fragment_create_daily_plan) {

    private val binding by viewBinding(FragmentCreateDailyPlanBinding::bind)

    private val viewModel: CreateDailyPlanViewModel by viewModels()

    private val photoPicker =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri ?: return@registerForActivityResult
            viewModel.onPhotoSelected(uri)
            val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
            context?.contentResolver?.takePersistableUriPermission(uri, flag)
        }

    private val exerciseAdapter by lazy {
        ExerciseAdapter(
            onAddExerciseClicked = ::onAddExerciseClicked
         )
    }

    private fun onAddExerciseClicked() {
        // TODO("Not yet implemented")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        initViews()
        subscribeObservers()
    }

    private fun initViews() {

        binding.titleInput.setText(viewModel.getCurrentTitle())
        binding.titleInput.doAfterTextChanged {
            viewModel.onTitleChanged(it.toString())
        }

        binding.caloriesInput.value = viewModel.getCurrentCalories().toFloat()
        binding.caloriesInput.addOnChangeListener { slider, value, fromUser ->
            if (fromUser) {
                viewModel.onCaloriesChanged(value.toInt())
            }
        }

        binding.confirmFab.setOnClickListener {
            viewModel.onConfirmClicked()
        }

        binding.addDailyPlanCoverPhotoContainer.setOnClickListener {
            photoPicker.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }

        binding.rvExercises.apply {
            verticalLinearLayoutManaged()
            adapter = exerciseAdapter
        }
    }

    private fun subscribeObservers() {
        observeUIState()
        collectEvents()
    }

    private fun observeUIState() {
        viewModel.dailyPlanUIState.observe(viewLifecycleOwner) { uiState ->
            setCoverPhotoSection(uiState.dailyPlanPhotoUri)
            exerciseAdapter.submitList(uiState.exercises)
        }
    }

    private fun collectEvents() {
        lifecycleScope.launch {
            viewModel.createDailyPlanEvents
                .flowWithLifecycle(lifecycle)
                .collect {
                    when (it) {
                        is CreateDailyPlanEvent.DailyPlanCreated -> sendCreationResultAndGoBack(it.dailyPlan)
                        CreateDailyPlanEvent.DailyPlanCaloriesEmpty -> showErrorSnackbar(
                            container = binding.root,
                            message = getString(R.string.calories_empty_text)
                        )
                        CreateDailyPlanEvent.DailyPlanExerciseEmpty -> showErrorSnackbar(
                            container = binding.root,
                            message = getString(R.string.exercise_empty_text)
                        )
                        CreateDailyPlanEvent.DailyPlanNameEmpty -> showErrorSnackbar(
                            container = binding.root,
                            message = getString(R.string.daily_plan_title_empty)
                        )
                    }
                }
        }
    }

    private fun sendCreationResultAndGoBack(dailyPlan: DailyPlan) {
        setFragmentResult(
            requestKey = DailyPlanCreatedRequestKey,
            result = Bundle().apply {
                putParcelable(DailyPlanCreatedKey, dailyPlan)
            }
        )
        navController.popBackStack()
    }

    private fun setCoverPhotoSection(workoutPhotoUri: Uri?) {
        binding.addDailyPlanCoverPhoto.isVisible = workoutPhotoUri != null
        if (workoutPhotoUri != null) {
            binding.addDailyPlanCoverPhoto.load(workoutPhotoUri) {
                error(com.oguzhanaslann.commonui.R.drawable.placeholder_thumbnail)
            }
        }
    }
}
