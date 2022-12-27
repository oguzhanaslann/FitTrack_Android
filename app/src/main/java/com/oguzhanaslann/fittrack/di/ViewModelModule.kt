package com.oguzhanaslann.fittrack.di

import android.content.Context
import com.oguzhanaslann.common_data.MemorySource
import com.oguzhanaslann.common_data.local.FitTrackDataStore
import com.oguzhanaslann.common_data.local.room.FitTrackDatabase
import com.oguzhanaslann.common_domain.AppLanguageUseCase
import com.oguzhanaslann.fittrack.data.AppInitRepository
import com.oguzhanaslann.fittrack.domain.usecase.InitializeAppUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideInitializeAppUseCase(
        fitTrackDataStore: FitTrackDataStore,
        appLanguageUseCase: AppLanguageUseCase,
        fitTrackDatabase: FitTrackDatabase,
        @ApplicationContext context: Context,
    ) = InitializeAppUseCase(
        appInitRepository = AppInitRepository(fitTrackDataStore),
        appLanguageUseCase = appLanguageUseCase,
        fitTrackDatabase = fitTrackDatabase,
        context = context
    )

    @Provides
    @ViewModelScoped
    fun provideAppInitRepository(
        memorySource: MemorySource,
    ) = AppLanguageUseCase(memorySource)
}
