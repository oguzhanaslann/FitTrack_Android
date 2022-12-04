package com.oguzhanaslann.commonui.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity

@Entity(tableName = "daily_plan_exercise", primaryKeys = ["daily_plan_id", "exercise_id"])
data class DailyPlanExercise(
    @ColumnInfo(name = "daily_plan_id")
    val dailyPlanId: Int,
    @ColumnInfo(name = "exercise_id")
    val exerciseId: Int,
    @Embedded
    val exerciseSet: ExerciseSet
)
