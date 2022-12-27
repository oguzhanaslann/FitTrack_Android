package com.oguzhanaslann.feature_home.ui

import com.oguzhanaslann.feature_home.domain.model.DailyWorkoutOverview

data class HomepageUIState(
    val profileShortInfo: ProfileShortInfoUIState?,
    val todaysWorkoutOverview : DailyWorkoutOverview?,
) {
    companion object {
        fun initial() = HomepageUIState(
            profileShortInfo = null,
            todaysWorkoutOverview = null,
        )
    }
}
