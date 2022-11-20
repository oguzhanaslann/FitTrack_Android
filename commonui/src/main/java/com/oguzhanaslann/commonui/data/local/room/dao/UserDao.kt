package com.oguzhanaslann.commonui.data.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.oguzhanaslann.commonui.data.local.room.entity.*

@Dao
interface UserDao : BaseDao<UserEntity> {

    @Query("SELECT * FROM user WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("DELETE FROM user")
    suspend fun clear()

    //UserWithWorkoutPlan
    @Transaction
    @Query("SELECT * FROM user WHERE user_id = :userId")
    suspend fun getUserWithWorkoutPlan(userId: Int): UserWithWorkoutPlan?

    // set user active workout plan id
    @Query("UPDATE user SET active_workout_plan_id = :workoutPlanId WHERE user_id = :userId")
    suspend fun setActiveWorkoutPlanId(userId: Int, workoutPlanId: Int)

    //UserWithFavoriteRecipes
    @Transaction
    @Query("SELECT * FROM user WHERE user_id = :userId")
    suspend fun getUserWithFavoriteRecipes(userId: Int): UserWithFavoriteRecipes?

    //UserProfile
    @Transaction
    @Query("SELECT * FROM user WHERE user_id = :userId")
    suspend fun getUserProfile(userId: Int): UserProfile?
}

@Dao
interface ProgressionPhotoDao : BaseDao<ProgressionPhotoEntity> {

    @Query("DELETE FROM progression_photo")
    suspend fun clear()
}

@Dao
interface RecipeDao : BaseDao<RecipeEntity> {
    @Query("SELECT * FROM recipe_entity WHERE title = :title")
    suspend fun getRecipeByTitle(title: String): RecipeEntity?
}

@Dao
interface UserFavoriteRecipeCrossRefDao : BaseDao<UserFavoriteRecipeCrossRef>

@Dao
interface WeightRecordDao : BaseDao<WeightRecordEntity>
