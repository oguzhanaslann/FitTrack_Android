package com.oguzhanaslann.feature_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.oguzhanaslann.common.DateHelper
import com.oguzhanaslann.common_domain.AppLanguageUseCase
import com.oguzhanaslann.domain_profile.domain.ProfileRepository
import com.oguzhanaslann.feature_home.data.HomepageRepository
import com.oguzhanaslann.feature_home.domain.mapper.ProfileShortMapper
import com.oguzhanaslann.feature_home.domain.model.DailyWorkoutOverview
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomepageViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val appLanguageUseCase: AppLanguageUseCase,
    private val homepageRepository: HomepageRepository,
) : ViewModel() {

    private val _profileInformation = MutableStateFlow<ProfileShortInfoUIState?>(null)
    private val _todaysWorkoutOverview = MutableStateFlow<DailyWorkoutOverview?>(null)


    private val _homepageUIState = combine(
        _profileInformation,
        _todaysWorkoutOverview,
    ) { profileShortInfo, todaysWorkoutOverview ->
        HomepageUIState(
            profileShortInfo = profileShortInfo,
            todaysWorkoutOverview = todaysWorkoutOverview,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        HomepageUIState.initial()
    )

    val homepageUIState = _homepageUIState.asLiveData()

    private val mapper = ProfileShortMapper()

    init {
        viewModelScope.launch {
            profileRepository
                .getProfile()
                .catch { _profileInformation.value = null }
                .map { it.getOrNull()?.let { mapper.map(it) } }
                .map { photoShortInfo ->
                    val currentDateText = getCurrentDateText()
                    photoShortInfo?.let {
                        ProfileShortInfoUIState(
                            profileShortInfo = photoShortInfo,
                            currentDateText = currentDateText,
                        )
                    }
                }
                .collect { result ->
                    _profileInformation.value = result
                }
        }

        viewModelScope.launch {
            _todaysWorkoutOverview.emitAll(
                homepageRepository.getTodaysWorkoutOverview()
            )
        }
    }

    private fun getCurrentDateText(): String {
        val now = Date()
        return DateHelper.tryFormat(
            now,
            DateHelper.DAY_MONTH_WITH_NAME_YEAR_FORMAT,
            locale = appLanguageUseCase.getCurrentLocale()
        )
    }
}
