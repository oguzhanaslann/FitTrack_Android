package com.oguzhanaslann.feature_home.domain.model

data class DailyWorkoutOverview(
    val id : String,
    val name : String,
    val calories : Int,
    val progress : Int,
    val imageUrl : String
)
