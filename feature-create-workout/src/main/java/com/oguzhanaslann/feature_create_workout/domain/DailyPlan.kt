package com.oguzhanaslann.feature_create_workout.domain

data class DailyPlan(
    val name: String,
    val imageUrl: String?,
    val calories: Int,
    val exerciseList: List<ExerciseSet>,
)
