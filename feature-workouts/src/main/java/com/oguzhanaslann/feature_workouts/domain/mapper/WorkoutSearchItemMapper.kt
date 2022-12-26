package com.oguzhanaslann.feature_workouts.domain.mapper

import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.common_domain.Workout
import com.oguzhanaslann.feature_workouts.ui.WorkoutSearchItem
import javax.inject.Inject

class WorkoutSearchItemMapper @Inject constructor() : Mapper<Workout, WorkoutSearchItem> {
    override suspend fun map(input: Workout): WorkoutSearchItem {
        return WorkoutSearchItem(
            id = input.id,
            name = input.name,
            description = input.description,
            image = input.imageUrl,
        )
    }
}
