package com.oguzhanaslann.commonui.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "daily_plan")
data class DailyPlanEntity(
    @ColumnInfo(name = "workout_plan_id")
    val workoutPlanId: Int,
    val name: String,
    val imageUrl: String,
    val calories: Int,
) {
    @androidx.room.PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "daily_plan_id")
    var id: Int? = null
}
