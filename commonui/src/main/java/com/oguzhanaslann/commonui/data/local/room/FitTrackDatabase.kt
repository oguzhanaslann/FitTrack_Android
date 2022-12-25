package com.oguzhanaslann.commonui.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.oguzhanaslann.commonui.data.local.room.FitTrackDatabase.Companion.DATABASE_VERSION
import com.oguzhanaslann.commonui.data.local.room.dao.*
import com.oguzhanaslann.commonui.data.local.room.entity.*

@Database(
    entities = [
        UserEntity::class,
        WorkoutPlanEntity::class,
        ProgressionPhotoEntity::class,
        RecipeEntity::class,
        UserFavoriteRecipeCrossRef::class,
        WeightRecordEntity::class,
        TagEntity::class,
        WorkoutPlanTagCrossRef::class,
        DailyPlanEntity::class,
        ExerciseEntity::class,
        DailyPlanExercise::class,
        UserWorkoutPlanEntity::class,
        UserDailyPlanEntity::class,
        UserExerciseEntity::class,
    ],
    version = DATABASE_VERSION,
)
abstract class FitTrackDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun workoutPlanDao(): WorkoutPlanDao
    abstract fun progressionPhotoDao(): ProgressionPhotoDao
    abstract fun recipeDao(): RecipeDao
    abstract fun userFavoriteRecipeDao(): UserFavoriteRecipeCrossRefDao
    abstract fun weightRecordDao(): WeightRecordDao
    abstract fun tagDao(): TagDao
    abstract fun workoutPlanTagDao(): WorkoutPlanTagCrossRefDao
    abstract fun dailyPlanDao(): DailyPlanDao
    abstract fun dailyPlanExerciseDao(): DailyPlanExerciseCrossRefDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun userWorkoutPlanDao(): UserWorkoutPlanDao
    abstract fun userDailyPlanDao(): UserDailyPlanDao
    abstract fun userExerciseDao(): UserExerciseDao


    companion object {
        private const val DATABASE_NAME = "fittrack.db"

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
