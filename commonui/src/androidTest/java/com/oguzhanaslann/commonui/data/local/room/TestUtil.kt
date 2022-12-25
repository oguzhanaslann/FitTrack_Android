package com.oguzhanaslann.commonui.data.local.room

import androidx.annotation.VisibleForTesting
import com.oguzhanaslann.commonui.data.local.room.entity.*

@VisibleForTesting
fun createWorkoutPlanEntity(
    name: String = "Workout Plan 1",
    languageCode: String = "en",
) = WorkoutPlanEntity(
    name = name,
    imageUrl = "",
    description = "Lorem ipsum",
    languageCode = languageCode
)

@VisibleForTesting
fun createRecipeEntity(
    title : String = "Recipe 1",
    languageCode: String = "en",
) = RecipeEntity(
    title = title,
    imageUrl = "",
    descriptionJson = "{}",
    languageCode = languageCode
)

@VisibleForTesting
fun createWeightRecordEntity(userId: Int) = WeightRecordEntity(
    weight = 0.0,
    date = 0,
    userId = userId,
)

@VisibleForTesting
fun createProgressionPhotoEntity(userId: Int): ProgressionPhotoEntity {
    return ProgressionPhotoEntity(
        photoUrl = "sample://photo",
        userId = userId,
        date = 0
    )
}

@VisibleForTesting
fun createTagEntity() = TagEntity(
    name = "Tag 1"
)

@VisibleForTesting
fun createDailyPlanEntity(
    workoutPlanId: Int,
    name : String = "Daily Plan 1",
    languageCode: String = "en",
) = DailyPlanEntity(
    name = "Daily Plan 1",
    imageUrl = "",
    workoutPlanId = workoutPlanId,
    languageCode = languageCode,
    calories = 0,
)

@VisibleForTesting
fun createExerciseEntity(
    name: String = "Exercise 1",
    languageCode: String = "en",
) = ExerciseEntity(
    name = name,
    imageUrl = "",
    description = "Lorem ipsum",
    languageCode = languageCode
)

@VisibleForTesting
fun createExerciseSet() = ExerciseSet(
    order = 0,
    reps = null,
    weight = null,
    rest = null,
)

@VisibleForTesting
fun createUserWorkoutPlanEntity(
    userId: Int,
    languageCode: String = "en",
    isCompleted: Boolean = false,
    isActive: Boolean = false,
) = UserWorkoutPlanEntity(
    name = "",
    imageUrl = "",
    description = "",
    userId = userId,
    startDate = 0,
    endDate = 0,
    isCompleted = isCompleted,
    isActive = isActive,
    languageCode = languageCode
)

@VisibleForTesting
fun createUserDailyPlanEntity(
    activeWorkoutPlanId: Int,
    userId: Int,
    languageCode: String = "en",
    isCompleted: Boolean = false,
    isActive: Boolean = false,
    startDate: Long = 0,
    endDate: Long = 0,
) = UserDailyPlanEntity(
    activeWorkoutPlanId = activeWorkoutPlanId,
    name = "",
    imageUrl = "",
    description = "",
    startDate = startDate,
    endDate = endDate,
    isActive = isActive,
    isCompleted = isCompleted,
    order = 0,
    userId = userId,
    languageCode = languageCode
)

@VisibleForTesting
fun createUserExerciseEntity(
    activeDailyPlanId: Int,
    languageCode: String = "en",
    isCompleted: Boolean = false,
) = UserExerciseEntity(
    name = "",
    imageUrl = "",
    description = "",
    isCompleted = isCompleted,
    languageCode = languageCode,
    activeDailyPlanId = activeDailyPlanId,
)
