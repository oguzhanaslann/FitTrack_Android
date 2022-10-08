package com.oguzhanaslann.feature_onboard.data.local

import com.oguzhanaslann.common.data.local.FitTrackDataStore

interface OnboardingLocalSource {
    suspend fun markOnboardingAsFinished(): Result<Boolean>
}

fun OnboardingLocalSource(
    dataStore : FitTrackDataStore
) : OnboardingLocalSource {
    return object : OnboardingLocalSource {
        override suspend fun markOnboardingAsFinished(): Result<Boolean> {
            return dataStore.setUserSeenOnboarding(true).map { true }
        }
    }
}
