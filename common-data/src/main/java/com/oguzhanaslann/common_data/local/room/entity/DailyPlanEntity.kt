package com.oguzhanaslann.common_data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "daily_plan")
data class DailyPlanEntity(
    @androidx.room.PrimaryKey
    @ColumnInfo(name = "daily_plan_id")
    var id: String,
    @ColumnInfo(name = "workout_plan_id")
    val workoutPlanId: String,
    val name: String,
    val imageUrl: String,
    val calories: Int,
    @ColumnInfo(name = "language_code")
    val languageCode: String,
) {

}
