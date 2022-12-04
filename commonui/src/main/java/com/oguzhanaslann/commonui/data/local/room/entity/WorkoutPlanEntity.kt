package com.oguzhanaslann.commonui.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

@Entity(tableName = "workout_plan")
data class WorkoutPlanEntity(
    val name: String,
    val imageUrl: String,
    val description: String,
) {
    @androidx.room.PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "workout_plan_id")
    var id: Int? = null
}

data class WorkoutPlanWithTags(
    @Embedded val workoutPlanEntity: WorkoutPlanEntity,
    @Relation(
        parentColumn = "workout_plan_id",
        entityColumn = "tag_name",
        associateBy = Junction(WorkoutPlanTagCrossRef::class)
    )
    val tags: List<TagEntity>
)

data class WorkoutPlanWithDailyPlans(
    @Embedded val workoutPlanEntity: WorkoutPlanEntity,
    @Relation(
        parentColumn = "workout_plan_id",
        entityColumn = "workout_plan_id"
    )
    val dailyPlans: List<DailyPlanEntity>
)

data class ExerciseSet(
    val order: Int,
    val reps: Int? = null,
    val weight: Int? = null,
    val rest: Int? = null,
)

data class DailyPlanWithExercises(
    @Embedded val dailyPlanEntity: DailyPlanEntity,
    @Relation(
        parentColumn = "daily_plan_id",
        entityColumn = "exercise_id",
        associateBy = Junction(DailyPlanExercise::class),
    )
    val exercises: List<ExerciseEntity>
)

data class WorkoutPlanDetail(
    @Embedded val workoutPlanEntity: WorkoutPlanEntity,
    @Relation(
        entity = DailyPlanEntity::class,
        parentColumn = "workout_plan_id",
        entityColumn = "workout_plan_id"
    )
    val dailyPlans: List<DailyPlanWithExercises>,

    @Relation(
        parentColumn = "workout_plan_id",
        entityColumn = "tag_name",
        associateBy = Junction(WorkoutPlanTagCrossRef::class)
    )
    val tags: List<TagEntity>
)

data class UserWorkoutWithDailyPlans(
    @Embedded
    val userDailyPlanEntity: UserWorkoutPlanEntity,

    @Relation(
        parentColumn = "user_workout_plan_id",
        entityColumn = "user_workout_plan_id"
    )
    val dailyPlans : List<UserDailyPlanEntity>
)

data class UserDailyPlanWithExercises(
    @Embedded
    val userDailyPlanEntity: UserDailyPlanEntity,

    @Relation(
        parentColumn = "user_daily_plan_id",
        entityColumn = "user_daily_plan_id"
    )
    val exercises : List<UserExerciseEntity>
)
