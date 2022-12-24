package com.oguzhanaslann.feature_workouts.domain.mapper

import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.feature_workouts.ui.WorkoutSearchItem
import com.oguzhanaslann.feature_workouts.domain.model.Workout
import javax.inject.Inject

class WorkoutSearchItemMapper @Inject constructor() : Mapper<Workout, WorkoutSearchItem> {
    override suspend fun map(input: Workout): WorkoutSearchItem {
        return WorkoutSearchItem(
            id = input.id,
            name = input.name,
            description = input.description,
            image = input.image,
        )
    }
}
