package com.oguzhanaslann.feature_create_workout.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.oguzhanaslann.commonui.verticalLinearLayoutManaged
import com.oguzhanaslann.commonui.viewBinding
import com.oguzhanaslann.feature_create_workout.R
import com.oguzhanaslann.feature_create_workout.databinding.FragmentCreateWorkoutBinding
import dagger.hilt.android.AndroidEntryPoint

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
        // TODO("Not yet implemented")
    }

    private val photoPicker =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri ?: return@registerForActivityResult
            viewModel.onPhotoSelected(uri)
            val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
            context?.contentResolver?.takePersistableUriPermission(uri, flag)
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
        viewModel.workoutUIState.observe(viewLifecycleOwner) { uiState ->
            setCoverPhotoSection(uiState.workoutPhotoUri)
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
