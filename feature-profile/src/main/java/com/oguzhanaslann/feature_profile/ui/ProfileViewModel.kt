package com.oguzhanaslann.feature_profile.ui

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.oguzhanaslann.common.DateHelper
import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.common.State
import com.oguzhanaslann.common.data
import com.oguzhanaslann.common.mapByStateSuspend
import com.oguzhanaslann.common.orDefault
import com.oguzhanaslann.common.toState
import com.oguzhanaslann.domain_profile.domain.ProfileRepository
import com.oguzhanaslann.domain_profile.domain.model.Profile
import com.oguzhanaslann.feature_profile.domain.usecase.LocalPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    private val _profileEventChannel = Channel<ProfileEvent>()
    val profileEvent = _profileEventChannel.receiveAsFlow()

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

    fun logout() {
        viewModelScope.launch {
            profileRepository.logout()
            _profileEventChannel.send(ProfileEvent.Logout)
        }
    }
}

sealed class ProfileEvent {
    object Logout : ProfileEvent()
}
