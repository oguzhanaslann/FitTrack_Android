package com.oguzhanaslann.feature_create_workout.domain

import android.net.Uri

data class WorkoutToBeSaved(
    val name : String,
    val description: String,
    val coverPhoto : Uri?,
    val plan: List<DailyPlanToBeSaved>,
)
