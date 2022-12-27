package com.oguzhanaslann.feature_home.data

import com.oguzhanaslann.common_data.local.FitTrackDataStore
import com.oguzhanaslann.common_data.local.room.dao.UserDao
import com.oguzhanaslann.common_data.local.room.dao.UserWorkoutPlanDao
import com.oguzhanaslann.common_data.local.room.entity.UserExerciseEntity
import com.oguzhanaslann.feature_home.domain.model.DailyWorkoutOverview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class HomepageRepositoryImpl(
    private val userDao: UserDao,
    private val workoutPlanDao: UserWorkoutPlanDao,
    private val fitTrackDataStore: FitTrackDataStore,
) : HomepageRepository {
    override suspend fun getTodaysWorkoutOverview(): Flow<DailyWorkoutOverview?> {
        val activeWorkoutId = userDao.getUserActiveWorkoutPlanId(getUserID())
        return flow {
            val resultFlow = activeWorkoutId?.let {
                val workoutWithWhole = workoutPlanDao.getUserWorkoutWithDailyPlansAndExercises(it)
                workoutWithWhole.map { userWorkout ->
                    val dailyPlans = userWorkout?.dailyPlans ?: return@map null
                    val dailyWorkout = dailyPlans
                        .sortedBy { it.userDailyPlanEntity.order }
                        .find { it.userDailyPlanEntity.isActive && it.userDailyPlanEntity.isCompleted.not() }

                    dailyWorkout?.let {
                        DailyWorkoutOverview(
                            id = dailyWorkout.userDailyPlanEntity.id,
                            name = dailyWorkout.userDailyPlanEntity.name,
                            calories = dailyWorkout.userDailyPlanEntity.calories,
                            progress = getProgress(dailyWorkout.exercises),
                            imageUrl = dailyWorkout.userDailyPlanEntity.imageUrl
                        )
                    }
                }
            }


            resultFlow?.let { emitAll(it) } ?: emit(null)
        }
    }

    private fun getProgress(exercises: List<UserExerciseEntity>): Int {
        return exercises.filter { it.isCompleted }.size * 100 / exercises.size
    }

    private suspend fun getUserID() = fitTrackDataStore.getUserId().first()
}
