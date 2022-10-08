package com.oguzhanaslann.feature_onboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhanaslann.common.State
import com.oguzhanaslann.common.toState
import com.oguzhanaslann.feature_onboard.data.OnboardingRepository
import com.oguzhanaslann.feature_onboard.domain.OnboardUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class OnboardViewModel @Inject constructor(
    private val onboardRepository: OnboardingRepository
) : ViewModel() {

    private val currentOnboardPageState = MutableStateFlow<Int>(FIRST_PAGE)
    private val onBoardMarkAsSeenState = MutableStateFlow<State<Boolean>>(State.Initial)

    val onboardUIState = combine(
        currentOnboardPageState,
        onBoardMarkAsSeenState
    ) { currentPage, markAsSeenState ->
        OnboardUIState(
            currentPage = currentPage,
            markAsSeenState = markAsSeenState
        )
    }

    fun onFinishOnboarding() {
        viewModelScope.launch {
            onBoardMarkAsSeenState.emit(State.Loading)
            val result = onboardRepository.markOnboardingAsFinished()
            onBoardMarkAsSeenState.emit(result.toState())
        }
    }

    fun onPageChanged(page: Int) {
        currentOnboardPageState.update { page }
    }

    fun onNextPage() {
        currentOnboardPageState.update { min(it + 1, LAST_PAGE) }
    }

    companion object {
        private const val FIRST_PAGE = 0
        private const val TOTAL_PAGE_COUNT = 3
        private const val LAST_PAGE = TOTAL_PAGE_COUNT - 1
    }
}
