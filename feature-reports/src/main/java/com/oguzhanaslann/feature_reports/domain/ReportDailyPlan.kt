package com.oguzhanaslann.feature_reports.domain

data class ReportDailyPlan(
    val name: String,
    val exercises: List<ReportExercise>,
)
