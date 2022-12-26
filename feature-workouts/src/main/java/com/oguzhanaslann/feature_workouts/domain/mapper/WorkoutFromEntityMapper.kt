package com.oguzhanaslann.feature_workouts.domain.mapper

import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.common_data.local.room.entity.WorkoutPlanEntity
import com.oguzhanaslann.common_domain.Workout

class WorkoutFromEntityMapper : Mapper<WorkoutPlanEntity, Workout> {
    override suspend fun map(input: WorkoutPlanEntity): Workout {
        return Workout(
            id = input.id,
            name = input.name,
            description = input.description,
            imageUrl = input.imageUrl,
            plans = emptyList()
        )
    }
}
