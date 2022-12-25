package com.oguzhanaslann.feature_onboard.di

import com.oguzhanaslann.common_data.local.FitTrackDataStore
import com.oguzhanaslann.feature_onboard.data.OnboardingRepository
import com.oguzhanaslann.feature_onboard.data.local.OnboardingLocalSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object OnboardingModule {

    @Provides
    @ViewModelScoped
    fun provideOnboardingRepository(
        fitTrackDataStore: com.oguzhanaslann.common_data.local.FitTrackDataStore
    ) : OnboardingRepository {
        return OnboardingRepository(
            localSource = OnboardingLocalSource(fitTrackDataStore)
        )
    }
}
