package com.oguzhanaslann.commonui.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "tag")
data class TagEntity(
    @androidx.room.PrimaryKey
    @ColumnInfo(name = "tag_name")
    val name: String
)
