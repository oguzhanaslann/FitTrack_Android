package com.oguzhanaslann.feature_create_workout.ui.createDailyPlan

data class ExerciseSetUIModel(
    val name: String = "",
    val imageUrl: String = "",
    val sets: Int = 0,
    val reps: Int = 0,
    val weight: Int = 0,
    val restMillis: Long = 0,
)
