package com.oguzhanaslann.feature_profile.ui.profileEdit

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.oguzhanaslann.common.DateHelper
import com.oguzhanaslann.domain_profile.domain.model.Profile
import com.oguzhanaslann.domain_profile.domain.model.UserProfileEdit
import com.oguzhanaslann.domain_profile.domain.usecase.ProfileEditUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

data class ProfileEditUIState(
    val name: String = "",
    val surname: String = "",
    val birthdate: Date? = null,
    val heightInCm: Int = 0,
    val weightInKg: Int = 0,
)

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val profileEditUseCase: ProfileEditUseCase,
) : ViewModel() {
    private val profileEditUIState = MutableStateFlow(ProfileEditUIState())

    private val _profileEditEventChannel = Channel<ProfileEditEvent>()
    val profileEditEvent = _profileEditEventChannel.receiveAsFlow()

    val userBirthdate = profileEditUIState.map {
        DateHelper.tryFormat(it.birthdate,
            DateHelper.DAY_MONTH_WITH_NAME_YEAR_FORMAT,
            autoLocale = true)
    }.asLiveData()

    val userHeightInCm: LiveData<Int> = profileEditUIState.map {
        it.heightInCm
    }.asLiveData()

    val userWeightInKg: LiveData<Int> = profileEditUIState.map {
        it.weightInKg
    }.asLiveData()

    fun setHeight(value: Int) {
        profileEditUIState.value = profileEditUIState.value.copy(heightInCm = value)
    }

    fun setWeight(value: Int) {
        profileEditUIState.value = profileEditUIState.value.copy(weightInKg = value)
    }

    fun setName(name: String) {
        profileEditUIState.value = profileEditUIState.value.copy(name = name)
    }

    fun setSurname(surname: String) {
        profileEditUIState.value = profileEditUIState.value.copy(surname = surname)
    }

    fun setBirthdate(date: Long) {
        profileEditUIState.value = profileEditUIState.value.copy(birthdate = Date(date))
    }

    fun onSaveButtonClicked() {
        viewModelScope.launch {
            val uiState = profileEditUIState.value
            when {
                uiState.name.isEmpty() -> {
                    Timber.d("Name is empty")
                    _profileEditEventChannel.send(ProfileEditEvent.NameEmpty)
                }
                uiState.surname.isEmpty() -> {
                    Timber.d("Surname is empty")
                    _profileEditEventChannel.send(ProfileEditEvent.SurnameEmpty)
                }
                uiState.birthdate == null -> {
                    Timber.d("Birthdate is empty")
                    _profileEditEventChannel.send(ProfileEditEvent.BirthdateEmpty)
                }
                uiState.heightInCm == 0 -> {
                    Timber.d("Height is empty")
                    _profileEditEventChannel.send(ProfileEditEvent.HeightEmpty)
                }
                uiState.weightInKg == 0 -> {
                    Timber.d("Weight is empty")
                    _profileEditEventChannel.send(ProfileEditEvent.WeightEmpty)
                }
                else -> {
                    saveProfileInformation(uiState)
                }
            }
        }
    }

    private suspend fun saveProfileInformation(uiState: ProfileEditUIState) {
        profileEditUseCase.invoke(
            UserProfileEdit(
                name = uiState.name,
                surname = uiState.surname,
                birthdate = uiState.birthdate,
                heightInCm = uiState.heightInCm,
                weightInKg = uiState.weightInKg,
            )
        )
        _profileEditEventChannel.send(ProfileEditEvent.ProfileSaved)
    }

    fun setProfileInformation(profile: Profile) {
        profileEditUIState.value = ProfileEditUIState(
            name = profile.userProfile.name,
            surname = profile.userProfile.surname,
            birthdate = profile.userProfile.birthDate,
            heightInCm = profile.userProfile.height.magnitudeAsCm.toInt(),
            weightInKg = profile.weightProgresses.lastOrNull()?.weight?.toInt() ?: 0,
        )
    }

    fun getName(): String {
        return profileEditUIState.value.name
    }

    fun getSurname(): String {
        return profileEditUIState.value.surname
    }

    companion object {
        const val DEFAULT_HEIGHT = 170
        const val DEFAULT_WEIGHT = 70
    }
}

sealed class ProfileEditEvent {
    object NameEmpty : ProfileEditEvent()
    object SurnameEmpty : ProfileEditEvent()
    object BirthdateEmpty : ProfileEditEvent()
    object HeightEmpty : ProfileEditEvent()
    object WeightEmpty : ProfileEditEvent()
    object ProfileSaved : ProfileEditEvent()
}
