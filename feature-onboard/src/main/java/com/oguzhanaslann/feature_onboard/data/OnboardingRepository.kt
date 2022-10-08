package com.oguzhanaslann.feature_onboard.data

import com.oguzhanaslann.feature_onboard.data.local.OnboardingLocalSource

interface OnboardingRepository {
    suspend fun markOnboardingAsFinished(): Result<Boolean>
}

fun OnboardingRepository(
    localSource: OnboardingLocalSource
) : OnboardingRepository {
    return object : OnboardingRepository {
        override suspend fun markOnboardingAsFinished(): Result<Boolean> {
            return localSource.markOnboardingAsFinished()
        }
    }
}
