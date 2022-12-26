package com.oguzhanaslann.feature_create_workout.ui.createDailyPlan

import android.net.Uri

data class CreateDailyPlanUIState(
    val name: String = "",
    val dailyPlanPhotoUri: Uri? = null,
    val exercises: List<ExerciseSetUIModel> = emptyList(),
    val calories: Int = 0,
)
