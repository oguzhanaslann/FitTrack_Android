package com.oguzhanaslann.commonui.data.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.oguzhanaslann.commonui.data.local.room.entity.*

@Dao
interface WorkoutPlanDao : BaseDao<WorkoutPlanEntity> {

    @Query("Delete from workout_plan")
    suspend fun clear()

    @Query("SELECT * FROM workout_plan WHERE workout_plan_id = :id")
    suspend fun getWorkoutPlanById(id: Int): WorkoutPlanEntity?

    // by name
    @Query("SELECT * FROM workout_plan WHERE name = :name")
    suspend fun getWorkoutPlanByName(name: String): WorkoutPlanEntity?

    // workout plan with tags
    @Transaction
    @Query("SELECT * FROM workout_plan WHERE workout_plan_id = :id")
    suspend fun getWorkoutPlanWithTags(id: Int): WorkoutPlanWithTags?

    // workout plan with daily plans
    @Transaction
    @Query("SELECT * FROM workout_plan WHERE workout_plan_id = :id")
    suspend fun getWorkoutPlanWithDailyPlans(id: Int): WorkoutPlanWithDailyPlans?

    //WorkoutPlanDetail
    @Query("SELECT * FROM workout_plan WHERE workout_plan_id = :id")
    suspend fun getWorkoutPlanDetail(id: Int): WorkoutPlanDetail?
}

@Dao
interface TagDao : BaseDao<TagEntity>

@Dao
interface WorkoutPlanTagCrossRefDao : BaseDao<WorkoutPlanTagCrossRef>

@Dao
interface DailyPlanDao : BaseDao<DailyPlanEntity> {
    @Query("SELECT * FROM daily_plan WHERE name = :name")
    suspend fun getDailyPlanByName(name: String): DailyPlanEntity?

    @Transaction
    @Query("SELECT * FROM daily_plan WHERE daily_plan_id = :id")
    suspend fun getDailyPlanWithExercises(id: Int): DailyPlanWithExercises?

}

@Dao
interface ExerciseDao : BaseDao<ExerciseEntity> {

    @Transaction
    @Query("SELECT * FROM exercise WHERE exercise_id = :id")
    suspend fun getExerciseWithSets(id: Int): ExerciseWithSets?

    //get by name
    @Query("SELECT * FROM exercise WHERE name = :name")
    suspend fun getExerciseByName(name: String): ExerciseEntity?
}

@Dao
interface ExerciseSetDao : BaseDao<ExerciseSetEntity>

@Dao
interface DailyPlanExerciseCrossRefDao : BaseDao<DailyPlanExerciseCrossRef>
