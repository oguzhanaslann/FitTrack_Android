package com.oguzhanaslann.domain_profile.domain.model

import android.net.Uri
import java.util.*

data class UserProfileEdit(
    val name: String = "",
    val surname: String = "",
    val birthdate: Date? = null,
    val heightInCm: Int = 0,
    val weightInKg: Int = 0,
    val profilePhotoUri: Uri? = null,
)
