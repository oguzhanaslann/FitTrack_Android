package com.oguzhanaslann.common_data.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.oguzhanaslann.common_data.local.room.entity.UserDailyPlanEntity
import com.oguzhanaslann.common_data.local.room.entity.UserDailyPlanWithExercises

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
