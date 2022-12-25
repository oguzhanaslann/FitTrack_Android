package com.oguzhanaslann.commonui.data.local.room

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.oguzhanaslann.common_data.local.room.dao.ProgressionPhotoDao
import com.oguzhanaslann.common_data.local.room.dao.RecipeDao
import com.oguzhanaslann.common_data.local.room.dao.UserDailyPlanDao
import com.oguzhanaslann.common_data.local.room.dao.UserDao
import com.oguzhanaslann.common_data.local.room.dao.UserFavoriteRecipeCrossRefDao
import com.oguzhanaslann.common_data.local.room.dao.UserWorkoutPlanDao
import com.oguzhanaslann.common_data.local.room.dao.WeightRecordDao
import com.oguzhanaslann.common_data.local.room.dao.WorkoutPlanDao
import com.oguzhanaslann.common_data.local.room.entity.UserEntity
import com.oguzhanaslann.common_data.local.room.entity.UserFavoriteRecipeCrossRef
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
class UserDAOTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var db: com.oguzhanaslann.common_data.local.room.FitTrackDatabase

    lateinit var userDao: com.oguzhanaslann.common_data.local.room.dao.UserDao
    lateinit var workoutPlanDao: com.oguzhanaslann.common_data.local.room.dao.WorkoutPlanDao
    lateinit var progressionPhotoDao: com.oguzhanaslann.common_data.local.room.dao.ProgressionPhotoDao
    lateinit var recipeDao: com.oguzhanaslann.common_data.local.room.dao.RecipeDao
    lateinit var userFavoriteRecipeDao: com.oguzhanaslann.common_data.local.room.dao.UserFavoriteRecipeCrossRefDao
    lateinit var weightRecordDao: com.oguzhanaslann.common_data.local.room.dao.WeightRecordDao
    lateinit var userWorkoutPlanDao: com.oguzhanaslann.common_data.local.room.dao.UserWorkoutPlanDao
    lateinit var userDailyPlanDao: com.oguzhanaslann.common_data.local.room.dao.UserDailyPlanDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, com.oguzhanaslann.common_data.local.room.FitTrackDatabase::class.java
        ).build()
        userDao = db.userDao()
        workoutPlanDao = db.workoutPlanDao()
        progressionPhotoDao = db.progressionPhotoDao()
        recipeDao = db.recipeDao()
        userFavoriteRecipeDao = db.userFavoriteRecipeDao()
        weightRecordDao = db.weightRecordDao()
        userWorkoutPlanDao = db.userWorkoutPlanDao()
        userDailyPlanDao = db.userDailyPlanDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    fun test_user_getUserByEmail_success() = runTest {
        val user =
            com.oguzhanaslann.common_data.local.room.entity.UserEntity("sample@gmail.com", "123456")
        userDao.insert(user)
        val userFromDB = userDao.getUserByEmail("sample@gmail.com")
        assertThat(userFromDB).isEqualTo(user)
    }

    @Test
    fun test_user_getUserByEmail_fail() = runTest {
        val user =
            com.oguzhanaslann.common_data.local.room.entity.UserEntity("sample@gmail.com", "123456")
        userDao.insert(user)
        val userFromDB = userDao.getUserByEmail("other@email.com")
        assertThat(userFromDB).isNotEqualTo(user)
    }

    @Test
    fun test_user_get_user_with_workout_plan_success() = runTest {
        val user =
            com.oguzhanaslann.common_data.local.room.entity.UserEntity("sample@gmail.com", "123456")
        userDao.insert(user)

        val workoutPlanEntity = createWorkoutPlanEntity()
        workoutPlanDao.insert(workoutPlanEntity)
        val workoutPlanFromDb = workoutPlanDao.getWorkoutPlanByName(workoutPlanEntity.name)

        val userFromDb = userDao.getUserByEmail(user.email)
        userDao.setActiveWorkoutPlanId(
            userFromDb!!.id!!,
            workoutPlanFromDb!!.id!!
        )

        val userWithWorkoutPlan = userDao.getUserWithWorkoutPlan(userFromDb.id!!)
        assertThat(userWithWorkoutPlan).isNotNull()
    }

    @Test
    fun test_user_get_user_with_favorite_recipes_success() = runTest {
        val user =
            com.oguzhanaslann.common_data.local.room.entity.UserEntity("sample@gmail.com", "123456")
        userDao.insert(user)

        val userFromDb = userDao.getUserByEmail(user.email)

        val recipeEntity = createRecipeEntity()
        recipeDao.insert(recipeEntity)
        val recipeFromDb = recipeDao.getRecipeByTitle(recipeEntity.title)

        userFavoriteRecipeDao.insert(
            com.oguzhanaslann.common_data.local.room.entity.UserFavoriteRecipeCrossRef(
                userId = userFromDb!!.id!!,
                recipeId = recipeFromDb!!.id!!
            )
        )

        val userWithFavoriteRecipes = userDao.getUserWithFavoriteRecipes(userFromDb.id!!)
        assertThat(userWithFavoriteRecipes).isNotNull()
        assertThat(userWithFavoriteRecipes!!.favoriteRecipes).isNotEmpty()
    }

    @Test
    fun test_user_get_user_with_favorite_recipes_empty() = runTest {
        val user =
            com.oguzhanaslann.common_data.local.room.entity.UserEntity("sample@gmail.com", "123456")
        userDao.insert(user)
        val userFromDb = userDao.getUserByEmail(user.email)
        val userWithFavoriteRecipes = userDao.getUserWithFavoriteRecipes(userFromDb!!.id!!)
        assertThat(userWithFavoriteRecipes).isNotNull()
        assertThat(userWithFavoriteRecipes!!.favoriteRecipes).isEmpty()
    }

    @Test
    fun test_user_getUserProfile_success() = runTest {
        val user =
            com.oguzhanaslann.common_data.local.room.entity.UserEntity("sample@gmail.com", "123456")
        userDao.insert(user)

        val workoutPlanEntity = createWorkoutPlanEntity()
        workoutPlanDao.insert(workoutPlanEntity)
        val workoutPlanFromDb = workoutPlanDao.getWorkoutPlanByName(workoutPlanEntity.name)

        val userFromDb = userDao.getUserByEmail(user.email)
        userDao.setActiveWorkoutPlanId(
            userFromDb!!.id!!,
            workoutPlanFromDb!!.id!!
        )

        val progressionPhotoEntity = createProgressionPhotoEntity(userFromDb.id!!)
        progressionPhotoDao.insert(progressionPhotoEntity)

        val recipeEntity = createRecipeEntity()
        recipeDao.insert(recipeEntity)
        val recipeFromDb = recipeDao.getRecipeByTitle(recipeEntity.title)

        userFavoriteRecipeDao.insert(
            com.oguzhanaslann.common_data.local.room.entity.UserFavoriteRecipeCrossRef(
                userId = userFromDb!!.id!!,
                recipeId = recipeFromDb!!.id!!
            )
        )

        val weightRecordEntity = createWeightRecordEntity(userFromDb.id!!)
        weightRecordDao.insert(weightRecordEntity)

        val plan = createUserWorkoutPlanEntity(userFromDb.id!!)
        userWorkoutPlanDao.insert(plan)
        val plansFromDb = userWorkoutPlanDao.getUserWorkoutPlanByUserId(userFromDb.id!!)
        assertThat(plansFromDb).hasSize(1)
        val planFromDb = plansFromDb.first()

        val dailyPlan = createUserDailyPlanEntity(planFromDb.id!!, userFromDb.id!!)
        userDailyPlanDao.insert(dailyPlan)

        val userProfile = userDao.getUserProfileSuspend(userFromDb.id!!)
        assertThat(userProfile).isNotNull()
        assertThat(userProfile!!.user.id).isEqualTo(userFromDb.id)
        assertThat(userProfile.workoutPlans).isNotEmpty()
        assertThat(userProfile.favoriteRecipes).isNotEmpty()
        assertThat(userProfile.weightRecords).isNotEmpty()
    }
}
