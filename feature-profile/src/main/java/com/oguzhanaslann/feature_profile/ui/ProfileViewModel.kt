package com.oguzhanaslann.feature_profile.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.oguzhanaslann.common.State
import com.oguzhanaslann.common.toState
import com.oguzhanaslann.feature_profile.domain.ActiveWorkoutPlan
import com.oguzhanaslann.feature_profile.domain.FavoriteRecipe
import com.oguzhanaslann.feature_profile.domain.OldWorkoutPlanOverView
import com.oguzhanaslann.feature_profile.domain.ProfileRepository
import com.oguzhanaslann.feature_profile.domain.ProgressPhoto
import com.oguzhanaslann.feature_profile.domain.WeightProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

fun WeightProgress.toEntry() = Entry(date.toFloat(), weight.toFloat())

data class ProfileUIState(
    val userName: String = "",
    val progressPhotos: List<ProgressPhoto> = emptyList(),
    val weight: List<WeightProgress> = emptyList(),
    val favoriteRecipes: List<FavoriteRecipe> = emptyList(),
    val activeWorkoutPlan: ActiveWorkoutPlan? = null,
    val oldWorkouts: List<OldWorkoutPlanOverView> = emptyList()
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _profileUIState = MutableLiveData<State<ProfileUIState>>(State.Initial)
    val profileUIState: LiveData<State<ProfileUIState>> get() = _profileUIState

    fun getProfileUIState() {
        _profileUIState.value = State.Loading
        viewModelScope.launch {
            val profileUIState = profileRepository.getProfileUIState()
            profileUIState.collect { _profileUIState.value = it.toState() }
        }
    }
}
