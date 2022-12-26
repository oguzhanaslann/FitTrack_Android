package com.oguzhanaslann.common_data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "daily_plan")
data class DailyPlanEntity(
    @ColumnInfo(name = "workout_plan_id")
    val workoutPlanId: Int,
    val name: String,
    val imageUrl: String,
    val calories: Int,
    @ColumnInfo(name = "language_code")
    val languageCode: String,
) {
    @androidx.room.PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "daily_plan_id")
    var id: Int? = null
}

fun DailyPlanEntity(
    id : Int,
    workoutPlanId: Int,
    name: String,
    imageUrl: String,
    calories: Int,
    languageCode: String,
) = DailyPlanEntity(
    workoutPlanId = workoutPlanId,
    name = name,
    imageUrl = imageUrl,
    calories = calories,
    languageCode = languageCode,
).apply { this.id = id }