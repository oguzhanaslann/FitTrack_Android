package com.oguzhanaslann.feature_home.ui

import com.oguzhanaslann.feature_home.domain.model.ProfileShortInfo

data class ProfileShortInfoUIState(
    val profileShortInfo: ProfileShortInfo,
    val currentDateText: String,
)
