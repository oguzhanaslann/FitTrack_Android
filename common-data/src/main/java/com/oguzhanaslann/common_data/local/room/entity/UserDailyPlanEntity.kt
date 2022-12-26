package com.oguzhanaslann.common_data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "user_daily_plan")
data class UserDailyPlanEntity(

    @androidx.room.PrimaryKey
    @ColumnInfo(name = "user_daily_plan_id")
    var id: String,

    @ColumnInfo(name = "user_workout_plan_id")
    val activeWorkoutPlanId: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "order")
    val order : Int,

    @ColumnInfo(name = "user_id")
    val userId: Int,

    val imageUrl: String,

    val description: String,

    @ColumnInfo(name = "start_date")
    val startDate: Long,

    @ColumnInfo(name = "end_date")
    val endDate: Long,

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,

    @ColumnInfo(name = "is_active")
    val isActive: Boolean,

    @ColumnInfo(name = "language_code")
    val languageCode: String,

    val calories: Int,
)
