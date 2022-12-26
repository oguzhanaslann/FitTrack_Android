package com.oguzhanaslann.common_domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Exercise(
    val id : String,
    val name: String,
    val description: String,
    val imageUrl: String,
) : Parcelable
