package com.oguzhanaslann.common_data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "user_exercise")
data class UserExerciseEntity(
    @androidx.room.PrimaryKey
    @ColumnInfo(name = "user_exercise_id")
    var id: String,
    @ColumnInfo(name = "user_daily_plan_id")
    val activeDailyPlanId: String,
    val name: String,
    val imageUrl: String,
    val description: String,

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,

    @ColumnInfo(name = "language_code")
    val languageCode: String,

    val order: Int,
    val reps: Int? = null,
    val set : Int? = null,
)
