package com.oguzhanaslann.commonui.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

@Entity(tableName = "user")
data class UserEntity(
    val email: String,
    val password: String,
    val profilePhotoUrl : String? = null,
    val name: String? = null,
    val surname: String? = null,
    val height: Double? = null,
    val measurementUnit: String? = null,
    val birthdate: Long? = null,
    @ColumnInfo(name = "active_workout_plan_id")
    val activeWorkoutPlanId: Int? = null,
) {
    @androidx.room.PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    var id: Int? = null

    override fun toString(): String {
        return super.toString() + " user_id: $id"
    }
}

@Entity(tableName = "progression_photo")
data class ProgressionPhotoEntity(
    @androidx.room.PrimaryKey
    val photoUrl: String,
    @ColumnInfo(name = "user_id")
    val userId : Int,
    val date: Long
)

data class UserWithWorkoutPlan(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "active_workout_plan_id",
        entityColumn = "workout_plan_id"
    )
    val workoutPlanEntity: WorkoutPlanEntity
)

@Entity(tableName = "recipe_entity")
data class RecipeEntity(
    val title : String,
    val imageUrl : String,
    val descriptionJson : String,
) {
    @androidx.room.PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "recipe_id")
    var id: Int? = null
}

@Entity(tableName = "user_favorite_recipes", primaryKeys = ["user_id", "recipe_id"])
data class UserFavoriteRecipeCrossRef(
    @ColumnInfo(name = "user_id")
    val userId: Int,
    @ColumnInfo(name = "recipe_id")
    val recipeId: Int
)

data class UserWithFavoriteRecipes(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "recipe_id",
        associateBy = Junction(UserFavoriteRecipeCrossRef::class)
    )
    val favoriteRecipes: List<RecipeEntity>
)


@Entity(tableName = "weight_record_entity")
data class WeightRecordEntity(
    val weight: Double,
    val date: Long,
    @ColumnInfo(name = "user_id")
    val userId: Int
) {
    @androidx.room.PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "weight_record_id")
    var id: Int? = null
}

data class UserProfileEntity(
    @Embedded val user: UserEntity,

    @Relation(
        parentColumn = "user_id",
        entityColumn = "user_id"
    )
    val progressionPhotos: List<ProgressionPhotoEntity>,

    @Relation(
        parentColumn = "user_id",
        entityColumn = "recipe_id",
        associateBy = Junction(UserFavoriteRecipeCrossRef::class)
    )
    val favoriteRecipes: List<RecipeEntity>,

    @Relation(
        parentColumn = "user_id",
        entityColumn = "user_id"
    )
    val weightRecords: List<WeightRecordEntity>,

    @Relation(
        parentColumn = "user_id",
        entityColumn = "user_id",
        entity = UserDailyPlanEntity::class
    )
    val workoutPlans: List<UserWorkoutWithDailyPlans>
)

data class UserWithWeightRecords(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "user_id"
    )
    val weightRecords: List<WeightRecordEntity>
)
