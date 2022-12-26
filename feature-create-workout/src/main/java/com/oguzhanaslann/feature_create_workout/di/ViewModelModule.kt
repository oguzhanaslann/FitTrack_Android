package com.oguzhanaslann.feature_create_workout.di

import com.oguzhanaslann.common_data.local.room.FitTrackDatabase
import com.oguzhanaslann.common_domain.AppLanguageUseCase
import com.oguzhanaslann.feature_create_workout.data.CreateWorkoutRepository
import com.oguzhanaslann.feature_create_workout.data.CreateWorkoutRepositoryImpl
import com.oguzhanaslann.feature_create_workout.domain.mapper.ExerciseMapper
import com.oguzhanaslann.feature_create_workout.domain.usecase.CreateWorkoutUseCase
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
    fun provideCreateWorkoutRepository(
        fitTrackDatabase: FitTrackDatabase
    ): CreateWorkoutRepository = CreateWorkoutRepositoryImpl(
        exerciseDao = fitTrackDatabase.exerciseDao(),
        exerciseMapper = ExerciseMapper()
    )

    @Provides
    @ViewModelScoped
    fun provideCreateWorkoutUseCase(
        appLanguageUseCase : AppLanguageUseCase,
        repository: CreateWorkoutRepository
    ): CreateWorkoutUseCase = CreateWorkoutUseCase(
        appLanguageUseCase = appLanguageUseCase,
        repository = repository
    )
}
