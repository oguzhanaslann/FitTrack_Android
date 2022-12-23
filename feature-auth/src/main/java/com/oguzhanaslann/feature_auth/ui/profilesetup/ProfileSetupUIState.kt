package com.oguzhanaslann.feature_auth.ui.profilesetup

import android.net.Uri
import java.util.*

data class ProfileSetupUIState(
    val name: String = "",
    val surname: String = "",
    val birthdate: Date? = null,
    val heightInCm: Int = 0,
    val weightInKg: Int = 0,
    val profilePhotoUri: Uri? = null,
)
