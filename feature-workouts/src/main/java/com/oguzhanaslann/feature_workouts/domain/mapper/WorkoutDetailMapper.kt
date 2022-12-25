package com.oguzhanaslann.feature_workouts.domain.mapper

import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.common.mapBy
import com.oguzhanaslann.commonui.data.local.room.entity.DailyPlanWithExercises
import com.oguzhanaslann.commonui.data.local.room.entity.WorkoutPlanDetail
import com.oguzhanaslann.feature_workouts.ui.workoutDetail.DailyPlanShort
import com.oguzhanaslann.feature_workouts.ui.workoutDetail.WorkoutDetail

class WorkoutDetailMapper(
    private val dailyPlanShortMapper: Mapper<DailyPlanWithExercises, DailyPlanShort>
): Mapper<WorkoutPlanDetail, WorkoutDetail> {
    override suspend fun map(input: WorkoutPlanDetail): WorkoutDetail {
        return WorkoutDetail(
            id = input.workoutPlanEntity.id ?: 0,
            name = input.workoutPlanEntity.name,
            imageUrl = input.workoutPlanEntity.imageUrl,
            calories = input.dailyPlans.sumOf { it.dailyPlanEntity.calories },
            description = input.workoutPlanEntity.description,
            programs = input.dailyPlans.mapBy(dailyPlanShortMapper),
            tags = input.tags.map { it.name }
        )
    }
}
