package com.oguzhanaslann.common_data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity

@Entity(tableName = "daily_plan_exercise", primaryKeys = ["daily_plan_id", "exercise_id"])
data class DailyPlanExercise(
    @ColumnInfo(name = "daily_plan_id")
    val dailyPlanId: String,
    @ColumnInfo(name = "exercise_id")
    val exerciseId: String,
    @Embedded
    val exerciseSet: ExerciseSet
)

data class ExerciseSet(
    val order: Int,
    val reps: Int? = null,
    val set : Int? = null,
    val weight: Int? = null,
    val rest: Long? = null,
)
