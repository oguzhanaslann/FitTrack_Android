package com.oguzhanaslann.feature_workouts.ui.workoutDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhanaslann.common.State
import com.oguzhanaslann.feature_workouts.domain.repository.WorkoutsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutDetailViewModel @Inject constructor(
    private val repository: WorkoutsRepository,
) : ViewModel() {

    private val _workoutDetail = MutableStateFlow<State<WorkoutDetail>>(State.Initial)
    val workoutDetail: StateFlow<State<WorkoutDetail>>
        get() = _workoutDetail

    fun getWorkoutDetail(id: String) {
        viewModelScope.launch {
            _workoutDetail.value = State.Loading
            repository.getWorkoutDetail(id = id)
                .catch { State.Error(it.localizedMessage ?: "") }
                .collect { _workoutDetail.value = State.Success(it) }
        }
    }
}
