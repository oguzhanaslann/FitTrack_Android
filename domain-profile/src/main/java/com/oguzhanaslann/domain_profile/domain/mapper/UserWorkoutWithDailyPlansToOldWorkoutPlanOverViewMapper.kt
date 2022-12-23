package com.oguzhanaslann.domain_profile.domain.mapper

import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.commonui.data.local.room.entity.UserWorkoutWithDailyPlans
import com.oguzhanaslann.domain_profile.domain.model.OldWorkoutPlanOverView

class UserWorkoutWithDailyPlansToOldWorkoutPlanOverViewMapper :
    Mapper<UserWorkoutWithDailyPlans, OldWorkoutPlanOverView> {
    override suspend fun map(input: UserWorkoutWithDailyPlans): OldWorkoutPlanOverView {
        return OldWorkoutPlanOverView(
            id = "${input.userDailyPlanEntity.id}",
            name = input.userDailyPlanEntity.name,
            imageUrl = input.userDailyPlanEntity.imageUrl,
        )
    }
}
