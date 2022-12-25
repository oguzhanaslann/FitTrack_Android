package com.oguzhanaslann.fittrack.domain.usecase

import androidx.appcompat.app.AppCompatDelegate
import com.oguzhanaslann.common_data.MemorySource
import com.oguzhanaslann.common_domain.AppLanguage
import com.oguzhanaslann.common_domain.AppLanguageUseCase
import com.oguzhanaslann.fittrack.data.AppInitRepository
import com.oguzhanaslann.fittrack.domain.model.AppInitialization
import java.util.*

class InitializeAppUseCase(
    private val appInitRepository: AppInitRepository,
    private val appLanguageUseCase: AppLanguageUseCase
) {

    suspend operator fun invoke(): AppInitialization {
        val hasSeenOnboarding = appInitRepository.hasSeenOnboarding()
        val hasAuthenticated = appInitRepository.hasAuthenticated()

        val currentApplicationLanguage = getCurrentApplicationLanguage()
        appLanguageUseCase.saveAppLanguageIntoMemory(currentApplicationLanguage)

        return AppInitialization(
            hasSeenOnboarding = hasSeenOnboarding,
            hasAuthenticated = hasAuthenticated
        )
    }

    private fun getCurrentApplicationLanguage(): AppLanguage {
        val appLocales = AppCompatDelegate.getApplicationLocales()

        val systemLanguage = Locale.getDefault().language
        return when {
            appLocales.isEmpty -> AppLanguage.fromLanguage(systemLanguage)
            else -> AppLanguage.fromLanguage(appLocales[0]!!.language)
        }
    }
}
