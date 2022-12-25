package com.oguzhanaslann.common_data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(t: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg t: T)

    @Delete
    suspend fun delete(t: T)

    @Delete
    suspend fun delete(vararg t: T)

    @Update
    suspend fun update(t: T)

    @Update
    suspend fun updateAll(vararg t: T)
}
