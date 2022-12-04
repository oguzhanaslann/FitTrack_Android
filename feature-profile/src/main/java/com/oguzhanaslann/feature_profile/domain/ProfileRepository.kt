package com.oguzhanaslann.feature_profile.domain

import android.util.Log
import com.oguzhanaslann.common.*
import com.oguzhanaslann.commonui.data.local.room.entity.ProgressionPhotoEntity
import com.oguzhanaslann.commonui.data.local.room.entity.RecipeEntity
import com.oguzhanaslann.commonui.data.local.room.entity.UserProfileEntity
import com.oguzhanaslann.commonui.data.local.room.entity.UserWorkoutWithDailyPlans
import com.oguzhanaslann.commonui.data.local.room.entity.WeightRecordEntity
import com.oguzhanaslann.feature_profile.data.local.ProfileLocalDataSource
import com.oguzhanaslann.feature_profile.domain.model.ActiveWorkoutPlan
import com.oguzhanaslann.feature_profile.domain.model.FavoriteRecipe
import com.oguzhanaslann.feature_profile.domain.model.OldWorkoutPlanOverView
import com.oguzhanaslann.feature_profile.domain.model.ProgressPhoto
import com.oguzhanaslann.feature_profile.domain.model.WeightProgress
import com.oguzhanaslann.feature_profile.domain.usecase.PhotoUrlAndLastEditDate
import com.oguzhanaslann.feature_profile.ui.ProfileUIState
import com.oguzhanaslann.feature_profile.ui.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

interface ProfileRepository {
    suspend fun getProfileUIState(): Flow<Result<ProfileUIState>>
    suspend fun setUserProfilePhoto(url: String)
    suspend fun updateProgressPhotos(progressPhotoUrls: List<PhotoUrlAndLastEditDate>)
}

fun ProfileRepository(
    profileLocalDataSource: ProfileLocalDataSource,
    mapper: Mapper<UserProfileEntity, ProfileUIState>,
): ProfileRepository = object : ProfileRepository {
    override suspend fun getProfileUIState(): Flow<Result<ProfileUIState>> {
        val userId = profileLocalDataSource.getUserId()
        return profileLocalDataSource.getUserProfile(userId).map { userProfile ->
            val profileUIState = mapper.map(userProfile)
            Result.success(profileUIState)
        }
    }

    override suspend fun setUserProfilePhoto(url: String) {
        val userId = profileLocalDataSource.getUserId()
        val result = profileLocalDataSource.setUserProfilePhoto(userId, url)
        Log.e("TAG", "setUserProfilePhoto: result : ${result.exceptionOrNull()}")
    }

    override suspend fun updateProgressPhotos(progressPhotoUrls: List<PhotoUrlAndLastEditDate>) {
        val userId = profileLocalDataSource.getUserId()
        profileLocalDataSource.updateProgressPhotosOfUser(progressPhotoUrls, userId)
    }
}

class UserProfileToProfileUIStateMapper(
    private val progressionMapper: Mapper<ProgressionPhotoEntity, ProgressPhoto>,
    private val weightMapper: Mapper<WeightRecordEntity, WeightProgress>,
    private val recipeMapper: Mapper<RecipeEntity, FavoriteRecipe>,
    private val workoutMapper: Mapper<UserWorkoutWithDailyPlans, ActiveWorkoutPlan>,
    private val oldWorkoutMapper: Mapper<UserWorkoutWithDailyPlans, OldWorkoutPlanOverView>,
) : Mapper<UserProfileEntity, ProfileUIState> {
    override suspend fun map(input: UserProfileEntity): ProfileUIState {

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

        return ProfileUIState(
            userProfile = UserProfile(
                id = input.user.id ?: 0,
                name = "${input.user.name} ${input.user.surname}",
                profilePhotoUrl = input.user.profilePhotoUrl,
                weight = weight,
                height = height,
                age = input.user.yearOfBirth?.let { DateHelper.getCurrentYear() - it }.orZero(),
            ),
            progressPhotos = input.progressionPhotos.mapBy(progressionMapper),
            weightProgresses = input.weightRecords.mapBy(weightMapper),
            favoriteRecipes = input.favoriteRecipes.mapBy(recipeMapper),
            activeWorkoutPlan = activePLan?.let { workoutMapper.map(it) },
            oldWorkouts = oldPlans.mapBy(oldWorkoutMapper)
        )
    }
}

class ProgressionPhotoToProgressPhotoMapper : Mapper<ProgressionPhotoEntity, ProgressPhoto> {
    override suspend fun map(input: ProgressionPhotoEntity): ProgressPhoto {
        return ProgressPhoto(
            id = input.photoUrl,
            url = input.photoUrl,
            description = run {
                val dateMillis = input.date
                val date = Date(dateMillis)
                DateHelper.format(
                    date,
                    DateHelper.DAY_MONTH_WITH_NAME_YEAR_FORMAT,
                    autoLocale = true
                )
            }
        )
    }
}

class WeightRecordToWeightProgressMapper : Mapper<WeightRecordEntity, WeightProgress> {
    override suspend fun map(input: WeightRecordEntity): WeightProgress {
        return WeightProgress(
            weight = "${input.weight}",// todo
            date = run {
                val dateMillis = input.date
                val date = Date(dateMillis)
                DateHelper.format(
                    date,
                    DateHelper.DAY_MONTH_WITH_NAME_YEAR_FORMAT,
                    autoLocale = true
                )
            }
        )
    }
}

class RecipeEntityToFavoriteRecipeMapper : Mapper<RecipeEntity, FavoriteRecipe> {
    override suspend fun map(input: RecipeEntity): FavoriteRecipe {
        return FavoriteRecipe(
            id = "${input.id}",
            name = input.title,
            imageUrl = input.imageUrl,
        )
    }
}

class UserWorkoutWithDailyPlansToActiveWorkoutPlanMapper :
    Mapper<UserWorkoutWithDailyPlans, ActiveWorkoutPlan> {
    override suspend fun map(input: UserWorkoutWithDailyPlans): ActiveWorkoutPlan {
        return ActiveWorkoutPlan(
            id = "${input.userDailyPlanEntity.id}",
            name = input.userDailyPlanEntity.name,
            description = input.userDailyPlanEntity.description,
            imageUrl = input.userDailyPlanEntity.imageUrl,
            progress = run {
                val dailyPlans = input.dailyPlans
                val completed = dailyPlans.sumOf { if (it.isCompleted) 1L else 0L }
                val progress = completed * 100f / dailyPlans.size
                progress
            }
        )
    }
}

class UserWorkoutWithDailyPlansToOldWorkoutPlanOverViewMapper :
    Mapper<UserWorkoutWithDailyPlans, OldWorkoutPlanOverView> {
    override suspend fun map(input: UserWorkoutWithDailyPlans): OldWorkoutPlanOverView {
        return OldWorkoutPlanOverView(
            id = "${input.userDailyPlanEntity.id}",
            name = input.userDailyPlanEntity.name,
            imageUrl = input.userDailyPlanEntity.imageUrl,
        )
    }
}
