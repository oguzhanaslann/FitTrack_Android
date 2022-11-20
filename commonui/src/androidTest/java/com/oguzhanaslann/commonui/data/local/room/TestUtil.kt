package com.oguzhanaslann.commonui.data.local.room

import androidx.annotation.VisibleForTesting
import com.oguzhanaslann.commonui.data.local.room.entity.DailyPlanEntity
import com.oguzhanaslann.commonui.data.local.room.entity.ProgressionPhotoEntity
import com.oguzhanaslann.commonui.data.local.room.entity.RecipeEntity
import com.oguzhanaslann.commonui.data.local.room.entity.TagEntity
import com.oguzhanaslann.commonui.data.local.room.entity.WeightRecordEntity
import com.oguzhanaslann.commonui.data.local.room.entity.WorkoutPlanEntity

@VisibleForTesting
fun createWorkoutPlanEntity() = WorkoutPlanEntity(
    name = "Workout Plan 1",
    imageUrl = "",
    description = "Lorem ipsum"
)

@VisibleForTesting
fun createRecipeEntity() = RecipeEntity(
    title = "Recipe 1",
    imageUrl = "",
    descriptionJson = "{}"
)

@VisibleForTesting
fun createWeightRecordEntity(userId: Int) = WeightRecordEntity(
    weight = 0.0,
    date = "",
    userId = userId
)

@VisibleForTesting
fun createProgressionPhotoEntity(userId: Int): ProgressionPhotoEntity {
    return ProgressionPhotoEntity(
        photoUrl = "sample://photo",
        userId = userId,
        date = ""
    )
}

@VisibleForTesting
fun createTagEntity() = TagEntity(
    name = "Tag 1"
)

@VisibleForTesting
fun createDailyPlanEntity(workoutPlanId: Int) = DailyPlanEntity(
    name = "Daily Plan 1",
    imageUrl = "",
    workoutPlanId = workoutPlanId,
)

//createExerciseEntity
@VisibleForTesting
fun createExerciseEntity() = com.oguzhanaslann.commonui.data.local.room.entity.ExerciseEntity(
    name = "Exercise 1",
    imageUrl = "",
    description = "Lorem ipsum",
)

//createExerciseSetEntity
@VisibleForTesting
fun createExerciseSetEntity(exerciseId: Int) = com.oguzhanaslann.commonui.data.local.room.entity.ExerciseSetEntity(
    exerciseId = exerciseId,
    order = 0,
    reps = null,
    weight = null,
    rest = null,
)
