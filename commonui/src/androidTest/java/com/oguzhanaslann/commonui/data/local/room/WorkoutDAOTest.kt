package com.oguzhanaslann.commonui.data.local.room

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.oguzhanaslann.commonui.data.local.room.dao.DailyPlanDao
import com.oguzhanaslann.commonui.data.local.room.dao.DailyPlanExerciseCrossRefDao
import com.oguzhanaslann.commonui.data.local.room.dao.ExerciseDao
import com.oguzhanaslann.commonui.data.local.room.dao.TagDao
import com.oguzhanaslann.commonui.data.local.room.dao.WorkoutPlanDao
import com.oguzhanaslann.commonui.data.local.room.dao.WorkoutPlanTagCrossRefDao
import com.oguzhanaslann.commonui.data.local.room.entity.DailyPlanExercise
import com.oguzhanaslann.commonui.data.local.room.entity.WorkoutPlanTagCrossRef
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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
class WorkoutDAOTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var db: FitTrackDatabase

    lateinit var workoutPlanDao: WorkoutPlanDao
    lateinit var tagDao: TagDao
    lateinit var workoutPlanTagDao: WorkoutPlanTagCrossRefDao
    lateinit var dailyPlanDao: DailyPlanDao
    lateinit var exerciseDao: ExerciseDao
    lateinit var dailyPlanExerciseDao: DailyPlanExerciseCrossRefDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, FitTrackDatabase::class.java
        ).build()
        workoutPlanDao = db.workoutPlanDao()
        tagDao = db.tagDao()
        workoutPlanTagDao = db.workoutPlanTagDao()
        dailyPlanDao = db.dailyPlanDao()
        exerciseDao = db.exerciseDao()
        dailyPlanExerciseDao = db.dailyPlanExerciseDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    fun test_get_workout_plan_by_id() = runTest {
        val workoutPlanEntity = createWorkoutPlanEntity()
        workoutPlanDao.insert(workoutPlanEntity)
        val workoutPlanFromDb = workoutPlanDao.getWorkoutPlanByName(workoutPlanEntity.name)
        assertThat(workoutPlanFromDb?.name).isEqualTo(workoutPlanEntity.name)
    }

    //getWorkoutPlanWithTags test
    @Test
    fun test_get_workout_plan_with_tags() = runTest {
        val workoutPlanEntity = createWorkoutPlanEntity()
        workoutPlanDao.insert(workoutPlanEntity)
        val workoutPlanFromDb = workoutPlanDao.getWorkoutPlanByName(workoutPlanEntity.name)
        val tagEntity = createTagEntity()
        tagDao.insert(tagEntity)

        val workoutPlanTagCrossRef = WorkoutPlanTagCrossRef(
            workoutPlanId = workoutPlanFromDb?.id!!,
            tagName = tagEntity.name
        )
        workoutPlanTagDao.insert(workoutPlanTagCrossRef)

        val workoutPlanWithTags = workoutPlanDao.getWorkoutPlanWithTags(workoutPlanFromDb!!.id!!)
        assertThat(workoutPlanWithTags).isNotNull()
    }

    //getWorkoutPlanWithDailyPlans test
    @Test
    fun test_get_workout_plan_with_daily_plans() = runTest {
        val workoutPlanEntity = createWorkoutPlanEntity()
        workoutPlanDao.insert(workoutPlanEntity)
        val workoutPlanFromDb = workoutPlanDao.getWorkoutPlanByName(workoutPlanEntity.name)

        val dailyPlanEntity = createDailyPlanEntity(workoutPlanFromDb!!.id!!)
        dailyPlanDao.insert(dailyPlanEntity)

        val workoutPlanWithDailyPlans =
            workoutPlanDao.getWorkoutPlanWithDailyPlans(workoutPlanFromDb!!.id!!)
        assertThat(workoutPlanWithDailyPlans).isNotNull()
    }

    @Test
    fun test_get_workout_plan_detail() = runTest {
        val workoutPlanEntity = createWorkoutPlanEntity()
        workoutPlanDao.insert(workoutPlanEntity)
        val workoutPlanFromDb = workoutPlanDao.getWorkoutPlanByName(workoutPlanEntity.name)

        val dailyPlanEntity = createDailyPlanEntity(workoutPlanFromDb!!.id!!)
        dailyPlanDao.insert(dailyPlanEntity)

        val dailyPlanFromDb = dailyPlanDao.getDailyPlanByName(dailyPlanEntity.name)

        val exerciseEntity = createExerciseEntity()
        exerciseDao.insert(exerciseEntity)
        val exerciseFromDb = exerciseDao.getExerciseByName(exerciseEntity.name)

        val dailyPlanExercise =
            DailyPlanExercise(
                dailyPlanId = dailyPlanFromDb!!.id!!,
                exerciseId = exerciseFromDb!!.id!!,
                exerciseSet = createExerciseSet()
            )
        dailyPlanExerciseDao.insert(dailyPlanExercise)

        val workoutPlanDetail = workoutPlanDao.getWorkoutPlanDetail(workoutPlanFromDb!!.id!!)
        assertThat(workoutPlanDetail).isNotNull()
    }

    // test searchWorkoutPlanByName by similar name
    @Test
    fun test_search_workout_plan_by_name() = runTest {
        val workoutPlanEntity = createWorkoutPlanEntity(name = "test")
        workoutPlanDao.insert(workoutPlanEntity)
        val workoutPlanList = workoutPlanDao.searchWorkoutPlanByName("tes")
        val workoutPlan = workoutPlanList.first()
        assertThat(workoutPlan).isNotEmpty()
    }

    // test searchWorkoutPlanByName by different name
    @Test
    fun test_search_workout_plan_by_name_different() = runTest {
        val workoutPlanEntity = createWorkoutPlanEntity(name = "test")
        workoutPlanDao.insert(workoutPlanEntity)
        val workoutPlanList = workoutPlanDao.searchWorkoutPlanByName("test2")
        val workoutPlan = workoutPlanList.first()
        assertThat(workoutPlan).isEmpty()
    }

}
