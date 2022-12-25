package com.oguzhanaslann.feature_workouts.domain.repository

import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.common.mapBy
import com.oguzhanaslann.commonui.data.local.room.dao.WorkoutPlanDao
import com.oguzhanaslann.commonui.data.local.room.entity.WorkoutPlanDetail
import com.oguzhanaslann.commonui.data.local.room.entity.WorkoutPlanEntity
import com.oguzhanaslann.feature_workouts.domain.model.Workout
import com.oguzhanaslann.feature_workouts.ui.workoutDetail.WorkoutDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

interface WorkoutsRepository {
    fun getWorkouts(first: Int): Flow<List<Workout>>
    fun searchWorkouts(query: String): Flow<List<Workout>>
    fun getWorkoutDetail(id: Int): Flow<WorkoutDetail>
}

class WorkoutsRepositoryImpl(
    private val workoutPlanDao: WorkoutPlanDao,
    private val workoutFromEntityMapper: Mapper<WorkoutPlanEntity, Workout>,
    private val workoutDetailMapper: Mapper<WorkoutPlanDetail, WorkoutDetail>,
) : WorkoutsRepository {
    override fun getWorkouts(first: Int): Flow<List<Workout>> {
        return flow {
            val workPlanEntities = workoutPlanDao.getWorkoutPlans(first = first)
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
            val workPlanEntities = workoutPlanDao.searchWorkoutPlanByName(query = query)
            emitAll(
                workPlanEntities.map {
                    it.mapBy(workoutFromEntityMapper)
                }
            )
        }
    }

    override fun getWorkoutDetail(id: Int): Flow<WorkoutDetail> {
        return flow {
            val workoutPlanEntity = workoutPlanDao.getWorkoutPlanDetailStream(id = id)
            emitAll(
                workoutPlanEntity
                    .filterNotNull()
                    .map { workoutDetailMapper.map(it) }
            )
        }
    }
}
