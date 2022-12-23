package com.oguzhanaslann.domain_profile.domain.model

data class ActiveWorkoutPlan(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val progress: Float
)
