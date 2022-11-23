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

@Entity(tableName = "tag")
data class TagEntity(
    @androidx.room.PrimaryKey
    @ColumnInfo(name = "tag_name")
    val name: String
)

// workout plan tag cross reference
@Entity(
    tableName = "workout_plan_tag_cross_ref",
    primaryKeys = ["workout_plan_id", "tag_name"]
)
data class WorkoutPlanTagCrossRef(
    @ColumnInfo(name = "workout_plan_id")
    val workoutPlanId: Int,
    @ColumnInfo(name = "tag_name")
    val tagName: String
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

@Entity(tableName = "daily_plan")
data class DailyPlanEntity(
    @ColumnInfo(name = "workout_plan_id")
    val workoutPlanId: Int,
    val name: String,
    val imageUrl: String,
) {
    @androidx.room.PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "daily_plan_id")
    var id: Int? = null
}

data class WorkoutPlanWithDailyPlans(
    @Embedded val workoutPlanEntity: WorkoutPlanEntity,
    @Relation(
        parentColumn = "workout_plan_id",
        entityColumn = "workout_plan_id"
    )
    val dailyPlans: List<DailyPlanEntity>
)

@Entity(tableName = "exercise")
data class ExerciseEntity(
    val name: String,
    val imageUrl: String,
    val description: String,
) {
    @androidx.room.PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "exercise_id")
    var id: Int? = null
}

@Entity(tableName = "daily_plan_exercise", primaryKeys = ["daily_plan_id", "exercise_id"])
data class DailyPlanExercise(
    @ColumnInfo(name = "daily_plan_id")
    val dailyPlanId: Int,
    @ColumnInfo(name = "exercise_id")
    val exerciseId: Int,
    @Embedded
    val exerciseSet: ExerciseSet
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

@Entity(tableName = "user_workout_plan")
data class UserWorkoutPlanEntity(
    val name: String,
    val imageUrl: String,
    val description: String,

    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "start_date")
    val startDate: Long,

    @ColumnInfo(name = "end_date")
    val endDate: Long,

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean  = false,

    @ColumnInfo(name = "is_active")
    val isActive: Boolean,
) {
    @androidx.room.PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_workout_plan_id")
    var id: Int? = null
}

@Entity(tableName = "user_daily_plan")
data class UserDailyPlanEntity(

    @ColumnInfo(name = "user_workout_plan_id")
    val activeWorkoutPlanId: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "order")
    val order : Int,

    @ColumnInfo(name = "user_id")
    val userId: Int,

    val imageUrl: String,

    val description: String,

    @ColumnInfo(name = "start_date")
    val startDate: Long,

    @ColumnInfo(name = "end_date")
    val endDate: Long,

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,

    @ColumnInfo(name = "is_active")
    val isActive: Boolean,
) {
    @androidx.room.PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_daily_plan_id")
    var id: Int? = null
}

data class UserWorkoutWithDailyPlans(
    @Embedded
    val userDailyPlanEntity: UserWorkoutPlanEntity,

    @Relation(
        parentColumn = "user_workout_plan_id",
        entityColumn = "user_workout_plan_id"
    )
    val dailyPlans : List<UserDailyPlanEntity>
)

@Entity(tableName = "user_exercise")
data class UserExerciseEntity(
    @ColumnInfo(name = "user_daily_plan_id")
    val activeDailyPlanId: Int,
    val name: String,
    val imageUrl: String,
    val description: String,

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,
) {
    @androidx.room.PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_exercise_id")
    var id: Int? = null
}

data class UserDailyPlanWithExercises(
    @Embedded
    val userDailyPlanEntity: UserDailyPlanEntity,

    @Relation(
        parentColumn = "user_daily_plan_id",
        entityColumn = "user_daily_plan_id"
    )
    val exercises : List<UserExerciseEntity>
)
