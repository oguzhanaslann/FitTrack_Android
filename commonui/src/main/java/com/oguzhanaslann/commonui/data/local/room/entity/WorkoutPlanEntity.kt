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

@Entity(tableName = "exercise_set")
data class ExerciseSetEntity(
    @ColumnInfo(name = "exercise_id")
    val exerciseId: Int,
    val order: Int,
    val reps: Int? = null,
    val weight: Int? = null,
    val rest: Int? = null
) {
    @androidx.room.PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "exercise_set_id")
    var id: Int? = null
}

data class ExerciseWithSets(
    @Embedded val exerciseEntity: ExerciseEntity,
    @Relation(
        parentColumn = "exercise_id",
        entityColumn = "exercise_id"
    )
    val exerciseSets: List<ExerciseSetEntity>
)

@Entity(tableName = "daily_plan_exercise_cross_ref", primaryKeys = ["daily_plan_id", "exercise_id"])
data class DailyPlanExerciseCrossRef(
    @ColumnInfo(name = "daily_plan_id")
    val dailyPlanId: Int,
    @ColumnInfo(name = "exercise_id")
    val exerciseId: Int,

    val order: Int
)

data class DailyPlanWithExercises(
    @Embedded val dailyPlanEntity: DailyPlanEntity,
    @Relation(
        parentColumn = "daily_plan_id",
        entityColumn = "exercise_id",
        associateBy = Junction(DailyPlanExerciseCrossRef::class),
        entity = ExerciseEntity::class
    )
    val exercises: List<ExerciseWithSets>
)


data class WorkoutPlanDetail(
    @Embedded val workoutPlanEntity: WorkoutPlanEntity,
    @Relation(
        entity = DailyPlanEntity::class,
        parentColumn = "workout_plan_id",
        entityColumn = "workout_plan_id"
    )
    val dailyPlans: List<DailyPlanWithExercises>
)
