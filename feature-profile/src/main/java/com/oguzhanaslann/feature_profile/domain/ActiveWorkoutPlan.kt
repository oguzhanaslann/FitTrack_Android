package com.oguzhanaslann.feature_profile.domain

data class ActiveWorkoutPlan(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val progress: Float
)
