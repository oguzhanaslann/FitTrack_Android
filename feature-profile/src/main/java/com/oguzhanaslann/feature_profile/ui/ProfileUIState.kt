package com.oguzhanaslann.feature_profile.ui

import com.github.mikephil.charting.data.Entry
import com.oguzhanaslann.domain_profile.domain.model.ActiveWorkoutPlan
import com.oguzhanaslann.domain_profile.domain.model.FavoriteRecipe
import com.oguzhanaslann.domain_profile.domain.model.OldWorkoutPlanOverView
import com.oguzhanaslann.domain_profile.domain.model.ProgressPhoto
import com.oguzhanaslann.domain_profile.domain.model.UserProfile
import com.oguzhanaslann.domain_profile.domain.model.WeightProgress

data class ProfileUIState(
    val userProfile: UserProfile,
    val progressPhotos: List<ProgressPhoto> = emptyList(),
    val weightProgresses: List<WeightProgress> = emptyList(),
    val favoriteRecipes: List<FavoriteRecipe> = emptyList(),
    val activeWorkoutPlan: ActiveWorkoutPlan? = null,
    val oldWorkouts: List<OldWorkoutPlanOverView> = emptyList(),
)

fun WeightProgress.toEntry() = Entry(date.time.toFloat(), weight.toFloat())
