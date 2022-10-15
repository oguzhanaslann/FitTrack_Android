package com.oguzhanaslann.common.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.oguzhanaslann.common.data.local.room.dao.UserDao
import com.oguzhanaslann.common.data.local.room.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class FitTrackDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        const val DATABASE_NAME = "fittrack.db"

        const val DATABASE_VERSION = 1

        @Volatile
        private var instance: FitTrackDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): FitTrackDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    ctx.applicationContext, FitTrackDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        instance = it
                    }
            }
        }


    }
}
