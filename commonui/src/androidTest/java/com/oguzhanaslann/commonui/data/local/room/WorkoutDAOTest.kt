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

    @Test
    fun test_search_workout_plan_by_name() = runTest {
        val workoutPlanEntity = createWorkoutPlanEntity(name = "test")
        workoutPlanDao.insert(workoutPlanEntity)
        val workoutPlanList = workoutPlanDao.searchWorkoutPlanByName("tes")
        val workoutPlan = workoutPlanList.first()
        assertThat(workoutPlan).isNotEmpty()
    }

    @Test
    fun test_search_workout_plan_by_name_different() = runTest {
        val workoutPlanEntity = createWorkoutPlanEntity(name = "test")
        workoutPlanDao.insert(workoutPlanEntity)
        val workoutPlanList = workoutPlanDao.searchWorkoutPlanByName("test2")
        val workoutPlan = workoutPlanList.first()
        assertThat(workoutPlan).isEmpty()
    }


    // test search workout plan name and language code
    @Test
    fun test_search_workout_plan_by_name_and_language_code() = runTest {
        val name = "test"
        val languageCode = "en"
        val workoutPlanEntity = createWorkoutPlanEntity(name = name, languageCode = languageCode)
        workoutPlanDao.insert(workoutPlanEntity)
        val workoutPlanList = workoutPlanDao.searchWorkoutPlanByName(name, languageCode)
        val workoutPlan = workoutPlanList.first()
        assertThat(workoutPlan).isNotEmpty()
    }

    // test search workout plan name and language code different
    @Test
    fun test_search_workout_plan_by_name_and_language_code_different() = runTest {
        val name = "test"
        val languageCode = "en"
        val workoutPlanEntity = createWorkoutPlanEntity(name = name, languageCode = languageCode)
        workoutPlanDao.insert(workoutPlanEntity)
        val workoutPlanList = workoutPlanDao.searchWorkoutPlanByName(name, "tr")
        val workoutPlan = workoutPlanList.first()
        assertThat(workoutPlan).isEmpty()
    }

    // test search workout plan name and language code 2 different language code
    @Test
    fun test_search_workout_plan_by_name_and_language_code_2_different_language_code() = runTest {
        val name = "test"
        val languageCode = "en"
        val workoutPlanEntityEN = createWorkoutPlanEntity(name = name, languageCode = languageCode)
        workoutPlanDao.insert(workoutPlanEntityEN)

        val nameTR = "testTR"
        val languageCodeTR = "tr"
        val workoutPlanEntityTR = createWorkoutPlanEntity(name = nameTR, languageCode = languageCodeTR)
        workoutPlanDao.insert(workoutPlanEntityTR)

        val workoutPlanList = workoutPlanDao.searchWorkoutPlanByName(name, languageCode)
        val workoutPlan = workoutPlanList.first()
        assertThat(workoutPlan).isNotEmpty()
        assertThat(workoutPlan).hasSize(1)
        assertThat(workoutPlan.first().name).isEqualTo(name)
        assertThat(workoutPlan.first().languageCode).isEqualTo(languageCode)
    }

    // test search workout plan name and language code similar name
    @Test
    fun test_search_workout_plan_by_name_and_language_code_similar_name() = runTest {
        val name = "test"
        val languageCode = "en"
        val workoutPlanEntityEN = createWorkoutPlanEntity(name = name, languageCode = languageCode)
        workoutPlanDao.insert(workoutPlanEntityEN)

        val nameSubstr = name.substring(0, 2)
        val workoutPlanList = workoutPlanDao.searchWorkoutPlanByName(nameSubstr, languageCode)
        val workoutPlan = workoutPlanList.first()
        assertThat(workoutPlan).isNotEmpty()
        assertThat(workoutPlan).hasSize(1)
    }
}
