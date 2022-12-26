package com.oguzhanaslann.feature_create_workout.domain

import android.net.Uri
import android.os.Parcelable
import com.oguzhanaslann.common_domain.ExerciseSet
import kotlinx.parcelize.Parcelize

@Parcelize
data class DailyPlanToBeSaved(
    val name: String,
    val imageUri: Uri?,
    val calories: Int,
    val exerciseList: List<ExerciseSet>,
): Parcelable
