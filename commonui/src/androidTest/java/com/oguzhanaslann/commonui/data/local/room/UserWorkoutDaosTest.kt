package com.oguzhanaslann.commonui.data.local.room

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.oguzhanaslann.commonui.data.local.room.dao.UserDailyPlanDao
import com.oguzhanaslann.commonui.data.local.room.dao.UserExerciseDao
import com.oguzhanaslann.commonui.data.local.room.dao.UserWorkoutPlanDao
import com.oguzhanaslann.commonui.data.local.room.entity.UserDailyPlanEntity
import com.oguzhanaslann.commonui.data.local.room.entity.UserExerciseEntity
import com.oguzhanaslann.commonui.data.local.room.entity.UserWorkoutPlanEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
class UserWorkoutDaosTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var db: FitTrackDatabase

    lateinit var userWorkoutPlanDao: UserWorkoutPlanDao
    lateinit var userDailyPlanDao: UserDailyPlanDao
    lateinit var userExerciseDao: UserExerciseDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, FitTrackDatabase::class.java
        ).build()
        userWorkoutPlanDao = db.userWorkoutPlanDao()
        userDailyPlanDao = db.userDailyPlanDao()
        userExerciseDao = db.userExerciseDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    fun test_empty_user_workout_plan_dao() = runTest {
        val anyUserId = 1001010
        val userWorkoutPlan = userWorkoutPlanDao.getUserWorkoutPlanByUserId(anyUserId)
        assertThat(userWorkoutPlan).isEmpty()
    }

    @Test
    fun test_active_workout_with_daily_plans() = runTest {
        val anyUserId = 1001010
        val plan = UserWorkoutPlanEntity(
            name = "",
            imageUrl = "",
            description = "",
            userId = anyUserId,
            startDate = 0,
            endDate = 0,
            isCompleted = false,
            isActive = true
        )

        userWorkoutPlanDao.insert(plan)
        val plansFromDb = userWorkoutPlanDao.getUserWorkoutPlanByUserId(anyUserId)
        assertThat(plansFromDb).hasSize(1)
        val planFromDb = plansFromDb.first()

        val dailyPlan = UserDailyPlanEntity(
            activeWorkoutPlanId = planFromDb.id!!,
            name = "",
            imageUrl = "",
            description = "",
            startDate = 0,
            endDate = 0,
            isCompleted = false,
            isActive = false,
            order = 0,
            userId = anyUserId
        )

        userDailyPlanDao.insert(dailyPlan)

        val userWorkoutWithDailyPlans =
            userWorkoutPlanDao.getUserWorkoutPlanWithWorkoutPlan(planFromDb.id!!)
        assertThat(userWorkoutWithDailyPlans).isNotNull()
        assertThat(userWorkoutWithDailyPlans?.userDailyPlanEntity?.id).isEqualTo(planFromDb.id)
        assertThat(userWorkoutWithDailyPlans?.dailyPlans).hasSize(1)
    }

    //getActiveUserWorkoutPlan
    @Test
    fun test_active_workout_plan() = runTest {
        val anyUserId = 1001010
        val plan = UserWorkoutPlanEntity(
            name = "",
            imageUrl = "",
            description = "",
            userId = anyUserId,
            startDate = 0,
            endDate = 0,
            isCompleted = false,
            isActive = true
        )

        userWorkoutPlanDao.insert(plan)
        val plansFromDb = userWorkoutPlanDao.getUserWorkoutPlanByUserId(anyUserId)
        assertThat(plansFromDb).hasSize(1)
        val planFromDb = plansFromDb.first()

        val activeWorkoutPlan = userWorkoutPlanDao.getActiveUserWorkoutPlan(anyUserId)
        assertThat(activeWorkoutPlan).isNotNull()
        assertThat(activeWorkoutPlan?.userDailyPlanEntity?.id).isEqualTo(planFromDb.id)
    }

    @Test
    fun test_past_completed_workout_plans() = runTest {
        val anyUserId = 1001010
        val plan = UserWorkoutPlanEntity(
            name = "",
            imageUrl = "",
            description = "",
            userId = anyUserId,
            startDate = 0,
            endDate = 0,
            isCompleted = true,
            isActive = false
        )

        userWorkoutPlanDao.insert(plan)
        val plansFromDb = userWorkoutPlanDao.getUserWorkoutPlanByUserId(anyUserId)
        assertThat(plansFromDb).hasSize(1)
        val planFromDb = plansFromDb.first()

        val pastCompletedWorkoutPlans = userWorkoutPlanDao.getPastCompletedUserWorkoutPlans(anyUserId)
        assertThat(pastCompletedWorkoutPlans).isNotNull()
        assertThat(pastCompletedWorkoutPlans).hasSize(1)
    }

    @Test
    fun test_active_workout_with_daily_plans_and_exercises() = runTest {
        val anyUserId = 1001010
        val plan = UserWorkoutPlanEntity(
            name = "",
            imageUrl = "",
            description = "",
            userId = anyUserId,
            startDate = 0,
            endDate = 0,
            isCompleted = false,
            isActive = true
        )

        userWorkoutPlanDao.insert(plan)
        val plansFromDb = userWorkoutPlanDao.getUserWorkoutPlanByUserId(anyUserId)

        assertThat(plansFromDb).hasSize(1)

        val planFromDb = plansFromDb.first()

        val dailyPlan = UserDailyPlanEntity(
            activeWorkoutPlanId = planFromDb.id!!,
            name = "",
            imageUrl = "",
            description = "",
            startDate = 0,
            endDate = 0,
            isCompleted = true,
            isActive = false,
            order = 0,
            userId = anyUserId
        )

        userDailyPlanDao.insert(dailyPlan)

        val dailyPlanFromDb = userDailyPlanDao.getUserDailyPlanByUserWorkoutPlanIdAndNameAndOrder(
            userWorkoutPlanId = planFromDb.id!!,
            name = dailyPlan.name,
            order = dailyPlan.order
        )

        assertThat(dailyPlanFromDb).isNotNull()
        assertThat(dailyPlanFromDb?.id).isNotNull()

        val exercise = UserExerciseEntity(
            name = "",
            imageUrl = "",
            description = "",
            isCompleted = false,
            activeDailyPlanId = dailyPlanFromDb!!.id!!,
        )

        userExerciseDao.insert(exercise)

        val userDailyPlanWithExercises = userDailyPlanDao.getUserCompletedExercises(anyUserId)
        assertThat(userDailyPlanWithExercises).isNotNull()
        assertThat(userDailyPlanWithExercises).hasSize(1)
        assertThat(userDailyPlanWithExercises!!.first().userDailyPlanEntity.id).isEqualTo(
            dailyPlanFromDb!!.id
        )
        assertThat(userDailyPlanWithExercises.first().exercises).hasSize(1)
    }

    @Test
    fun test_user_daily_plans_and_exercises_by_date_on_exact_date() = runTest {
        val anyUserId = 1001010
        val plan = UserWorkoutPlanEntity(
            name = "",
            imageUrl = "",
            description = "",
            userId = anyUserId,
            startDate = 0,
            endDate = 0,
            isCompleted = false,
            isActive = true
        )

        userWorkoutPlanDao.insert(plan)
        val plansFromDb = userWorkoutPlanDao.getUserWorkoutPlanByUserId(anyUserId)

        assertThat(plansFromDb).hasSize(1)

        val planFromDb = plansFromDb.first()


        val now = Date().time
        val yesterday = now - 86400000

        val dailyPlan = UserDailyPlanEntity(
            activeWorkoutPlanId = planFromDb.id!!,
            name = "",
            imageUrl = "",
            description = "",
            startDate = 0,
            endDate = yesterday,
            isCompleted = true,
            isActive = false,
            order = 0,
            userId = anyUserId
        )

        userDailyPlanDao.insert(dailyPlan)

        val dailyPlanFromDb = userDailyPlanDao.getUserDailyPlanByUserWorkoutPlanIdAndNameAndOrder(
            userWorkoutPlanId = planFromDb.id!!,
            name = dailyPlan.name,
            order = dailyPlan.order
        )

        assertThat(dailyPlanFromDb).isNotNull()
        assertThat(dailyPlanFromDb?.id).isNotNull()

        val exercise = UserExerciseEntity(
            name = "",
            imageUrl = "",
            description = "",
            isCompleted = false,
            activeDailyPlanId = dailyPlanFromDb!!.id!!,
        )

        userExerciseDao.insert(exercise)

        val userDailyPlanWithExercises = userDailyPlanDao.getPastCompletedUserDailyPlanWithExercises(anyUserId, yesterday)
        assertThat(userDailyPlanWithExercises).isNotNull()
        assertThat(userDailyPlanWithExercises).hasSize(1)
        assertThat(userDailyPlanWithExercises!!.first().userDailyPlanEntity.id).isEqualTo(dailyPlanFromDb.id)
        assertThat(userDailyPlanWithExercises.first().exercises).hasSize(1)
    }

    @Test
    fun test_user_daily_plans_and_exercises_by_date_by_past_date() = runTest {
        val anyUserId = 1001010
        val plan = UserWorkoutPlanEntity(
            name = "",
            imageUrl = "",
            description = "",
            userId = anyUserId,
            startDate = 0,
            endDate = 0,
            isCompleted = false,
            isActive = true
        )

        userWorkoutPlanDao.insert(plan)
        val plansFromDb = userWorkoutPlanDao.getUserWorkoutPlanByUserId(anyUserId)

        assertThat(plansFromDb).hasSize(1)

        val planFromDb = plansFromDb.first()

        val now = Date().time
        val yesterday = now - 86400000
        val dayBeforeYesterday = yesterday - 86400000

        val dailyPlan = UserDailyPlanEntity(
            activeWorkoutPlanId = planFromDb.id!!,
            name = "",
            imageUrl = "",
            description = "",
            startDate = 0,
            endDate = dayBeforeYesterday,
            isCompleted = true,
            isActive = false,
            order = 0,
            userId = anyUserId
        )

        userDailyPlanDao.insert(dailyPlan)

        val dailyPlanFromDb = userDailyPlanDao.getUserDailyPlanByUserWorkoutPlanIdAndNameAndOrder(
            userWorkoutPlanId = planFromDb.id!!,
            name = dailyPlan.name,
            order = dailyPlan.order
        )

        assertThat(dailyPlanFromDb).isNotNull()
        assertThat(dailyPlanFromDb?.id).isNotNull()

        val exercise = UserExerciseEntity(
            name = "",
            imageUrl = "",
            description = "",
            isCompleted = false,
            activeDailyPlanId = dailyPlanFromDb!!.id!!,
        )

        userExerciseDao.insert(exercise)

        val userDailyPlanWithExercises = userDailyPlanDao.getPastCompletedUserDailyPlanWithExercises(anyUserId, yesterday)
        assertThat(userDailyPlanWithExercises).isNotNull()
        assertThat(userDailyPlanWithExercises).hasSize(1)
        assertThat(userDailyPlanWithExercises!!.first().userDailyPlanEntity.id).isEqualTo(dailyPlanFromDb.id)
        assertThat(userDailyPlanWithExercises.first().exercises).hasSize(1)
    }

    @Test
    fun test_user_daily_plans_and_exercises_by_date_by_future_date() = runTest {
        val anyUserId = 1001010
        val plan = UserWorkoutPlanEntity(
            name = "",
            imageUrl = "",
            description = "",
            userId = anyUserId,
            startDate = 0,
            endDate = 0,
            isCompleted = false,
            isActive = true
        )

        userWorkoutPlanDao.insert(plan)
        val plansFromDb = userWorkoutPlanDao.getUserWorkoutPlanByUserId(anyUserId)

        assertThat(plansFromDb).hasSize(1)

        val planFromDb = plansFromDb.first()

        val now = Date().time
        val yesterday = now - 86400000

        val dailyPlan = UserDailyPlanEntity(
            activeWorkoutPlanId = planFromDb.id!!,
            name = "",
            imageUrl = "",
            description = "",
            startDate = 0,
            endDate = now,
            isCompleted = true,
            isActive = false,
            order = 0,
            userId = anyUserId
        )

        userDailyPlanDao.insert(dailyPlan)

        val dailyPlanFromDb = userDailyPlanDao.getUserDailyPlanByUserWorkoutPlanIdAndNameAndOrder(
            userWorkoutPlanId = planFromDb.id!!,
            name = dailyPlan.name,
            order = dailyPlan.order
        )

        assertThat(dailyPlanFromDb).isNotNull()
        assertThat(dailyPlanFromDb?.id).isNotNull()

        val exercise = UserExerciseEntity(
            name = "",
            imageUrl = "",
            description = "",
            isCompleted = false,
            activeDailyPlanId = dailyPlanFromDb!!.id!!,
        )

        userExerciseDao.insert(exercise)

        val userDailyPlanWithExercises = userDailyPlanDao.getPastCompletedUserDailyPlanWithExercises(anyUserId, yesterday)
        assertThat(userDailyPlanWithExercises).isEmpty()
    }

    @Test
    fun test_completed_dpwe_by_date_matching_date() = runTest {
        val anyUserId = 1001010
        val plan = UserWorkoutPlanEntity(
            name = "",
            imageUrl = "",
            description = "",
            userId = anyUserId,
            startDate = 0,
            endDate = 0,
            isCompleted = false,
            isActive = true
        )

        userWorkoutPlanDao.insert(plan)
        val plansFromDb = userWorkoutPlanDao.getUserWorkoutPlanByUserId(anyUserId)

        assertThat(plansFromDb).hasSize(1)

        val planFromDb = plansFromDb.first()

        val now = Date().time
        val yesterday = now - 86400000

        val dailyPlan = UserDailyPlanEntity(
            activeWorkoutPlanId = planFromDb.id!!,
            name = "",
            imageUrl = "",
            description = "",
            startDate = 0,
            endDate = now,
            isCompleted = true,
            isActive = false,
            order = 0,
            userId = anyUserId
        )

        userDailyPlanDao.insert(dailyPlan)

        val dailyPlanFromDb = userDailyPlanDao.getUserDailyPlanByUserWorkoutPlanIdAndNameAndOrder(
            userWorkoutPlanId = planFromDb.id!!,
            name = dailyPlan.name,
            order = dailyPlan.order
        )

        assertThat(dailyPlanFromDb).isNotNull()
        assertThat(dailyPlanFromDb?.id).isNotNull()

        val exercise = UserExerciseEntity(
            name = "",
            imageUrl = "",
            description = "",
            isCompleted = false,
            activeDailyPlanId = dailyPlanFromDb!!.id!!,
        )

        userExerciseDao.insert(exercise)

        val userDailyPlanWithExercises = userDailyPlanDao.getCompletedUserDailyPlanWithExercisesByDate(anyUserId, now)
        assertThat(userDailyPlanWithExercises).isNotNull()
        assertThat(userDailyPlanWithExercises).hasSize(1)
        assertThat(userDailyPlanWithExercises!!.first().userDailyPlanEntity.id).isEqualTo(dailyPlanFromDb.id)
        assertThat(userDailyPlanWithExercises.first().exercises).hasSize(1)
    }

    @Test
    fun test_completed_dpwe_by_date_not_matching_past_date() = runTest {
        val anyUserId = 1001010
        val plan = UserWorkoutPlanEntity(
            name = "",
            imageUrl = "",
            description = "",
            userId = anyUserId,
            startDate = 0,
            endDate = 0,
            isCompleted = false,
            isActive = true
        )

        userWorkoutPlanDao.insert(plan)
        val plansFromDb = userWorkoutPlanDao.getUserWorkoutPlanByUserId(anyUserId)

        assertThat(plansFromDb).hasSize(1)

        val planFromDb = plansFromDb.first()

        val now = Date().time
        val yesterday = now - 86400000

        val dailyPlan = UserDailyPlanEntity(
            activeWorkoutPlanId = planFromDb.id!!,
            name = "",
            imageUrl = "",
            description = "",
            startDate = 0,
            endDate = now,
            isCompleted = true,
            isActive = false,
            order = 0,
            userId = anyUserId
        )

        userDailyPlanDao.insert(dailyPlan)

        val dailyPlanFromDb = userDailyPlanDao.getUserDailyPlanByUserWorkoutPlanIdAndNameAndOrder(
            userWorkoutPlanId = planFromDb.id!!,
            name = dailyPlan.name,
            order = dailyPlan.order
        )

        assertThat(dailyPlanFromDb).isNotNull()
        assertThat(dailyPlanFromDb?.id).isNotNull()

        val exercise = UserExerciseEntity(
            name = "",
            imageUrl = "",
            description = "",
            isCompleted = false,
            activeDailyPlanId = dailyPlanFromDb!!.id!!,
        )

        userExerciseDao.insert(exercise)

        val userDailyPlanWithExercises = userDailyPlanDao.getCompletedUserDailyPlanWithExercisesByDate(anyUserId, yesterday)
        assertThat(userDailyPlanWithExercises).isEmpty()
    }

    @Test
    fun test_completed_dpwe_by_date_not_matching_future_date() = runTest {
        val anyUserId = 1001010
        val plan = UserWorkoutPlanEntity(
            name = "",
            imageUrl = "",
            description = "",
            userId = anyUserId,
            startDate = 0,
            endDate = 0,
            isCompleted = false,
            isActive = true
        )

        userWorkoutPlanDao.insert(plan)
        val plansFromDb = userWorkoutPlanDao.getUserWorkoutPlanByUserId(anyUserId)

        assertThat(plansFromDb).hasSize(1)

        val planFromDb = plansFromDb.first()

        val now = Date().time
        val tomorrow = now + 86400000

        val dailyPlan = UserDailyPlanEntity(
            activeWorkoutPlanId = planFromDb.id!!,
            name = "",
            imageUrl = "",
            description = "",
            startDate = 0,
            endDate = now,
            isCompleted = true,
            isActive = false,
            order = 0,
            userId = anyUserId
        )

        userDailyPlanDao.insert(dailyPlan)

        val dailyPlanFromDb = userDailyPlanDao.getUserDailyPlanByUserWorkoutPlanIdAndNameAndOrder(
            userWorkoutPlanId = planFromDb.id!!,
            name = dailyPlan.name,
            order = dailyPlan.order
        )

        assertThat(dailyPlanFromDb).isNotNull()
        assertThat(dailyPlanFromDb?.id).isNotNull()

        val exercise = UserExerciseEntity(
            name = "",
            imageUrl = "",
            description = "",
            isCompleted = false,
            activeDailyPlanId = dailyPlanFromDb!!.id!!,
        )

        userExerciseDao.insert(exercise)

        val userDailyPlanWithExercises = userDailyPlanDao.getCompletedUserDailyPlanWithExercisesByDate(anyUserId, tomorrow)
        assertThat(userDailyPlanWithExercises).isEmpty()
    }

    @Test
    fun test_completed_dpwe_by_date_not_completed() = runTest {
        val anyUserId = 1001010
        val plan = UserWorkoutPlanEntity(
            name = "",
            imageUrl = "",
            description = "",
            userId = anyUserId,
            startDate = 0,
            endDate = 0,
            isCompleted = false,
            isActive = true
        )

        userWorkoutPlanDao.insert(plan)
        val plansFromDb = userWorkoutPlanDao.getUserWorkoutPlanByUserId(anyUserId)

        assertThat(plansFromDb).hasSize(1)

        val planFromDb = plansFromDb.first()

        val now = Date().time
        val tomorrow = now + 86400000

        val dailyPlan = UserDailyPlanEntity(
            activeWorkoutPlanId = planFromDb.id!!,
            name = "",
            imageUrl = "",
            description = "",
            startDate = 0,
            endDate = now,
            isCompleted = false,
            isActive = false,
            order = 0,
            userId = anyUserId
        )

        userDailyPlanDao.insert(dailyPlan)

        val dailyPlanFromDb = userDailyPlanDao.getUserDailyPlanByUserWorkoutPlanIdAndNameAndOrder(
            userWorkoutPlanId = planFromDb.id!!,
            name = dailyPlan.name,
            order = dailyPlan.order
        )

        assertThat(dailyPlanFromDb).isNotNull()
        assertThat(dailyPlanFromDb?.id).isNotNull()

        val exercise = UserExerciseEntity(
            name = "",
            imageUrl = "",
            description = "",
            isCompleted = false,
            activeDailyPlanId = dailyPlanFromDb!!.id!!,
        )

        userExerciseDao.insert(exercise)

        val userDailyPlanWithExercises = userDailyPlanDao.getCompletedUserDailyPlanWithExercisesByDate(anyUserId, tomorrow)
        assertThat(userDailyPlanWithExercises).isEmpty()
    }

}
