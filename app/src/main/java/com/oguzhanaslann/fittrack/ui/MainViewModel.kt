package com.oguzhanaslann.fittrack.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class MainViewModel @Inject constructor() : ViewModel() {

    private val _appUiState = MutableStateFlow(AppUiState.default())
    val appUiState: StateFlow<AppUiState> = _appUiState

    val isInitializing = _appUiState.map { it.isInitializing }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )

    fun initializeApp() {
        _appUiState.value = _appUiState.value!!.copy(isInitializing = true)
        viewModelScope.launch {
            delay(1500)
            _appUiState.value = _appUiState.value!!.copy(
                isInitializing = false,
                hasSeenOnBoard = false,
                isAuthenticated = false
            )
        }
    }

}
