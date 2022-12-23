package com.oguzhanaslann.feature_auth.ui.profilesetup

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.oguzhanaslann.common.DateHelper
import com.oguzhanaslann.domain_profile.domain.model.UserProfileEdit
import com.oguzhanaslann.domain_profile.domain.usecase.ProfileEditUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProfileSetUpViewModel @Inject constructor(
    private val profileEditUseCase: ProfileEditUseCase
) : ViewModel() {

    private val _userName = MutableStateFlow<String>("")
    private val _userSurname = MutableStateFlow<String>("")
    private val _userBirthdate = MutableStateFlow<Date?>(null)
    private val _userHeightInCm = MutableStateFlow<Int>(DEFAULT_HEIGHT)
    private val _userWeightInKg = MutableStateFlow<Int>(DEFAULT_WEIGHT)
    private val _userProfilePhoto = MutableStateFlow<Uri?>(null)

    private val _profileSetupEventsChannel = Channel<ProfileSetupEvent>()
    val profileSetupEvents = _profileSetupEventsChannel.receiveAsFlow()

    val profileSetupUIState = combine(
        _userName,
        _userSurname,
        _userBirthdate,
        _userHeightInCm,
        _userWeightInKg,
        _userProfilePhoto
    ) { flows ->
        val name = flows[0] as String
        val surname = flows[1] as String
        val birthdate = flows[2] as Date?
        val heightInCm = flows[3] as Int
        val weightInKg = flows[4] as Int
        val profilePhotoUri = flows[5] as Uri?

        ProfileSetupUIState(
            name = name,
            surname = surname,
            birthdate = birthdate,
            heightInCm = heightInCm,
            weightInKg = weightInKg,
            profilePhotoUri = profilePhotoUri
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ProfileSetupUIState()
    )

    val userName: LiveData<String> = profileSetupUIState.map {
        it.name
    }.asLiveData()
    val userSurname: LiveData<String> = profileSetupUIState.map {
        it.surname
    }.asLiveData()

    val userBirthdate = _userBirthdate.map {
        DateHelper.tryFormat(it, DateHelper.DAY_MONTH_WITH_NAME_YEAR_FORMAT, autoLocale = true)
    }.asLiveData()

    val userHeightInCm: LiveData<Int> = profileSetupUIState.map {
        it.heightInCm
    }.asLiveData()

    val userWeightInKg: LiveData<Int> = profileSetupUIState.map {
        it.weightInKg
    }.asLiveData()

    val userProfilePhoto: LiveData<Uri?> = profileSetupUIState.map {
        it.profilePhotoUri
    }.asLiveData()

    fun setHeight(value: Int) {
        _userHeightInCm.value = value
    }

    fun setWeight(value: Int) {
        _userWeightInKg.value = value
    }

    fun setName(name: String) {
        _userName.value = name
    }

    fun setSurname(surname: String) {
        _userSurname.value = surname
    }

    fun setBirthdate(date: Long) {
        _userBirthdate.value = Date(date)
    }

    fun onPhotoSelected(uri: Uri) {
        _userProfilePhoto.value = uri
    }

    fun onSaveButtonClicked() {
        viewModelScope.launch {
            val uiState = profileSetupUIState.value
            when {
                uiState.name.isEmpty() -> {
                    Timber.d("Name is empty")
                    _profileSetupEventsChannel.send(ProfileSetupEvent.NameEmpty)
                }
                uiState.surname.isEmpty() -> {
                    Timber.d("Surname is empty")
                    _profileSetupEventsChannel.send(ProfileSetupEvent.SurnameEmpty)
                }
                uiState.birthdate == null -> {
                    Timber.d("Birthdate is empty")
                    _profileSetupEventsChannel.send(ProfileSetupEvent.BirthdateEmpty)
                }
                uiState.heightInCm == 0 -> {
                    Timber.d("Height is empty")
                    _profileSetupEventsChannel.send(ProfileSetupEvent.HeightEmpty)
                }
                uiState.weightInKg == 0 -> {
                    Timber.d( "Weight is empty")
                    _profileSetupEventsChannel.send(ProfileSetupEvent.WeightEmpty)
                }
                else -> {
                    saveProfileInformation(uiState)
                }
            }
        }
    }

    private suspend fun saveProfileInformation(uiState: ProfileSetupUIState) {
        profileEditUseCase.invoke(
            UserProfileEdit(
                name = uiState.name,
                surname = uiState.surname,
                birthdate = uiState.birthdate,
                heightInCm = uiState.heightInCm,
                weightInKg = uiState.weightInKg,
                profilePhotoUri = uiState.profilePhotoUri
            )
        )
        _profileSetupEventsChannel.send(ProfileSetupEvent.ProfileSaved)
    }

    companion object {
        const val DEFAULT_HEIGHT = 170
        const val DEFAULT_WEIGHT = 70
    }
}
