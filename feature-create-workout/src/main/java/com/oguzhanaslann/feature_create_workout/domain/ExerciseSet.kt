package com.oguzhanaslann.feature_create_workout.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExerciseSet(
    val exercise: Exercise,
    val reps: Int? = null,
    val sets: Int? = null,
    val weight: Int? = null,
    val rest: Long? = null,
) : Parcelable
