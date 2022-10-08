package com.oguzhanaslann.fittrack.domain.usecase

import com.oguzhanaslann.fittrack.data.AppInitRepository
import com.oguzhanaslann.fittrack.domain.model.AppInitialization

class InitializeAppUseCase(
    private val appInitRepository: AppInitRepository
) {

    suspend operator fun invoke(): AppInitialization {
        val hasSeenOnboarding = appInitRepository.hasSeenOnboarding()
        val hasAuthenticated = appInitRepository.hasAuthenticated()
        return AppInitialization(
            hasSeenOnboarding = hasSeenOnboarding,
            hasAuthenticated = hasAuthenticated
        )
    }

}
