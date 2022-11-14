package com.oguzhanaslann.feature_profile.di

import com.oguzhanaslann.feature_profile.domain.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideProfileRepository(): ProfileRepository {
        return ProfileRepository()
    }

}
