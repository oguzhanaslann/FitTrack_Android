package com.oguzhanaslann.domain_profile.domain.model

import com.oguzhanaslann.feature_profile.domain.model.ActiveWorkoutPlan
import com.oguzhanaslann.feature_profile.domain.model.FavoriteRecipe
import com.oguzhanaslann.feature_profile.domain.model.OldWorkoutPlanOverView

data class Profile(
    val userProfile: UserProfile,
    val progressPhotos: List<ProgressPhoto> = emptyList(),
    val weightProgresses: List<WeightProgress> = emptyList(),
    val favoriteRecipes: List<FavoriteRecipe> = emptyList(),
    val activeWorkoutPlan: ActiveWorkoutPlan? = null,
    val oldWorkouts: List<OldWorkoutPlanOverView> = emptyList(),
)
