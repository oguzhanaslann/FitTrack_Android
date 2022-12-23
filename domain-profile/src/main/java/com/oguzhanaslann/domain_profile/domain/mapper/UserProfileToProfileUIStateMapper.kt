package com.oguzhanaslann.domain_profile.domain.mapper

import com.oguzhanaslann.common.*
import com.oguzhanaslann.commonui.data.local.room.entity.ProgressionPhotoEntity
import com.oguzhanaslann.commonui.data.local.room.entity.RecipeEntity
import com.oguzhanaslann.commonui.data.local.room.entity.UserProfileEntity
import com.oguzhanaslann.commonui.data.local.room.entity.UserWorkoutWithDailyPlans
import com.oguzhanaslann.commonui.data.local.room.entity.WeightRecordEntity
import com.oguzhanaslann.domain_profile.domain.model.Profile
import com.oguzhanaslann.domain_profile.domain.model.ProgressPhoto
import com.oguzhanaslann.domain_profile.domain.model.UserProfile
import com.oguzhanaslann.domain_profile.domain.model.WeightProgress
import com.oguzhanaslann.domain_profile.domain.model.ActiveWorkoutPlan
import com.oguzhanaslann.domain_profile.domain.model.FavoriteRecipe
import com.oguzhanaslann.domain_profile.domain.model.OldWorkoutPlanOverView
import java.util.*

class UserProfileToProfileUIStateMapper(
    private val progressionMapper: Mapper<ProgressionPhotoEntity, ProgressPhoto>,
    private val weightMapper: Mapper<WeightRecordEntity, WeightProgress>,
    private val recipeMapper: Mapper<RecipeEntity, FavoriteRecipe>,
    private val workoutMapper: Mapper<UserWorkoutWithDailyPlans, ActiveWorkoutPlan>,
    private val oldWorkoutMapper: Mapper<UserWorkoutWithDailyPlans, OldWorkoutPlanOverView>,
) : Mapper<UserProfileEntity, Profile> {
    override suspend fun map(input: UserProfileEntity): Profile {

        val activePLan = input.workoutPlans.find { it.userDailyPlanEntity.isActive }
        val oldPlans =
            input.workoutPlans.filter { it.userDailyPlanEntity.isActive.not() && it.userDailyPlanEntity.isCompleted }

        val weight =
            if (input.weightRecords.lastOrNull() != null && input.user.measurementUnit.isNotNullOrEmpty()) {
                Weight.from(input.weightRecords.last().weight.toFloat(),
                    MeasurementUnit.parseFrom(input.user.measurementUnit!!).orDefault()).orEmpty()
            } else {
                Weight.empty
            }

        val height =
            if (input.user.height != null && input.user.measurementUnit.isNotNullOrEmpty()) {
                Height.from(input.user.height!!.toFloat(),
                    MeasurementUnit.parseFrom(input.user.measurementUnit!!).orDefault()).orEmpty()
            } else {
                Height.empty
            }

        return Profile(
            userProfile = UserProfile(
                id = input.user.id ?: 0,
                name = input.user.name ?: "",
                surname = input.user.surname ?: "",
                profilePhotoUrl = input.user.profilePhotoUrl,
                weight = weight,
                height = height,
                birthDate = input.user.birthdate?.let { Date(it) }
            ),
            progressPhotos = input.progressionPhotos.mapBy(progressionMapper),
            weightProgresses = input.weightRecords.mapBy(weightMapper),
            favoriteRecipes = input.favoriteRecipes.mapBy(recipeMapper),
            activeWorkoutPlan = activePLan?.let { workoutMapper.map(it) },
            oldWorkouts = oldPlans.mapBy(oldWorkoutMapper)
        )
    }
}
