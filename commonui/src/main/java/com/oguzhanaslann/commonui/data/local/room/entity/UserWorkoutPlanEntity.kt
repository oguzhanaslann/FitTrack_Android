package com.oguzhanaslann.commonui.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "user_workout_plan")
data class UserWorkoutPlanEntity(
    val name: String,
    val imageUrl: String,
    val description: String,

    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "start_date")
    val startDate: Long,

    @ColumnInfo(name = "end_date")
    val endDate: Long,

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean  = false,

    @ColumnInfo(name = "is_active")
    val isActive: Boolean,
) {
    @androidx.room.PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_workout_plan_id")
    var id: Int? = null
}
