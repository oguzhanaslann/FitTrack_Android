package com.oguzhanaslann.feature_workouts.ui.workoutDetail

data class WorkoutDetail(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val calories: Int,
    val description: String,
    val programs: List<DailyPlanShort>,
    val tags: List<String>
)
