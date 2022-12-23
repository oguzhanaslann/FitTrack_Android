package com.oguzhanaslann.feature_profile.ui

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.common.State
import com.oguzhanaslann.common.data
import com.oguzhanaslann.common.orDefault
import com.oguzhanaslann.common.toState
import com.oguzhanaslann.domain_profile.domain.model.Profile
import com.oguzhanaslann.domain_profile.domain.ProfileRepository
import com.oguzhanaslann.domain_profile.domain.model.ProgressPhoto
import com.oguzhanaslann.domain_profile.domain.model.UserProfile
import com.oguzhanaslann.domain_profile.domain.model.WeightProgress
import com.oguzhanaslann.feature_profile.domain.model.ActiveWorkoutPlan
import com.oguzhanaslann.feature_profile.domain.model.FavoriteRecipe
import com.oguzhanaslann.feature_profile.domain.model.OldWorkoutPlanOverView
import com.oguzhanaslann.feature_profile.domain.usecase.LocalPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val _profileUIState = MutableLiveData<State<ProfileUIState>>(State.Initial)
    val profileUIState: LiveData<State<ProfileUIState>> get() = _profileUIState

    val profilePhoto = _profileUIState.map {
        it.data()?.userProfile?.profilePhotoUrl ?: ""
    }

    val userName = _profileUIState.map {
        it.data()?.userProfile?.fullName ?: ""
    }

    val userWeight = _profileUIState.map {
        it.data()?.userProfile?.weight?.orDefault()
    }

    val userHeight = _profileUIState.map {
        it.data()?.userProfile?.height.orDefault()
    }

    val userAge = _profileUIState.map {
        it.data()?.userProfile?.age ?: 0
    }

    val oldWorkouts = _profileUIState.map {
        it.data()?.oldWorkouts ?: emptyList()
    }

    val favoriteRecipes = _profileUIState.map {
        it.data()?.favoriteRecipes ?: emptyList()
    }

    val activeWorkoutPlan = _profileUIState.map {
        it.data()?.activeWorkoutPlan
    }

    val weightProgresses = _profileUIState.map {
        it.data()?.weightProgresses ?: emptyList()
    }

    private val mapper: Mapper<Profile,ProfileUIState> = Mapper {
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
            val profileUIState = profileRepository.getProfileUIState()
            profileUIState.map { it.map { mapper.map(it) } }
                .collect { _profileUIState.value = it.toState() }
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
}
