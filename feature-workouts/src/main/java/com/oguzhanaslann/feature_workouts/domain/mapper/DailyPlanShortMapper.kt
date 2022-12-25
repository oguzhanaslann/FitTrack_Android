package com.oguzhanaslann.feature_workouts.domain.mapper

import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.common_data.local.room.entity.DailyPlanWithExercises
import com.oguzhanaslann.feature_workouts.ui.workoutDetail.DailyPlanShort

class DailyPlanShortMapper: Mapper<com.oguzhanaslann.common_data.local.room.entity.DailyPlanWithExercises, DailyPlanShort> {
    override suspend fun map(input: com.oguzhanaslann.common_data.local.room.entity.DailyPlanWithExercises): DailyPlanShort {
        return DailyPlanShort(
            id = input.dailyPlanEntity.id ?: 0,
            name = input.dailyPlanEntity.name,
            calories = input.dailyPlanEntity.calories
        )
    }
}
