package com.oguzhanaslann.domain_profile.domain.model

import com.oguzhanaslann.common.Height
import com.oguzhanaslann.common.Weight
import java.util.*

data class UserProfile(
    val id: Int,
    val name: String,
    val surname : String,
    val profilePhotoUrl: String?,
    val weight : Weight,
    val height : Height,
    val birthDate: Date?
) {
    val fullName
        get() = "$name $surname"
}
