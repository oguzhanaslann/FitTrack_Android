package com.oguzhanaslann.domain_profile.domain.mapper

import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.commonui.data.local.room.entity.UserWorkoutWithDailyPlans
import com.oguzhanaslann.feature_profile.domain.model.ActiveWorkoutPlan

class UserWorkoutWithDailyPlansToActiveWorkoutPlanMapper :
    Mapper<UserWorkoutWithDailyPlans, ActiveWorkoutPlan> {
    override suspend fun map(input: UserWorkoutWithDailyPlans): ActiveWorkoutPlan {
        return ActiveWorkoutPlan(
            id = "${input.userDailyPlanEntity.id}",
            name = input.userDailyPlanEntity.name,
            description = input.userDailyPlanEntity.description,
            imageUrl = input.userDailyPlanEntity.imageUrl,
            progress = run {
                val dailyPlans = input.dailyPlans
                val completed = dailyPlans.sumOf { if (it.isCompleted) 1L else 0L }
                val progress = completed * 100f / dailyPlans.size
                progress
            }
        )
    }
}
