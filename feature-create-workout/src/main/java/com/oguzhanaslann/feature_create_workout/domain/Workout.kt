package com.oguzhanaslann.feature_create_workout.domain

data class Workout(
    val name: String,
    val imageUrl: String,
    val description: String,
    val plans: List<DailyPlan>,
)
