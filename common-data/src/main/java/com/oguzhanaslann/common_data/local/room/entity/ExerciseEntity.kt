package com.oguzhanaslann.common_data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "exercise")
data class ExerciseEntity(
    @androidx.room.PrimaryKey
    @ColumnInfo(name = "exercise_id")
    var id: String,
    val name: String,
    val imageUrl: String,
    val description: String,
    @ColumnInfo(name = "language_code")
    val languageCode: String,
)
