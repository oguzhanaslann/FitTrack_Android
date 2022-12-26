package com.oguzhanaslann.feature_create_workout.data

import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.common.mapBy
import com.oguzhanaslann.common_data.local.room.dao.DailyPlanDao
import com.oguzhanaslann.common_data.local.room.dao.DailyPlanExerciseCrossRefDao
import com.oguzhanaslann.common_data.local.room.dao.ExerciseDao
import com.oguzhanaslann.common_data.local.room.dao.WorkoutPlanDao
import com.oguzhanaslann.common_data.local.room.entity.DailyPlanEntity
import com.oguzhanaslann.common_data.local.room.entity.DailyPlanExercise
import com.oguzhanaslann.common_data.local.room.entity.ExerciseEntity
import com.oguzhanaslann.common_data.local.room.entity.ExerciseSet
import com.oguzhanaslann.common_data.local.room.entity.WorkoutPlanEntity
import com.oguzhanaslann.common_domain.Exercise
import com.oguzhanaslann.common_domain.Workout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.*

interface CreateWorkoutRepository {
    suspend fun getExercises(first: Int, appLanguageCode: String): Flow<List<Exercise>>
    suspend fun searchExercise(query: String, appLanguageCode: String): Flow<List<Exercise>>
    suspend fun saveWorkout(workout: Workout, appLanguageCode: String)
}

class CreateWorkoutRepositoryImpl(
    private val exerciseDao: ExerciseDao,
    private val workoutPlanDao: WorkoutPlanDao,
    private val dailyPlanDao: DailyPlanDao,
    private val dailyPlanExercise: DailyPlanExerciseCrossRefDao,
    private val exerciseMapper: Mapper<ExerciseEntity, Exercise>,
) : CreateWorkoutRepository {
    override suspend fun getExercises(first: Int, appLanguageCode: String): Flow<List<Exercise>> {
        return flow {
            val mappedResultFlow = exerciseDao.getExercises(first, appLanguageCode).map {
                it.mapBy(exerciseMapper)
            }

            emitAll(mappedResultFlow)
        }
    }

    override suspend fun searchExercise(
        query: String,
        appLanguageCode: String,
    ): Flow<List<Exercise>> {
        return flow {
            val mappedResultFlow = exerciseDao.searchExercises(query, appLanguageCode).map {
                it.mapBy(exerciseMapper)
            }

            emitAll(mappedResultFlow)
        }
    }

    override suspend fun saveWorkout(workout: Workout, appLanguageCode: String) {
        val workoutEntity = WorkoutPlanEntity(
            id = UUID.randomUUID().toString(),
            name = workout.name,
            imageUrl = workout.imageUrl,
            description = workout.description,
            languageCode = appLanguageCode
        )

        workoutPlanDao.insert(workoutEntity)

        val dailyPlanEntities = workout.plans.map {
            DailyPlanEntity(
                id = UUID.randomUUID().toString(),
                workoutPlanId = workoutEntity.id,
                name = it.name,
                imageUrl = it.imageUrl ?: "",
                calories = it.calories,
                languageCode = appLanguageCode
            )
        }

        dailyPlanDao.insertAll(*dailyPlanEntities.toTypedArray())

        val exerciseRelations = workout.plans.flatMapIndexed { index, dailyPlan ->
            dailyPlan.exerciseList.mapIndexed { exerciseIndex, exercise ->
                DailyPlanExercise(
                    dailyPlanId = dailyPlanEntities[index].id,
                    exerciseId = exercise.exercise.id,
                    exerciseSet = ExerciseSet(
                        reps = exercise.reps,
                        weight = exercise.weight,
                        order = exerciseIndex,
                        set = exercise.sets,
                        rest = exercise.rest
                    )
                )
            }
        }

        dailyPlanExercise.insertAll(*exerciseRelations.toTypedArray())
    }
}
