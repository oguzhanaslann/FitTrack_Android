package com.oguzhanaslann.feature_workouts.ui.workoutDetail

import com.oguzhanaslann.common_domain.DailyPlan

data class WorkoutDetail(
    val id: String,
    val name: String,
    val imageUrl: String,
    val calories: Int,
    val description: String,
    val programs: List<DailyPlan>,
    val tags: List<String>,
)
