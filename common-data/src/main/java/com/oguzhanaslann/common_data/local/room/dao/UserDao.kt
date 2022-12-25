package com.oguzhanaslann.common_data.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.oguzhanaslann.common_data.local.room.entity.ProgressionPhotoEntity
import com.oguzhanaslann.common_data.local.room.entity.RecipeEntity
import com.oguzhanaslann.common_data.local.room.entity.UserEntity
import com.oguzhanaslann.common_data.local.room.entity.UserFavoriteRecipeCrossRef
import com.oguzhanaslann.common_data.local.room.entity.UserProfileEntity
import com.oguzhanaslann.common_data.local.room.entity.UserWithFavoriteRecipes
import com.oguzhanaslann.common_data.local.room.entity.UserWithWorkoutPlan
import com.oguzhanaslann.common_data.local.room.entity.WeightRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao : BaseDao<UserEntity> {

    @Query("SELECT * FROM user WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM user WHERE user_id = :id")
    suspend fun getUserByIdSuspend(id: Int): UserEntity?

    @Query("SELECT * FROM user WHERE user_id = :id")
    fun getUserById(id: Int): Flow<UserEntity>


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
    suspend fun getUserProfileSuspend(userId: Int): UserProfileEntity?

    @Transaction
    @Query("SELECT * FROM user WHERE user_id = :userId")
    fun getUserProfile(userId: Int): Flow<UserProfileEntity>
}

@Dao
interface ProgressionPhotoDao : BaseDao<ProgressionPhotoEntity> {

    @Query("DELETE FROM progression_photo")
    suspend fun clear()

    @Query("DELETE FROM progression_photo WHERE user_id = :userId")
    suspend fun deleteAllPhotosOfUser(userId: Int)
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
