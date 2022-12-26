package com.oguzhanaslann.feature_create_workout.domain.mapper

import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.common_data.local.room.entity.ExerciseEntity
import com.oguzhanaslann.common_domain.Exercise

class ExerciseMapper: Mapper<ExerciseEntity, Exercise> {
    override suspend fun map(input: ExerciseEntity): Exercise {
        return Exercise(
            id = input.id,
            name = input.name,
            description = input.description,
            imageUrl = input.imageUrl,
        )
    }
}
