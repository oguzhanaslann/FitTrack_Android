package com.oguzhanaslann.feature_workouts.domain.mapper

import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.common_data.local.room.entity.DailyPlanWithExercises
import com.oguzhanaslann.feature_workouts.ui.workoutDetail.DailyPlanShort

class DailyPlanShortMapper: Mapper<DailyPlanWithExercises, DailyPlanShort> {
    override suspend fun map(input: DailyPlanWithExercises): DailyPlanShort {
        return DailyPlanShort(
            id = input.dailyPlanEntity.id,
            name = input.dailyPlanEntity.name,
            calories = input.dailyPlanEntity.calories
        )
    }
}
