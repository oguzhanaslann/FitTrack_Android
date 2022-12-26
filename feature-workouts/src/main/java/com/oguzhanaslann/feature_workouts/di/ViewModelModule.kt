package com.oguzhanaslann.feature_workouts.di

import com.oguzhanaslann.common_data.local.FitTrackDataStore
import com.oguzhanaslann.common_data.local.room.FitTrackDatabase
import com.oguzhanaslann.common_domain.AppLanguageUseCase
import com.oguzhanaslann.feature_workouts.domain.mapper.DailyPlanShortMapper
import com.oguzhanaslann.feature_workouts.domain.mapper.WorkoutDetailMapper
import com.oguzhanaslann.feature_workouts.domain.mapper.WorkoutFromEntityMapper
import com.oguzhanaslann.feature_workouts.domain.repository.WorkoutsRepository
import com.oguzhanaslann.feature_workouts.domain.repository.WorkoutsRepositoryImpl
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
    fun provideWorkoutsRepository(
        fitTrackDatabase: FitTrackDatabase,
        fitTrackDataStore: FitTrackDataStore,
        appLanguageUseCase: AppLanguageUseCase,
    ): WorkoutsRepository = WorkoutsRepositoryImpl(
        workoutPlanDao = fitTrackDatabase.workoutPlanDao(),
        workoutFromEntityMapper = WorkoutFromEntityMapper(),
        workoutDetailMapper = WorkoutDetailMapper(
            dailyPlanShortMapper = DailyPlanShortMapper()
        ),
        appLanguageUseCase = appLanguageUseCase,
        userDao = fitTrackDatabase.userDao(),
        fitTrackDataStore = fitTrackDataStore,
        userWorkoutPlanDao = fitTrackDatabase.userWorkoutPlanDao(),
        userDailyPlanDao = fitTrackDatabase.userDailyPlanDao(),
        userExerciseDao = fitTrackDatabase.userExerciseDao()
    )
}
