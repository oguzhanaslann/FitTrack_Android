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

@Dao
interface UserDailyPlanDao : BaseDao<UserDailyPlanEntity> {
    @Query("SELECT * FROM user_daily_plan WHERE user_id = :userId")
    suspend fun getUserDailyPlanByUserId(userId: Int): List<UserDailyPlanEntity>

    @Query(
        """
        SELECT * FROM user_daily_plan WHERE user_workout_plan_id = :userWorkoutPlanId
            AND name = :name 
            AND `order` = :order
    """
    )
    suspend fun getUserDailyPlanByUserWorkoutPlanIdAndNameAndOrder(
        userWorkoutPlanId: Int,
        name: String,
        order: Int
    ): UserDailyPlanEntity?

    @Transaction
    @Query(
        """
        SELECT * FROM user_daily_plan WHERE user_id = :userId
            AND is_completed = 1
    """
    )
    suspend fun getUserCompletedExercises(userId: Int): List<UserDailyPlanWithExercises>?

    @Transaction
    @Query(
        """
        SELECT * FROM user_daily_plan WHERE user_id = :userId
            AND end_date <= :date
            AND is_completed = 1
    """
    )
    suspend fun getPastCompletedUserDailyPlanWithExercises(
        userId: Int,
        date: Long
    ): List<UserDailyPlanWithExercises>?

    @Transaction
    @Query(
        """
        SELECT * FROM user_daily_plan WHERE user_id = :userId
            AND end_date = :date
            AND is_completed = 1
    """
    )
    suspend fun getCompletedUserDailyPlanWithExercisesByDate(
        userId: Int,
        date: Long
    ): List<UserDailyPlanWithExercises>?
}

@Dao
interface UserExerciseDao : BaseDao<UserExerciseEntity>
