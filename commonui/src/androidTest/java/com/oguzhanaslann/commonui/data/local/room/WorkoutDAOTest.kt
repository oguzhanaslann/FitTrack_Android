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
import com.oguzhanaslann.commonui.data.local.room.dao.ExerciseSetDao
import com.oguzhanaslann.commonui.data.local.room.dao.TagDao
import com.oguzhanaslann.commonui.data.local.room.dao.WorkoutPlanDao
import com.oguzhanaslann.commonui.data.local.room.dao.WorkoutPlanTagCrossRefDao
import com.oguzhanaslann.commonui.data.local.room.entity.DailyPlanExerciseCrossRef
import com.oguzhanaslann.commonui.data.local.room.entity.WorkoutPlanTagCrossRef
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
class WorkoutDAOTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var db: FitTrackDatabase

    lateinit var workoutPlanDao: WorkoutPlanDao
    lateinit var tagDao: TagDao
    lateinit var workoutPlanTagDao: WorkoutPlanTagCrossRefDao
    lateinit var dailyPlanDao: DailyPlanDao
    lateinit var exerciseDao: ExerciseDao
    lateinit var exerciseSetDao: ExerciseSetDao
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
        exerciseSetDao = db.exerciseSetDao()
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

        val exerciseSetEntity = createExerciseSetEntity(exerciseFromDb!!.id!!)
        exerciseSetDao.insert(exerciseSetEntity)

        val dailyPlanExerciseCrossRef =
            DailyPlanExerciseCrossRef(dailyPlanFromDb!!.id!!, exerciseFromDb.id!!, order = 0)
        dailyPlanExerciseDao.insert(dailyPlanExerciseCrossRef)

        val workoutPlanDetail = workoutPlanDao.getWorkoutPlanDetail(workoutPlanFromDb!!.id!!)
        assertThat(workoutPlanDetail).isNotNull()
    }

    @Test
    fun test_get_daily_plan_with_exercises() = runTest {
        val workoutPlanEntity = createWorkoutPlanEntity()
        workoutPlanDao.insert(workoutPlanEntity)
        val workoutPlanFromDb = workoutPlanDao.getWorkoutPlanByName(workoutPlanEntity.name)

        val dailyPlanEntity = createDailyPlanEntity(workoutPlanFromDb!!.id!!)
        dailyPlanDao.insert(dailyPlanEntity)
        val dailyPlanFromDb = dailyPlanDao.getDailyPlanByName(dailyPlanEntity.name)

        val exerciseEntity = createExerciseEntity()
        exerciseDao.insert(exerciseEntity)
        val exerciseFromDb = exerciseDao.getExerciseByName(exerciseEntity.name)

        val exerciseSetEntity = createExerciseSetEntity(exerciseFromDb!!.id!!)
        exerciseSetDao.insert(exerciseSetEntity)

        val dailyPlanExerciseCrossRef = DailyPlanExerciseCrossRef(
            dailyPlanId = dailyPlanFromDb!!.id!!,
            exerciseId = exerciseFromDb!!.id!!,
            order = 0
        )

        dailyPlanExerciseDao.insert(dailyPlanExerciseCrossRef)

        val dailyPlanWithExercises = dailyPlanDao.getDailyPlanWithExercises(dailyPlanFromDb!!.id!!)
        assertThat(dailyPlanWithExercises).isNotNull()
    }

}
