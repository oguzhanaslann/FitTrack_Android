package com.oguzhanaslann.feature_home.ui.trace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.oguzhanaslann.common_domain.DailyPlan
import com.oguzhanaslann.common_domain.ExerciseSet
import com.oguzhanaslann.feature_home.data.TraceWorkoutRepository
import com.oguzhanaslann.feature_home.domain.model.TraceExercise
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TraceWorkoutViewModel @Inject constructor(
    private val repository: TraceWorkoutRepository,
) : ViewModel() {
    private val _dailyPlan = MutableStateFlow<DailyPlan?>(null)
    private val checkedExercises = MutableStateFlow<List<ExerciseSet>>(emptyList())

    private val _traceWorkoutViewState = combine(
        _dailyPlan,
        checkedExercises
    ) { dailyPlan, checkedExercises ->
        TraceWorkoutViewState(
            name = dailyPlan?.name ?: "",
            traceExercises = dailyPlan?.exerciseList?.map { exerciseSet ->
                TraceExercise(
                    exerciseSet = exerciseSet,
                    isChecked = checkedExercises.contains(exerciseSet)
                )
            } ?: emptyList()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = TraceWorkoutViewState(
            name = "",
            traceExercises = listOf()
        )
    )

    val traceWorkoutViewState = _traceWorkoutViewState.asLiveData()

    private val _traceWorkoutEventChannel = Channel<TraceWorkoutEvent>()
    val traceWorkoutEvent = _traceWorkoutEventChannel.receiveAsFlow()

    fun getDailyPlan(dailyPlanId: String) {
        viewModelScope.launch {
            _dailyPlan.value = repository.getDailyPlan(dailyPlanId)
        }
    }

    fun onExerciseClicked(exercise: TraceExercise) {
        checkedExercises.value = if (checkedExercises.value.contains(exercise.exerciseSet)) {
            checkedExercises.value - exercise.exerciseSet
        } else {
            checkedExercises.value + exercise.exerciseSet
        }
    }

    fun completeDailyPlan(dailyPlanId: String) {
        viewModelScope.launch {
            val traceExercises = _traceWorkoutViewState.value.traceExercises
            repository.doneWorkout(dailyPlanId, traceExercises)
            _traceWorkoutEventChannel.send(TraceWorkoutEvent.WorkoutCompleted)
        }
    }
}
