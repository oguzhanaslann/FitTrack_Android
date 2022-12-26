package com.oguzhanaslann.feature_workouts.domain.repository

import android.util.Log
import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.common.mapBy
import com.oguzhanaslann.common_data.local.FitTrackDataStore
import com.oguzhanaslann.common_data.local.room.dao.UserDailyPlanDao
import com.oguzhanaslann.common_data.local.room.dao.UserDao
import com.oguzhanaslann.common_data.local.room.dao.UserExerciseDao
import com.oguzhanaslann.common_data.local.room.dao.UserWorkoutPlanDao
import com.oguzhanaslann.common_data.local.room.dao.WorkoutPlanDao
import com.oguzhanaslann.common_data.local.room.entity.UserDailyPlanEntity
import com.oguzhanaslann.common_data.local.room.entity.UserExerciseEntity
import com.oguzhanaslann.common_data.local.room.entity.UserWorkoutPlanEntity
import com.oguzhanaslann.common_data.local.room.entity.WorkoutPlanDetail
import com.oguzhanaslann.common_data.local.room.entity.WorkoutPlanEntity
import com.oguzhanaslann.common_domain.AppLanguageUseCase
import com.oguzhanaslann.common_domain.Workout
import com.oguzhanaslann.feature_workouts.ui.workoutDetail.WorkoutDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.*

interface WorkoutsRepository {
    fun getWorkouts(first: Int): Flow<List<Workout>>
    fun searchWorkouts(query: String): Flow<List<Workout>>
    fun getWorkoutDetail(id: String): Flow<WorkoutDetail>
    suspend fun getIsActiveWorkout(id: String): Boolean
    suspend fun getHasUserActiveWorkout(): Boolean
    suspend fun startWorkout(workoutDetail: WorkoutDetail)
}

class WorkoutsRepositoryImpl(
    private val userDao: UserDao,
    private val userWorkoutPlanDao: UserWorkoutPlanDao,
    private val userDailyPlanDao: UserDailyPlanDao,
    private val userExerciseDao: UserExerciseDao,
    private val workoutPlanDao: WorkoutPlanDao,
    private val appLanguageUseCase: AppLanguageUseCase,
    private val fitTrackDataStore: FitTrackDataStore,
    private val workoutFromEntityMapper: Mapper<WorkoutPlanEntity, Workout>,
    private val workoutDetailMapper: Mapper<WorkoutPlanDetail, WorkoutDetail>,
) : WorkoutsRepository {
    override fun getWorkouts(first: Int): Flow<List<Workout>> {
        return flow {
            val workPlanEntities = workoutPlanDao.getWorkoutPlans(
                first = first,
                languageCode = getAppLanguage()
            )
            emitAll(
                workPlanEntities.map {
                    it.mapBy(workoutFromEntityMapper)
                }
            )
        }
    }

    // TODO: Add pagination later on
    override fun searchWorkouts(query: String): Flow<List<Workout>> {
        return flow {
            val workPlanEntities = workoutPlanDao.searchWorkoutPlanByName(
                query = query,
                languageCode = getAppLanguage()
            )
            emitAll(
                workPlanEntities.map {
                    it.mapBy(workoutFromEntityMapper)
                }
            )
        }
    }

    private fun getAppLanguage() = appLanguageUseCase.getAppLanguageCode()

    override fun getWorkoutDetail(id: String): Flow<WorkoutDetail> {
        return flow {
            val workoutPlanEntity = workoutPlanDao.getWorkoutPlanDetailStream(id = id)
            emitAll(
                workoutPlanEntity
                    .filterNotNull()
                    .map { workoutDetailMapper.map(it) }
            )
        }
    }

    override suspend fun getIsActiveWorkout(id: String): Boolean {
        val userActiveWorkoutId = getUserActiveWorkoutId()
        return id == userActiveWorkoutId
    }

    private suspend fun getUserId(): Int {
        return fitTrackDataStore.getUserId().first()
    }

    override suspend fun getHasUserActiveWorkout(): Boolean {
        val userActiveWorkoutId = getUserActiveWorkoutId()
        return userActiveWorkoutId != null
    }

    private suspend fun getUserActiveWorkoutId(): String? {
        val userId = getUserId()
        return userDao.getUserActiveWorkoutPlanId(userId)
    }

    override suspend fun startWorkout(workoutDetail: WorkoutDetail) {
        val hasUserActiveWorkout = getHasUserActiveWorkout()
        if (hasUserActiveWorkout) {
            finishUserActiveWorkout()
        }

        val userId = getUserId()
        val user = userDao.getUserByIdSuspend(userId)
        user?.let {
            val workoutPlanEntity = UserWorkoutPlanEntity(
                id = workoutDetail.id,
                name = workoutDetail.name,
                imageUrl = workoutDetail.imageUrl,
                description = workoutDetail.description,
                userId = userId,
                startDate = Date().time,
                endDate = 0,
                isCompleted = false,
                isActive = true,
                languageCode = getAppLanguage()
            )

            userWorkoutPlanDao.insert(workoutPlanEntity)

            val dailyPlans = workoutDetail.programs.mapIndexed { index, dailyPlan ->
                UserDailyPlanEntity(
                    id = dailyPlan.id,
                    activeWorkoutPlanId = workoutDetail.id,
                    name = dailyPlan.name,
                    order = index,
                    userId = userId,
                    imageUrl = dailyPlan.imageUrl.orEmpty(),
                    startDate = Date().time,
                    endDate = 0,
                    isCompleted = false,
                    isActive = true,
                    languageCode = getAppLanguage(),
                    description = ""
                )
            }

            userDailyPlanDao.insertAll(*dailyPlans.toTypedArray())

            val exercises = workoutDetail.programs.flatMap { dailyPlan ->
                dailyPlan.exerciseList.mapIndexed { index, exercise ->
                    UserExerciseEntity(
                        id = exercise.exercise.id,
                        activeDailyPlanId = dailyPlan.id,
                        name = exercise.exercise.name,
                        imageUrl = exercise.exercise.imageUrl,
                        description = exercise.exercise.description,
                        isCompleted = false,
                        languageCode = getAppLanguage(),
                    )
                }
            }

            userExerciseDao.insertAll(*exercises.toTypedArray())

            val updatedUser = it.copy(
                activeWorkoutPlanId = workoutPlanEntity.id
            )
            updatedUser.id = userId
            Log.e("TAG", "startWorkout: $updatedUser")
            userDao.update(updatedUser)
        }
    }

    private suspend fun finishUserActiveWorkout() {
        val userId = getUserId()
        val user = userDao.getUserByIdSuspend(userId)
        user?.let {
            val activeWorkoutPlanId = it.activeWorkoutPlanId ?: return
            val activeWorkoutPlan = userWorkoutPlanDao.getUserWorkoutPlanById(activeWorkoutPlanId)
            activeWorkoutPlan?.let {
                val updatedWorkoutPlan = it.copy(
                    isActive = false,
                    isCompleted = true,
                    endDate = Date().time
                )
                userWorkoutPlanDao.update(updatedWorkoutPlan)
            }
        }
    }
}
