package com.oguzhanaslann.feature_home.data

import com.oguzhanaslann.common_domain.DailyPlan
import com.oguzhanaslann.feature_home.domain.model.TraceExercise

interface TraceWorkoutRepository {
    suspend fun getDailyPlan(id: String): DailyPlan?
    suspend fun doneWorkout(id: String, traceExercises: List<TraceExercise>)
}
