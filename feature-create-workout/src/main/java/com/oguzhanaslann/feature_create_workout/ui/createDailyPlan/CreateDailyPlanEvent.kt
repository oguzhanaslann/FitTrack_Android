package com.oguzhanaslann.feature_create_workout.ui.createDailyPlan

import com.oguzhanaslann.feature_create_workout.domain.DailyPlanToBeSaved

sealed class CreateDailyPlanEvent {
    class DailyPlanCreated(
        val dailyPlan: DailyPlanToBeSaved,
    ) : CreateDailyPlanEvent()

    object DailyPlanNameEmpty : CreateDailyPlanEvent()
    object DailyPlanCaloriesEmpty : CreateDailyPlanEvent()
    object DailyPlanExerciseEmpty : CreateDailyPlanEvent()
}
