package com.oguzhanaslann.fittrack.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhanaslann.fittrack.domain.usecase.InitializeAppUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AppUiState(
    val isInitializing: Boolean,
    val hasSeenOnBoard: Boolean,
    val isAuthenticated: Boolean
) {
    companion object {
        fun default() = AppUiState(true, false, false)
    }
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val initializeAppUseCase: InitializeAppUseCase
) : ViewModel() {
    private val isInitializing = MutableStateFlow<Boolean>(true)
    private val hasSeenOnBoard = MutableStateFlow<Boolean>(false)
    private val isAuthenticated = MutableStateFlow<Boolean>(false)

    val appUiState: StateFlow<AppUiState> = combine(
        isInitializing,
        hasSeenOnBoard,
        isAuthenticated
    ) { isInitializing, hasSeenOnBoard, isAuthenticated ->
        AppUiState(
            isInitializing = isInitializing,
            hasSeenOnBoard = hasSeenOnBoard,
            isAuthenticated = isAuthenticated
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = AppUiState.default()
    )



    fun initializeApp() {

        viewModelScope.launch {
            isInitializing.emit(true)
            val appInitialization = initializeAppUseCase()
            hasSeenOnBoard.emit(appInitialization.hasSeenOnboarding)
            isAuthenticated.emit(appInitialization.hasAuthenticated)
            isInitializing.emit(false)
        }
    }

}
