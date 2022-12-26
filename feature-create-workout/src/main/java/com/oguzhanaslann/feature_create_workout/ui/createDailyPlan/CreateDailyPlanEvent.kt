package com.oguzhanaslann.feature_create_workout.ui.createDailyPlan

import com.oguzhanaslann.feature_create_workout.domain.DailyPlan

sealed class CreateDailyPlanEvent {
    class DailyPlanCreated(
        val dailyPlan: DailyPlan,
    ) : CreateDailyPlanEvent()

    object DailyPlanNameEmpty : CreateDailyPlanEvent()
    object DailyPlanCaloriesEmpty : CreateDailyPlanEvent()
    object DailyPlanExerciseEmpty : CreateDailyPlanEvent()
}
