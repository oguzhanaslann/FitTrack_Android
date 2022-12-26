package com.oguzhanaslann.common_domain

data class Workout(
    val id : String,
    val name: String,
    val imageUrl: String,
    val description: String,
    val plans: List<DailyPlan>,
)
