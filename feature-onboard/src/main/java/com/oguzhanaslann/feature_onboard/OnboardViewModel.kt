package com.oguzhanaslann.feature_onboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhanaslann.common.State
import com.oguzhanaslann.common.toState
import com.oguzhanaslann.feature_onboard.data.OnboardingRepository
import com.oguzhanaslann.feature_onboard.domain.OnboardUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

@HiltViewModel
class OnboardViewModel @Inject constructor(
    private val onboardRepository: OnboardingRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val currentOnboardPageState =
        savedStateHandle.getStateFlow(CURRENT_PAGE_KEY, FIRST_PAGE)
    private val onBoardMarkAsSeenState = MutableStateFlow<State<Boolean>>(State.Initial)

    val onboardUIState = combine(
        currentOnboardPageState,
        onBoardMarkAsSeenState
    ) { currentPage, markAsSeenState ->
        OnboardUIState(
            currentPage = currentPage,
            markAsSeenState = markAsSeenState
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        OnboardUIState(currentOnboardPageState.value, onBoardMarkAsSeenState.value)
    )

    fun onFinishOnboarding() {
        viewModelScope.launch {
            onBoardMarkAsSeenState.emit(State.Loading)
            val result = onboardRepository.markOnboardingAsFinished()
            onBoardMarkAsSeenState.emit(result.toState())
        }
    }

    fun onPageChanged(page: Int) {
        savedStateHandle[CURRENT_PAGE_KEY] = page
    }

    fun onNextPage() {
        savedStateHandle[CURRENT_PAGE_KEY] = min(currentOnboardPageState.value + 1, LAST_PAGE)

    }

    fun canGoBack(): Boolean {
        return currentOnboardPageState.value > FIRST_PAGE
    }

    fun goBack() {
        savedStateHandle[CURRENT_PAGE_KEY] = max(currentOnboardPageState.value - 1, FIRST_PAGE)
    }

    fun getCurrentPage(): Int {
        return onboardUIState.value.currentPage
    }

    companion object {
        private const val CURRENT_PAGE_KEY = "currentOnboardPage"

        private const val FIRST_PAGE = 0
        private const val TOTAL_PAGE_COUNT = 3
        private const val LAST_PAGE = TOTAL_PAGE_COUNT - 1
    }
}
