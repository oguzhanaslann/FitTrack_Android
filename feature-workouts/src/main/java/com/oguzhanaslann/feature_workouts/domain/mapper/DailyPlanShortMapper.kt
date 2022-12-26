package com.oguzhanaslann.feature_workouts.domain.mapper

import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.common_data.local.room.entity.DailyPlanWithExercises
import com.oguzhanaslann.common_domain.DailyPlan
import com.oguzhanaslann.common_domain.Exercise
import com.oguzhanaslann.common_domain.ExerciseSet

class DailyPlanShortMapper : Mapper<DailyPlanWithExercises, DailyPlan> {
    override suspend fun map(input: DailyPlanWithExercises): DailyPlan {
        return DailyPlan(
            id = input.dailyPlanEntity.id,
            name = input.dailyPlanEntity.name,
            calories = input.dailyPlanEntity.calories,
            imageUrl = input.dailyPlanEntity.imageUrl,
            exerciseList = input.exercises.map {
                ExerciseSet(
                    exercise = Exercise(
                        id = it.id,
                        name = it.name,
                        description = it.description,
                        imageUrl = it.imageUrl
                    )
                )
            }
        )
    }
}
