package com.oguzhanaslann.domain_profile.domain.model

data class Profile(
    val userProfile: UserProfile,
    val progressPhotos: List<ProgressPhoto> = emptyList(),
    val weightProgresses: List<WeightProgress> = emptyList(),
    val favoriteRecipes: List<FavoriteRecipe> = emptyList(),
    val activeWorkoutPlan: ActiveWorkoutPlan? = null,
    val oldWorkouts: List<OldWorkoutPlanOverView> = emptyList(),
) : java.io.Serializable
