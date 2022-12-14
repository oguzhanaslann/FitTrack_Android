package com.oguzhanaslann.common_data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

@Entity(tableName = "workout_plan")
data class WorkoutPlanEntity(
    @androidx.room.PrimaryKey
    @ColumnInfo(name = "workout_plan_id")
    var id: String,
    val name: String,
    val imageUrl: String,
    val description: String,
    @ColumnInfo(name = "language_code")
    val languageCode: String,
)

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


data class UserWorkoutWithDailyPlansAndExercises(
    @Embedded
    val userWorkoutPlanEntity: UserWorkoutPlanEntity,

    @Relation(
        parentColumn = "user_workout_plan_id",
        entityColumn = "user_workout_plan_id",
        entity = UserDailyPlanEntity::class
    )
    val dailyPlans : List<UserDailyPlanWithExercises>
)
