package com.oguzhanaslann.fittrack.domain.usecase

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.oguzhanaslann.common_data.local.room.FitTrackDatabase
import com.oguzhanaslann.common_data.local.room.dao.WorkoutPlanTagCrossRefDao
import com.oguzhanaslann.common_data.local.room.entity.DailyPlanEntity
import com.oguzhanaslann.common_data.local.room.entity.DailyPlanExercise
import com.oguzhanaslann.common_data.local.room.entity.ExerciseEntity
import com.oguzhanaslann.common_data.local.room.entity.ExerciseSet
import com.oguzhanaslann.common_data.local.room.entity.TagEntity
import com.oguzhanaslann.common_data.local.room.entity.WorkoutPlanEntity
import com.oguzhanaslann.common_data.local.room.entity.WorkoutPlanTagCrossRef
import com.oguzhanaslann.common_domain.AppLanguage
import com.oguzhanaslann.common_domain.AppLanguageUseCase
import com.oguzhanaslann.commonui.getUrlOfDrawable
import com.oguzhanaslann.fittrack.R
import com.oguzhanaslann.fittrack.data.AppInitRepository
import com.oguzhanaslann.fittrack.domain.model.AppInitialization
import java.util.*

class InitializeAppUseCase(
    private val appInitRepository: AppInitRepository,
    private val appLanguageUseCase: AppLanguageUseCase,
    private val fitTrackDatabase: FitTrackDatabase,
    private val context: Context,
) {

    suspend operator fun invoke(): AppInitialization {
        val hasSeenOnboarding = appInitRepository.hasSeenOnboarding()
        val hasAuthenticated = appInitRepository.hasAuthenticated()

        val currentApplicationLanguage = getCurrentApplicationLanguage()
        appLanguageUseCase.saveAppLanguageIntoMemory(currentApplicationLanguage)

        populateDatabase()

        return AppInitialization(
            hasSeenOnboarding = hasSeenOnboarding,
            hasAuthenticated = hasAuthenticated
        )
    }

    private fun getCurrentApplicationLanguage(): AppLanguage {
        val appLocales = AppCompatDelegate.getApplicationLocales()

        val systemLanguage = Locale.getDefault().language
        return when {
            appLocales.isEmpty -> AppLanguage.fromLanguage(systemLanguage)
            else -> AppLanguage.fromLanguage(appLocales[0]!!.language)
        }
    }

    private suspend fun populateDatabase() {
        if (appInitRepository.isDatabasePopulated()) return
        insertExercises()
        insertTags()
        insertWorkouts()
        appInitRepository.setDatabasePopulated()
    }

    private suspend fun insertExercises() {
        fitTrackDatabase.exerciseDao().apply {
            insertAll(
                ExerciseEntity(
                    id = "BenchPressIDEN",
                    name = "Bench Press",
                    imageUrl = getUrlOfDrawable(context, R.drawable.benchpress),
                    description = "Bench press is a strength training exercise that consists of pressing a weight upwards from a supine position.",
                    languageCode = "en",
                ),

                ExerciseEntity(
                    id = "BenchPressIDTR",
                    name = "Bench Press",
                    imageUrl = getUrlOfDrawable(context, R.drawable.benchpress),
                    description = "Bench press is a strength training exercise that consists of pressing a weight upwards from a supine position.",
                    languageCode = "tr",
                ),

                ExerciseEntity(
                    id = "DeadliftIDEN",
                    name = "Deadlift",
                    imageUrl = getUrlOfDrawable(context, R.drawable.deadlift),
                    description = "The deadlift is a weight training exercise in which a loaded barbell or bar is lifted off the ground to the level of the hips, torso erect.",
                    languageCode = "en",
                ),

                ExerciseEntity(
                    id = "DeadliftIDTR",
                    name = "Deadlift",
                    imageUrl = getUrlOfDrawable(context, R.drawable.deadlift),
                    description = "Deadlift, bir ağırlık antrenmanı egzersizidir. Yüksek bir barbell veya bar yüklenmiş olarak yerden kaldırılır ve bel ve göğüs dik olarak kalır.",
                    languageCode = "tr",
                ),

                ExerciseEntity(
                    id = "SquatIDEN",
                    name = "Squat",
                    imageUrl = getUrlOfDrawable(context, R.drawable.squat),
                    description = "The squat is a compound, full body exercise that trains primarily the muscles of the thighs, hips, buttocks and quads.",
                    languageCode = "en",
                ),

                ExerciseEntity(
                    id = "SquatIDTR",
                    name = "Squat",
                    imageUrl = getUrlOfDrawable(context, R.drawable.squat),
                    description = "The squat is a compound, full body exercise that trains primarily the muscles of the thighs, hips, buttocks and quads.",
                    languageCode = "tr",
                ),

                ExerciseEntity(
                    id = "OverheadPressIDEN",
                    name = "Overhead Press",
                    imageUrl = getUrlOfDrawable(context, R.drawable.ohp),
                    description = "",
                    languageCode = "en"
                ),

                ExerciseEntity(
                    id = "OverheadPressIDTR",
                    name = "Overhead Press",
                    imageUrl = getUrlOfDrawable(context, R.drawable.ohp),
                    description = "",
                    languageCode = "tr"
                ),

                ExerciseEntity(
                    id = "PullUPIDEN",
                    name = "Pull-up",
                    imageUrl = getUrlOfDrawable(context, R.drawable.pullup),
                    description = "",
                    languageCode = "en"
                ),

                ExerciseEntity(
                    id = "PullUPIDTR",
                    name = "Pull-up",
                    imageUrl = getUrlOfDrawable(context, R.drawable.pullup),
                    description = "",
                    languageCode = "tr"
                )
            )
        }
    }

    private suspend fun insertTags() {
        fitTrackDatabase.tagDao().insertAll(
            TagEntity("Hard"),
            TagEntity("Easy"),
            TagEntity("Newbie")
        )
    }

    private suspend fun insertWorkouts() {
        insertBeginner1()
        insertBeginner2()
    }

    private suspend fun insertBeginner1() {
        val workoutEntity = WorkoutPlanEntity(
            id = "Beginner1ID",
            name = "Beginner Workout - 1",
            imageUrl = getUrlOfDrawable(context, R.drawable.workout_1),
            description = "This workout is designed for beginners who want to start working out. It is a 3 day workout plan that focuses on the major muscle groups.",
            languageCode = "en"
        )

        fitTrackDatabase.workoutPlanDao().insert(workoutEntity)

        fitTrackDatabase.workoutPlanTagDao().insertAll(
            WorkoutPlanTagCrossRef(
                workoutPlanId = "Beginner1ID",
                tagName = "Easy"
            ),
            WorkoutPlanTagCrossRef(
                workoutPlanId = "Beginner1ID",
                tagName = "Newbie"
            )
        )

        val dailyPlanEntity = DailyPlanEntity(
            id = UUID.randomUUID().toString(),
            workoutPlanId = workoutEntity.id,
            name = "Day 1",
            imageUrl = getUrlOfDrawable(context, R.drawable.day_1),
            calories = 1000,
            languageCode = "en"
        )

        fitTrackDatabase.dailyPlanDao().insert(dailyPlanEntity)

        val exerciseRelations = listOf(
            DailyPlanExercise(
                dailyPlanId = dailyPlanEntity.id,
                exerciseId = "BenchPressIDEN",
                exerciseSet = ExerciseSet(
                    order = 0,
                    reps = 10,
                    set = 3,
                )
            ),

            DailyPlanExercise(
                dailyPlanId = dailyPlanEntity.id,
                exerciseId = "DeadliftIDEN",
                exerciseSet = ExerciseSet(
                    order = 1,
                    reps = 10,
                    set = 3,
                )
            ),

            DailyPlanExercise(
                dailyPlanId = dailyPlanEntity.id,
                exerciseId = "SquatIDEN",
                exerciseSet = ExerciseSet(
                    order = 2,
                    reps = 10,
                    set = 3,
                )
            )
        )

        fitTrackDatabase.dailyPlanExerciseDao().insertAll(*exerciseRelations.toTypedArray())

        // day 2
        val dailyPlanEntity2 = DailyPlanEntity(
            id = UUID.randomUUID().toString(),
            workoutPlanId = workoutEntity.id,
            name = "Day 2",
            imageUrl = getUrlOfDrawable(context, R.drawable.day_2),
            calories = 1000,
            languageCode = "en"
        )

        fitTrackDatabase.dailyPlanDao().insert(dailyPlanEntity2)

        val exerciseRelations2 = listOf(
            DailyPlanExercise(
                dailyPlanId = dailyPlanEntity2.id,
                exerciseId = "OverheadPressIDEN",
                exerciseSet = ExerciseSet(
                    order = 0,
                    reps = 10,
                    set = 3,
                )
            ),

            DailyPlanExercise(
                dailyPlanId = dailyPlanEntity2.id,
                exerciseId = "PullUPIDEN",
                exerciseSet = ExerciseSet(
                    order = 1,
                    reps = 10,
                    set = 3,
                )
            )
        )

        fitTrackDatabase.dailyPlanExerciseDao().insertAll(*exerciseRelations2.toTypedArray())
    }

    private suspend fun insertBeginner2() {
        val workoutEntity = WorkoutPlanEntity(
            id = "Beginner2ID",
            name = "Beginner Workout - 2",
            imageUrl = getUrlOfDrawable(context, R.drawable.workout_2),
            description = "This workout is designed for beginners who want to start working out. It is a 3 day workout plan that focuses on the major muscle groups.",
            languageCode = "tr"
        )

        fitTrackDatabase.workoutPlanDao().insert(workoutEntity)

        fitTrackDatabase.workoutPlanTagDao().insertAll(
            WorkoutPlanTagCrossRef(
                workoutPlanId = "Beginner2ID",
                tagName = "Medium"
            ),
            WorkoutPlanTagCrossRef(
                workoutPlanId = "Beginner2ID",
                tagName = "Newbie"
            )
        )

        val dailyPlanEntity = DailyPlanEntity(
            id = UUID.randomUUID().toString(),
            workoutPlanId = workoutEntity.id,
            name = "Day 1",
            imageUrl = getUrlOfDrawable(context, R.drawable.day_1),
            calories = 1000,
            languageCode = "tr"
        )

        fitTrackDatabase.dailyPlanDao().insert(dailyPlanEntity)

        val exerciseRelations = listOf(
            DailyPlanExercise(
                dailyPlanId = dailyPlanEntity.id,
                exerciseId = "BenchPressIDTR",
                exerciseSet = ExerciseSet(
                    order = 0,
                    reps = 10,
                    set = 3,
                )
            ),

            DailyPlanExercise(
                dailyPlanId = dailyPlanEntity.id,
                exerciseId = "DeadliftIDTR",
                exerciseSet = ExerciseSet(
                    order = 1,
                    reps = 10,
                    set = 3,
                )
            ),

            DailyPlanExercise(
                dailyPlanId = dailyPlanEntity.id,
                exerciseId = "SquatIDTR",
                exerciseSet = ExerciseSet(
                    order = 2,
                    reps = 10,
                    set = 3,
                )
            )
        )

        fitTrackDatabase.dailyPlanExerciseDao().insertAll(*exerciseRelations.toTypedArray())

        // day 2
        val dailyPlanEntity2 = DailyPlanEntity(
            id = UUID.randomUUID().toString(),
            workoutPlanId = workoutEntity.id,
            name = "Day 2",
            imageUrl = getUrlOfDrawable(context, R.drawable.day_2),
            calories = 1000,
            languageCode = "tr"
        )

        fitTrackDatabase.dailyPlanDao().insert(dailyPlanEntity2)

        val exerciseRelations2 = listOf(
            DailyPlanExercise(
                dailyPlanId = dailyPlanEntity2.id,
                exerciseId = "OverheadPressIDTR",
                exerciseSet = ExerciseSet(
                    order = 0,
                    reps = 10,
                    set = 3,
                )
            ),

            DailyPlanExercise(
                dailyPlanId = dailyPlanEntity2.id,
                exerciseId = "PullUPIDTR",
                exerciseSet = ExerciseSet(
                    order = 1,
                    reps = 10,
                    set = 3,
                )
            )
        )

        fitTrackDatabase.dailyPlanExerciseDao().insertAll(*exerciseRelations2.toTypedArray())
    }
}
