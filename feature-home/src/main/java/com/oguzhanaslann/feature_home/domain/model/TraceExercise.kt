package com.oguzhanaslann.feature_home.domain.model

import com.oguzhanaslann.common_domain.ExerciseSet

data class TraceExercise(
    val exerciseSet: ExerciseSet,
    val isChecked: Boolean,
)
