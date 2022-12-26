package com.oguzhanaslann.feature_create_workout.domain.usecase

import android.util.Log
import com.oguzhanaslann.common_domain.AppLanguageUseCase
import com.oguzhanaslann.feature_create_workout.data.CreateWorkoutRepository
import com.oguzhanaslann.feature_create_workout.domain.DailyPlan
import com.oguzhanaslann.feature_create_workout.domain.Exercise
import com.oguzhanaslann.feature_create_workout.domain.Workout
import com.oguzhanaslann.feature_create_workout.domain.WorkoutToBeSaved
import kotlinx.coroutines.flow.Flow

class CreateWorkoutUseCase(
    private val appLanguageUseCase: AppLanguageUseCase,
    private val repository: CreateWorkoutRepository,
    private val workoutLocalPhotosUseCase: WorkoutLocalPhotosUseCase,
) {
    suspend fun getExercises(first: Int): Flow<List<Exercise>> {
        val appLanguageCode = getAppLanguageCode()
        Log.e("TAG", "getExercises: $appLanguageCode")
        return repository.getExercises(first, appLanguageCode)
    }

    suspend fun searchExercise(query: String): Flow<List<Exercise>> {
        val appLanguageCode = getAppLanguageCode()
        return repository.searchExercise(query, appLanguageCode)
    }

    private fun getAppLanguageCode(): String {
        return appLanguageUseCase.getAppLanguageCode()
    }

    suspend fun saveWorkout(
        workoutToBeSaved: WorkoutToBeSaved,
    ) {

        val appLanguageCode = getAppLanguageCode()

        val coverPhotoUrl = workoutToBeSaved.coverPhoto?.let {
            workoutLocalPhotosUseCase.saveWorkoutPhotoAndGetUrl(
                workoutName = workoutToBeSaved.name,
                uri = it
            )
        } ?: ""

        val workout = Workout(
            name = workoutToBeSaved.name,
            imageUrl = coverPhotoUrl,
            description = workoutToBeSaved.description,
            plans = workoutToBeSaved.plan.map {
                val dailyPlanPhotoUrl = it.imageUri?.let { photoUri ->
                    workoutLocalPhotosUseCase.saveDailyPlanPhotoAndGetUrl(
                        workoutName = workoutToBeSaved.name,
                        dailyPlanName = it.name,
                        uri = photoUri
                    )
                }

                DailyPlan(
                    name = it.name,
                    imageUrl = dailyPlanPhotoUrl,
                    calories = it.calories,
                    exerciseList = it.exerciseList
                )
            }
        )
        repository.saveWorkout(workout, appLanguageCode)
    }
}
