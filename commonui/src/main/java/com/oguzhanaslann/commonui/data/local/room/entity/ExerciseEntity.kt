package com.oguzhanaslann.commonui.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "exercise")
data class ExerciseEntity(
    val name: String,
    val imageUrl: String,
    val description: String,
    @ColumnInfo(name = "language_code")
    val languageCode: String
) {
    @androidx.room.PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "exercise_id")
    var id: Int? = null
}
