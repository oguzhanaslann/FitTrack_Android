package com.oguzhanaslann.domain_profile.domain.mapper

import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.common_data.local.room.entity.UserWorkoutWithDailyPlans
import com.oguzhanaslann.domain_profile.domain.model.OldWorkoutPlanOverView

class UserWorkoutWithDailyPlansToOldWorkoutPlanOverViewMapper :
    Mapper<com.oguzhanaslann.common_data.local.room.entity.UserWorkoutWithDailyPlans, OldWorkoutPlanOverView> {
    override suspend fun map(input: com.oguzhanaslann.common_data.local.room.entity.UserWorkoutWithDailyPlans): OldWorkoutPlanOverView {
        return OldWorkoutPlanOverView(
            id = "${input.userDailyPlanEntity.id}",
            name = input.userDailyPlanEntity.name,
            imageUrl = input.userDailyPlanEntity.imageUrl,
        )
    }
}
