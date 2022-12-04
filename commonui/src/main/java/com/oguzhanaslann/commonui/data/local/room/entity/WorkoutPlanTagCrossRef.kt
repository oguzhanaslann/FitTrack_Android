package com.oguzhanaslann.commonui.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

// workout plan tag cross reference
@Entity(
    tableName = "workout_plan_tag_cross_ref",
    primaryKeys = ["workout_plan_id", "tag_name"]
)
data class WorkoutPlanTagCrossRef(
    @ColumnInfo(name = "workout_plan_id")
    val workoutPlanId: Int,
    @ColumnInfo(name = "tag_name")
    val tagName: String
)
