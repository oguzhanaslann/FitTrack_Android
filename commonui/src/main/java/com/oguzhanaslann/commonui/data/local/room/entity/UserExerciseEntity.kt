package com.oguzhanaslann.commonui.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "user_exercise")
data class UserExerciseEntity(
    @ColumnInfo(name = "user_daily_plan_id")
    val activeDailyPlanId: Int,
    val name: String,
    val imageUrl: String,
    val description: String,

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,

    @ColumnInfo(name = "language_code")
    val languageCode: String
) {
    @androidx.room.PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_exercise_id")
    var id: Int? = null
}
