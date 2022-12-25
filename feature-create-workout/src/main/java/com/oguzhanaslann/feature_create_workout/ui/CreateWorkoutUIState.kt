package com.oguzhanaslann.feature_create_workout.ui

import android.net.Uri

data class CreateWorkoutUIState(
    val name: String = "",
    val description: String = "",
    val workoutPhotoUri: Uri? = null,
    val planList: List<DailyPlanUIModel> = emptyList(),
)
