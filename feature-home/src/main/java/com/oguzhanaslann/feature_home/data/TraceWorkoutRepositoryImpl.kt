package com.oguzhanaslann.feature_home.data

import com.oguzhanaslann.common.DateHelper
import com.oguzhanaslann.common_data.local.room.dao.UserDailyPlanDao
import com.oguzhanaslann.common_data.local.room.dao.UserExerciseDao
import com.oguzhanaslann.common_data.local.room.entity.UserExerciseEntity
import com.oguzhanaslann.common_domain.DailyPlan
import com.oguzhanaslann.common_domain.Exercise
import com.oguzhanaslann.common_domain.ExerciseSet
import com.oguzhanaslann.feature_home.domain.model.TraceExercise
import java.util.*

class TraceWorkoutRepositoryImpl(
    private val userDailyPlanDao: UserDailyPlanDao,
    private val userExerciseDao: UserExerciseDao,
) : TraceWorkoutRepository {
    override suspend fun getDailyPlan(id: String): DailyPlan? {
        val userDailyPlan = userDailyPlanDao.getUserDailyPlanWithExercises(id)
        return userDailyPlan?.let {
            DailyPlan(
                id = it.userDailyPlanEntity.id,
                name = it.userDailyPlanEntity.name,
                imageUrl = it.userDailyPlanEntity.imageUrl,
                calories = it.userDailyPlanEntity.calories,
                exerciseList = it.exercises.map { exercise ->
                    ExerciseSet(
                        exercise = Exercise(
                            id = exercise.id,
                            name = exercise.name,
                            description = exercise.description,
                            imageUrl = exercise.imageUrl,
                        ),
                        reps = exercise.reps,
                        sets = exercise.set,
                    )
                }
            )
        }
    }

    override suspend fun doneWorkout(id: String, traceExercises: List<TraceExercise>) {
        val userDailyPlan = userDailyPlanDao.getUserDailyPlanWithExercises(id)
        userDailyPlan?.let {
            val userExercises = it.exercises
            val updatedExercises: List<UserExerciseEntity> = userExercises.map { userExercise ->
                val tracedExercise =
                    traceExercises.find { traceExercise -> traceExercise.exerciseSet.exercise.id == userExercise.id }

                tracedExercise?.let {
                    userExercise.copy(
                        isCompleted = it.isChecked,
                    )
                } ?: userExercise
            }

            userExerciseDao.updateAll(*updatedExercises.toTypedArray())

            val now = Date()
            val staterTimeOfNow = DateHelper.getStartDateOf(now)
            val userDailyPlanEntity = it.userDailyPlanEntity.copy(
                endDate =staterTimeOfNow.time,
                isCompleted = true,
                isActive = false,
            )

            userDailyPlanDao.update(userDailyPlanEntity)
        }
    }
}
