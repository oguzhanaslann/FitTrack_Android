package com.oguzhanaslann.feature_create_workout.domain

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DailyPlan(
    val name: String,
    val imageUrl: Uri?,
    val calories: Int,
    val exerciseList: List<ExerciseSet>,
): Parcelable
