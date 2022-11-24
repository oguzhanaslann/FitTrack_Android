package com.oguzhanaslann.feature_profile.domain

import com.oguzhanaslann.common.DateHelper
import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.common.mapBy
import com.oguzhanaslann.commonui.data.local.room.entity.ProgressionPhotoEntity
import com.oguzhanaslann.commonui.data.local.room.entity.RecipeEntity
import com.oguzhanaslann.commonui.data.local.room.entity.UserProfile
import com.oguzhanaslann.commonui.data.local.room.entity.UserWorkoutWithDailyPlans
import com.oguzhanaslann.commonui.data.local.room.entity.WeightRecordEntity
import com.oguzhanaslann.feature_profile.data.local.ProfileLocalDataSource
import com.oguzhanaslann.feature_profile.ui.ProfileUIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

interface ProfileRepository {
    suspend fun getProfileUIState(): Flow<Result<ProfileUIState>>
}

fun ProfileRepository(
    profileLocalDataSource: ProfileLocalDataSource,
    mapper: Mapper<UserProfile, ProfileUIState>
): ProfileRepository = object : ProfileRepository {
    override suspend fun getProfileUIState(): Flow<Result<ProfileUIState>> {
        val userId = profileLocalDataSource.getUserId()
        return flow {
            val userProfile = profileLocalDataSource.getUserProfile(userId)
            if (userProfile != null) {
                val profileUIState = mapper.map(userProfile)
                emit(Result.success(profileUIState))
            } else {
                emit(Result.failure(Exception("User not found")))
            }
        }
    }
}

class UserProfileToProfileUIStateMapper(
    private val progressionMapper: Mapper<ProgressionPhotoEntity, ProgressPhoto>,
    private val weightMapper: Mapper<WeightRecordEntity, WeightProgress>,
    private val recipeMapper: Mapper<RecipeEntity, FavoriteRecipe>,
    private val workoutMapper: Mapper<UserWorkoutWithDailyPlans, ActiveWorkoutPlan>,
    private val oldWorkoutMapper: Mapper<UserWorkoutWithDailyPlans, OldWorkoutPlanOverView>
) : Mapper<UserProfile, ProfileUIState> {
    override suspend fun map(input: UserProfile): ProfileUIState {

        val activePLan = input.workoutPlans.find { it.userDailyPlanEntity.isActive }
        val oldPlans =
            input.workoutPlans.filter { it.userDailyPlanEntity.isActive.not() && it.userDailyPlanEntity.isCompleted }

        return ProfileUIState(
            userName = "${input.user.name} ${input.user.surname}",
            progressPhotos = input.progressionPhotos.mapBy(progressionMapper),
            weight = input.weightRecords.mapBy(weightMapper),
            favoriteRecipes = input.favoriteRecipes.mapBy(recipeMapper),
            activeWorkoutPlan = workoutMapper.map(activePLan!!),
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
            weight = "${input.weight} ${input.weightUnit}",
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
