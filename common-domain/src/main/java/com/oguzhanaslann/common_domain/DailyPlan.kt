package com.oguzhanaslann.common_domain

data class DailyPlan(
    val id : String,
    val name: String,
    val imageUrl: String?,
    val calories: Int,
    val exerciseList: List<ExerciseSet>,
)
