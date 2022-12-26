package com.oguzhanaslann.feature_create_workout.domain.usecase

import android.util.Log
import com.oguzhanaslann.common_domain.AppLanguageUseCase
import com.oguzhanaslann.feature_create_workout.domain.Exercise
import com.oguzhanaslann.feature_create_workout.data.CreateWorkoutRepository
import kotlinx.coroutines.flow.Flow

class CreateWorkoutUseCase(
    private val appLanguageUseCase: AppLanguageUseCase,
    private val repository: CreateWorkoutRepository,
) {
    suspend fun getExercises(first : Int): Flow<List<Exercise>> {
        val appLanguageCode = appLanguageUseCase.getAppLanguageCode()
        Log.e("TAG", "getExercises: $appLanguageCode")
        return repository.getExercises(first, appLanguageCode)
    }

    suspend fun searchExercise(query: String): Flow<List<Exercise>> {
        val appLanguageCode = appLanguageUseCase.getAppLanguageCode()
        return repository.searchExercise(query, appLanguageCode)
    }
}
