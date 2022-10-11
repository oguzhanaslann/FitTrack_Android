package com.oguzhanaslann.feature_onboard.domain

import com.oguzhanaslann.common.State

data class OnboardUIState(
    val currentPage: Int = 0,
    val markAsSeenState: State<Boolean>
) {
    companion object {
        fun initial() = OnboardUIState(markAsSeenState = State.Initial)
    }
}
