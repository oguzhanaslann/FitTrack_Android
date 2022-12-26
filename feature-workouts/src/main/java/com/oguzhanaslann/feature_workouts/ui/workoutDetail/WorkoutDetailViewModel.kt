package com.oguzhanaslann.feature_workouts.ui.workoutDetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhanaslann.common.State
import com.oguzhanaslann.common.isError
import com.oguzhanaslann.common.isLoading
import com.oguzhanaslann.common.onAny
import com.oguzhanaslann.common.onSuccess
import com.oguzhanaslann.feature_workouts.domain.repository.WorkoutsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WorkoutDetailState(
    val workoutDetail: State<WorkoutDetail>,
    val isActive: Boolean,
) {
    companion object {
        fun initial() = WorkoutDetailState(
            workoutDetail = State.Initial,
            isActive = false,
        )
    }
}

@HiltViewModel
class WorkoutDetailViewModel @Inject constructor(
    private val repository: WorkoutsRepository,
) : ViewModel() {

    private val _workoutDetail = MutableStateFlow<State<WorkoutDetail>>(State.Initial)
    private val _isActive = MutableStateFlow(false)

    val workoutDetailState = combine(
        _workoutDetail,
        _isActive
    ) { workoutDetail, isActive ->
        WorkoutDetailState(
            workoutDetail = workoutDetail,
            isActive = isActive
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        WorkoutDetailState.initial()
    )

    private val _workoutDetailEventChannel = Channel<WorkoutDetailEvent>()
    val workoutDetailEvent = _workoutDetailEventChannel.receiveAsFlow()

    fun getWorkoutDetail(id: String) {
        viewModelScope.launch {
            _workoutDetail.value = State.Loading
            repository.getWorkoutDetail(id = id)
                .catch { State.Error(it.localizedMessage ?: "") }
                .onEach { getIsActiveWorkout(id) }
                .collect { _workoutDetail.value = State.Success(it) }
        }
    }

    private suspend fun getIsActiveWorkout(id: String) {
        _isActive.value = false
        val isActive = repository.getIsActiveWorkout(id)
        Log.e("TAG", "getIsActiveWorkout: $isActive")
        _isActive.value = isActive
    }

    fun onStartWorkoutRequested(workoutId: String) {
        viewModelScope.launch {
            val hasActiveWorkout = repository.getHasUserActiveWorkout()
            if (hasActiveWorkout) {
                sendEventSuspend(WorkoutDetailEvent.PermissionNeeded)
            } else {
                tryStartingWorkout()
            }
        }
    }

    private fun tryStartingWorkout() {
        val detail = _workoutDetail.value
        detail.onSuccess {
            startWorkout(it)
        }.onAny(
            selector = { it.isLoading || it.isError || it is State.Initial },
            block = { sendEvent(WorkoutDetailEvent.CannotStartWorkout) }
        )
    }

    private fun startWorkout(workoutDetail: WorkoutDetail) {
        viewModelScope.launch {
            repository.startWorkout(workoutDetail)
            getIsActiveWorkout(workoutDetail.id)
        }
    }

    private suspend fun sendEventSuspend(event: WorkoutDetailEvent) {
        _workoutDetailEventChannel.send(event)
    }

    private fun sendEvent(event: WorkoutDetailEvent) {
        viewModelScope.launch {
            sendEventSuspend(event)
        }
    }
}

sealed class WorkoutDetailEvent {
    object PermissionNeeded : WorkoutDetailEvent()
    object StartWorkout : WorkoutDetailEvent()
    object CannotStartWorkout : WorkoutDetailEvent()
}
