package com.oguzhanaslann.feature_reports.domain

data class ReportExercise(
    val id: Int,
    val name: String,
    val description: String,
    val image: String,
    val isDone: Boolean,
)
