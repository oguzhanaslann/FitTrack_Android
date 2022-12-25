package com.oguzhanaslann.common_data.local.room.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class DailyPlanWithExercises(
    @Embedded val dailyPlanEntity: DailyPlanEntity,
    @Relation(
        parentColumn = "daily_plan_id",
        entityColumn = "exercise_id",
        associateBy = Junction(DailyPlanExercise::class),
    )
    val exercises: List<ExerciseEntity>
)
