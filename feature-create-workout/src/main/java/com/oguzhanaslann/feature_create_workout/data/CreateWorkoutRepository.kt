package com.oguzhanaslann.feature_create_workout.data

import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.common.mapBy
import com.oguzhanaslann.common_data.local.room.dao.ExerciseDao
import com.oguzhanaslann.common_data.local.room.entity.ExerciseEntity
import com.oguzhanaslann.feature_create_workout.domain.Exercise
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

interface CreateWorkoutRepository {
    suspend fun getExercises(first: Int, appLanguageCode: String): Flow<List<Exercise>>
    suspend fun searchExercise(query: String, appLanguageCode: String): Flow<List<Exercise>>
}

class CreateWorkoutRepositoryImpl(
    private val exerciseDao: ExerciseDao,
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
}
