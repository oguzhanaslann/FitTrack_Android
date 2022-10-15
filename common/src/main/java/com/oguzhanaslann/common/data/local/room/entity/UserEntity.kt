package com.oguzhanaslann.common.data.local.room.entity

import androidx.room.ColumnInfo

@androidx.room.Entity(tableName = "user")
data class UserEntity(
    val email: String,
    val password: String
) {
    @androidx.room.PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null
}
