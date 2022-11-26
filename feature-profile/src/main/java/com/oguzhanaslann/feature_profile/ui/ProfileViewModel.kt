package com.oguzhanaslann.feature_profile.ui

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.oguzhanaslann.common.State
import com.oguzhanaslann.common.data
import com.oguzhanaslann.common.toState
import com.oguzhanaslann.feature_profile.domain.model.ActiveWorkoutPlan
import com.oguzhanaslann.feature_profile.domain.model.FavoriteRecipe
import com.oguzhanaslann.feature_profile.domain.usecase.LocalPhotosUseCase
import com.oguzhanaslann.feature_profile.domain.model.OldWorkoutPlanOverView
import com.oguzhanaslann.feature_profile.domain.model.ProfileRepository
import com.oguzhanaslann.feature_profile.domain.model.ProgressPhoto
import com.oguzhanaslann.feature_profile.domain.model.WeightProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

fun WeightProgress.toEntry() = Entry(date.toFloat(), weight.toFloat())

data class User(
    val id: Int,
    val name: String,
    val profilePhotoUrl: String?,
)

data class ProfileUIState(
    val user: User,
    val progressPhotos: List<ProgressPhoto> = emptyList(),
    val weight: List<WeightProgress> = emptyList(),
    val favoriteRecipes: List<FavoriteRecipe> = emptyList(),
    val activeWorkoutPlan: ActiveWorkoutPlan? = null,
    val oldWorkouts: List<OldWorkoutPlanOverView> = emptyList()
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val localPhotosUseCase: LocalPhotosUseCase
) : ViewModel() {
    private val _profileUIState = MutableLiveData<State<ProfileUIState>>(State.Initial)
    val profileUIState: LiveData<State<ProfileUIState>> get() = _profileUIState

    val profilePhoto = _profileUIState.map {
        it.data()?.user?.profilePhotoUrl ?: ""
    }

    fun getProfileUIState() {
        _profileUIState.value = State.Loading
        viewModelScope.launch {
            val profileUIState = profileRepository.getProfileUIState()
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
}
