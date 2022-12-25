package com.oguzhanaslann.feature_create_workout.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.load
import com.oguzhanaslann.commonui.BundleCompat
import com.oguzhanaslann.commonui.navController
import com.oguzhanaslann.commonui.showErrorSnackbar
import com.oguzhanaslann.commonui.showSuccessSnackbar
import com.oguzhanaslann.commonui.verticalLinearLayoutManaged
import com.oguzhanaslann.commonui.viewBinding
import com.oguzhanaslann.feature_create_workout.R
import com.oguzhanaslann.feature_create_workout.databinding.FragmentCreateWorkoutBinding
import com.oguzhanaslann.feature_create_workout.domain.DailyPlan
import com.oguzhanaslann.feature_create_workout.ui.createDailyPlan.DailyPlanCreatedKey
import com.oguzhanaslann.feature_create_workout.ui.createDailyPlan.DailyPlanCreatedRequestKey
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateWorkoutFragment : Fragment(R.layout.fragment_create_workout) {

    private val binding by viewBinding(FragmentCreateWorkoutBinding::bind)

    private val viewModel: CreateWorkoutViewModel by viewModels()

    private val workoutDailyPlanAdapter by lazy {
        WorkoutDailyPlanAdapter(
            onAddDailyPlanClicked = ::onAddDailyPlanClicked
        )
    }

    private fun onAddDailyPlanClicked() {
        navController.navigate(
            CreateWorkoutFragmentDirections.actionCreateWorkoutFragmentToCreateDailyPlanFragment()
        )
    }

    private val photoPicker =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri ?: return@registerForActivityResult
            viewModel.onPhotoSelected(uri)
            val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
            context?.contentResolver?.takePersistableUriPermission(uri, flag)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDailyPlanResultListener()
    }

    private fun setDailyPlanResultListener() {
        setFragmentResultListener(
            requestKey = DailyPlanCreatedRequestKey
        ) { requestKey, bundle ->
            val dailyPlan =
                BundleCompat.getParcelable(bundle, DailyPlanCreatedKey, DailyPlan::class.java)
            dailyPlan?.let {
                viewModel.onDailyPlanCreated(it)
            }
        }
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
            val title = it?.toString() ?: ""
            viewModel.onTitleChanged(title)
        }

        binding.descriptionInput.setText(viewModel.getCurrentDescription())
        binding.descriptionInput.doAfterTextChanged {
            val description = it?.toString() ?: ""
            viewModel.onDescriptionChanged(description)
        }

        binding.addWorkoutCoverPhotoContainer.setOnClickListener {
            photoPicker.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }

        binding.publishFab.setOnClickListener {
            viewModel.onPublishClicked()
        }

        binding.rvDailyPlan.apply {
            verticalLinearLayoutManaged()
            adapter = workoutDailyPlanAdapter
        }
    }

    private fun subscribeObservers() {
        observeUIState()
        lifecycleScope.launch {
            viewModel.createWorkoutEvents
                .flowWithLifecycle(lifecycle)
                .collect {
                    when (it) {
                        CreateWorkoutEvent.WorkoutCreated -> showSuccessSnackbar(
                            container = binding.root,
                            message = getString(R.string.workout_created_successfully),
                            onDialogDismissed = { navController.popBackStack() }
                        )
                        CreateWorkoutEvent.WorkoutDescriptionEmpty -> showErrorSnackbar(
                            container = binding.root,
                            message = getString(R.string.workout_description_empty)
                        )
                        CreateWorkoutEvent.WorkoutNameEmpty -> showErrorSnackbar(
                            container = binding.root,
                            message = getString(R.string.workout_title_empty)
                        )
                        CreateWorkoutEvent.WorkoutPlanEmpty -> showErrorSnackbar(
                            container = binding.root,
                            message = getString(R.string.workout_plan_empty)
                        )
                    }
                }
        }
    }

    private fun observeUIState() {
        viewModel.workoutUIState.observe(viewLifecycleOwner) { uiState ->
            setCoverPhotoSection(uiState.workoutPhotoUri)
            workoutDailyPlanAdapter.submitList(uiState.planList)
        }
    }

    private fun setCoverPhotoSection(workoutPhotoUri: Uri?) {
        binding.addWorkoutCoverPhoto.isVisible = workoutPhotoUri != null
        if (workoutPhotoUri != null) {
            binding.addWorkoutCoverPhoto.load(workoutPhotoUri) {
                error(com.oguzhanaslann.commonui.R.drawable.placeholder_thumbnail)
            }
        }
    }
}
