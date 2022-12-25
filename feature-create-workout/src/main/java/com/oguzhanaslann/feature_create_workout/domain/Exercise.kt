package com.oguzhanaslann.feature_create_workout.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Exercise(
    val name: String,
    val description: String,
    val imageUrl: String,
) : Parcelable
