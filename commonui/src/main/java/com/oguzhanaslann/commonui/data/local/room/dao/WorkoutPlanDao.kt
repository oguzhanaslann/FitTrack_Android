package com.oguzhanaslann.commonui.data.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.oguzhanaslann.commonui.data.local.room.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutPlanDao : BaseDao<WorkoutPlanEntity> {

    @Query("Delete from workout_plan")
    suspend fun clear()

    @Query("SELECT * FROM workout_plan WHERE workout_plan_id = :id")
    suspend fun getWorkoutPlanById(id: Int): WorkoutPlanEntity?

    @Query("SELECT * FROM workout_plan WHERE name = :name")
    suspend fun getWorkoutPlanByName(name: String): WorkoutPlanEntity?

    @Query("SELECT * FROM workout_plan WHERE name LIKE '%' || :query || '%'")
    fun searchWorkoutPlanByName(query: String): Flow<List<WorkoutPlanEntity>>

    @Query("SELECT * FROM workout_plan LIMIT :first")
    fun getWorkoutPlans(first: Int): Flow<List<WorkoutPlanEntity>>

    @Transaction
    @Query("SELECT * FROM workout_plan WHERE workout_plan_id = :id")
    suspend fun getWorkoutPlanWithTags(id: Int): WorkoutPlanWithTags?

    @Transaction
    @Query("SELECT * FROM workout_plan WHERE workout_plan_id = :id")
    suspend fun getWorkoutPlanWithDailyPlans(id: Int): WorkoutPlanWithDailyPlans?

    @Transaction
    @Query("SELECT * FROM workout_plan WHERE workout_plan_id = :id")
    suspend fun getWorkoutPlanDetail(id: Int): WorkoutPlanDetail?

    // getWorkoutPlanDetail flow
    @Transaction
    @Query("SELECT * FROM workout_plan WHERE workout_plan_id = :id")
    fun getWorkoutPlanDetailStream(id: Int): Flow<WorkoutPlanDetail?>
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
    @Query("SELECT * FROM exercise WHERE name = :name")
    suspend fun getExerciseByName(name: String): ExerciseEntity?
}

@Dao
interface DailyPlanExerciseCrossRefDao : BaseDao<DailyPlanExercise>

@Dao
interface UserWorkoutPlanDao : BaseDao<UserWorkoutPlanEntity> {
    @Query("SELECT * FROM user_workout_plan WHERE user_id = :userId")
    suspend fun getUserWorkoutPlanByUserId(userId: Int): List<UserWorkoutPlanEntity>

    @Query("SELECT * FROM user_workout_plan WHERE user_workout_plan_id = :id")
    suspend fun getUserWorkoutPlanById(id: Int): UserWorkoutPlanEntity?

    @Transaction
    @Query("SELECT * FROM user_workout_plan WHERE user_workout_plan_id = :id")
    suspend fun getUserWorkoutPlanWithWorkoutPlan(id: Int): UserWorkoutWithDailyPlans?

    @Transaction
    @Query("SELECT * FROM user_workout_plan WHERE user_id = :userId AND is_active = 1")
    suspend fun getActiveUserWorkoutPlan(userId: Int): UserWorkoutWithDailyPlans?

    @Transaction
    @Query("SELECT * FROM user_workout_plan WHERE user_id = :userId AND is_active = 0 AND is_completed = 1")
    suspend fun getPastCompletedUserWorkoutWithDailyPlans(userId: Int): List<UserWorkoutWithDailyPlans>?

    @Query("SELECT * FROM user_workout_plan WHERE user_id = :userId AND is_active = 0 AND is_completed = 1")
    suspend fun getPastCompletedUserWorkouts(userId: Int): List<UserWorkoutPlanEntity>?
}
