package com.oguzhanaslann.feature_home.ui.trace

import com.oguzhanaslann.feature_home.domain.model.TraceExercise

data class TraceWorkoutViewState(
    val name: String,
    val traceExercises: List<TraceExercise> = emptyList(),
)
