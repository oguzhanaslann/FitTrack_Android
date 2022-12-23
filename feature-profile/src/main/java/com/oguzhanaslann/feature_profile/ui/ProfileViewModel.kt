package com.oguzhanaslann.feature_profile.ui

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.oguzhanaslann.common.DateHelper
import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.common.State
import com.oguzhanaslann.common.data
import com.oguzhanaslann.common.mapByStateSuspend
import com.oguzhanaslann.common.orDefault
import com.oguzhanaslann.common.toState
import com.oguzhanaslann.domain_profile.domain.ProfileRepository
import com.oguzhanaslann.domain_profile.domain.model.ActiveWorkoutPlan
import com.oguzhanaslann.domain_profile.domain.model.FavoriteRecipe
import com.oguzhanaslann.domain_profile.domain.model.OldWorkoutPlanOverView
import com.oguzhanaslann.domain_profile.domain.model.Profile
import com.oguzhanaslann.domain_profile.domain.model.ProgressPhoto
import com.oguzhanaslann.domain_profile.domain.model.UserProfile
import com.oguzhanaslann.domain_profile.domain.model.WeightProgress
import com.oguzhanaslann.feature_profile.domain.usecase.LocalPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

fun WeightProgress.toEntry() = Entry(date.time.toFloat(), weight.toFloat())

data class ProfileUIState(
    val userProfile: UserProfile,
    val progressPhotos: List<ProgressPhoto> = emptyList(),
    val weightProgresses: List<WeightProgress> = emptyList(),
    val favoriteRecipes: List<FavoriteRecipe> = emptyList(),
    val activeWorkoutPlan: ActiveWorkoutPlan? = null,
    val oldWorkouts: List<OldWorkoutPlanOverView> = emptyList(),
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val localPhotosUseCase: LocalPhotosUseCase,
) : ViewModel() {
    private val _profileUIState = MutableStateFlow<State<Profile>>(State.Initial)
    val profileUIState: LiveData<State<ProfileUIState>> = _profileUIState.map {
        it.mapByStateSuspend { mapper.map(it) }
    }.asLiveData()

    val userName = _profileUIState.map {
        it.data()?.userProfile?.fullName ?: ""
    }.asLiveData()

    val userWeight = _profileUIState.map {
        it.data()?.userProfile?.weight?.orDefault()
    }.asLiveData()

    val userHeight = _profileUIState.map {
        it.data()?.userProfile?.height.orDefault()
    }.asLiveData()

    val userAge = _profileUIState.map {
        it.data()?.userProfile?.birthDate?.let {
            DateHelper.calculateAgeOf(it)
        } ?: 0
    }.asLiveData()

    val oldWorkouts = _profileUIState.map {
        it.data()?.oldWorkouts ?: emptyList()
    }.asLiveData()

    val favoriteRecipes = _profileUIState.map {
        it.data()?.favoriteRecipes ?: emptyList()
    }.asLiveData()

    val activeWorkoutPlan = _profileUIState.map {
        it.data()?.activeWorkoutPlan
    }.asLiveData()

    val weightProgresses = _profileUIState.map {
        it.data()?.weightProgresses ?: emptyList()
    }.asLiveData()

    private val mapper: Mapper<Profile, ProfileUIState> = Mapper {
        ProfileUIState(
            userProfile = it.userProfile,
            progressPhotos = it.progressPhotos,
            weightProgresses = it.weightProgresses,
            favoriteRecipes = it.favoriteRecipes,
            activeWorkoutPlan = it.activeWorkoutPlan,
            oldWorkouts = it.oldWorkouts
        )
    }

    fun getProfileUIState() {
        _profileUIState.value = State.Loading
        viewModelScope.launch {
            val profileUIState = profileRepository.getProfile()
            profileUIState.collect { _profileUIState.value = it.toState() }
        }
    }

    fun updateProfilePhoto(uri: Uri) {
        viewModelScope.launch {
            localPhotosUseCase.saveAndUpdateProfilePhoto(uri)
        }
    }

    fun updateProgressPhotos() {
        viewModelScope.launch {
            localPhotosUseCase.updateProgressPhotos()
        }
    }

    fun getNewProgressPhotoUri(): Uri {
        return localPhotosUseCase.getNewProgressPhotoUri()
    }

    fun getProfileOrNull(): Profile? {
        return _profileUIState.value.data()
    }
}
