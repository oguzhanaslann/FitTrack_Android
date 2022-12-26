package com.oguzhanaslann.feature_create_workout.di

import android.content.Context
import com.oguzhanaslann.common_data.local.room.FitTrackDatabase
import com.oguzhanaslann.common_domain.AppLanguageUseCase
import com.oguzhanaslann.feature_create_workout.data.CreateWorkoutRepository
import com.oguzhanaslann.feature_create_workout.data.CreateWorkoutRepositoryImpl
import com.oguzhanaslann.feature_create_workout.domain.mapper.ExerciseMapper
import com.oguzhanaslann.feature_create_workout.domain.usecase.CreateWorkoutUseCase
import com.oguzhanaslann.feature_create_workout.domain.usecase.WorkoutLocalPhotosUseCase
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
    fun provideCreateWorkoutRepository(
        fitTrackDatabase: FitTrackDatabase,
    ): CreateWorkoutRepository = CreateWorkoutRepositoryImpl(
        exerciseDao = fitTrackDatabase.exerciseDao(),
        exerciseMapper = ExerciseMapper(),
        workoutPlanDao = fitTrackDatabase.workoutPlanDao(),
        dailyPlanDao = fitTrackDatabase.dailyPlanDao(),
        dailyPlanExercise = fitTrackDatabase.dailyPlanExerciseDao()
    )

    @Provides
    @ViewModelScoped
    fun provideCreateWorkoutUseCase(
        appLanguageUseCase: AppLanguageUseCase,
        repository: CreateWorkoutRepository,
        workoutLocalPhotosUseCase: WorkoutLocalPhotosUseCase,
    ): CreateWorkoutUseCase = CreateWorkoutUseCase(
        appLanguageUseCase = appLanguageUseCase,
        repository = repository,
        workoutLocalPhotosUseCase = workoutLocalPhotosUseCase
    )

    @Provides
    @ViewModelScoped
    fun provideWorkoutLocalPhotosUseCase(
        @ApplicationContext context: Context,
    ): WorkoutLocalPhotosUseCase = WorkoutLocalPhotosUseCase(
        applicationContext = context
    )
}
