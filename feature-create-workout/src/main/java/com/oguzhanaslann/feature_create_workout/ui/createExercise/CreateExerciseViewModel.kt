package com.oguzhanaslann.feature_create_workout.ui.createExercise

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.oguzhanaslann.common.State
import com.oguzhanaslann.common_domain.Exercise
import com.oguzhanaslann.common_domain.ExerciseSet
import com.oguzhanaslann.feature_create_workout.domain.usecase.CreateWorkoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateExerciseViewModel @Inject constructor(
    private val createWorkoutUseCase: CreateWorkoutUseCase,
) : ViewModel() {

    private val _exercises = MutableStateFlow<State<List<Exercise>>>(State.Initial)

    val exercises: LiveData<State<List<Exercise>>> = _exercises.asLiveData()

    private val _searchQuery = MutableStateFlow("")

    private var candidateExercise : Exercise? = null

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(500)
                .collectLatest {
                    when {
                        it.isEmpty() || it.isBlank() -> doEmptySearch()
                        else -> doSearch(it)
                    }
                }
        }
    }

    private fun doEmptySearch() {
        viewModelScope.launch {
            _exercises.update { State.Loading }
            createWorkoutUseCase.getExercises(10)
                .onEach {
                    Log.e("TAG", "doEmptySearch: $it")
                }
                .catch { _exercises.emit(State.Error(it.localizedMessage ?: "")) }
                .collectLatest {
                    _exercises.emit(State.Success(it))
                }
        }
    }

    private fun doSearch(query: String) {
        viewModelScope.launch {
            _exercises.update { State.Loading }
            createWorkoutUseCase.searchExercise(query)
                .catch { _exercises.emit(State.Error(it.localizedMessage ?: "")) }
                .collectLatest {
                    _exercises.emit(State.Success(it))
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.update { query }
    }

    fun getQuery(): String {
        return _searchQuery.value
    }

    fun setCandidateExercise(exercise: Exercise) {
        this.candidateExercise = exercise
    }

    fun getCandidateExercise(reps: Int, sets: Int): ExerciseSet? {
        return ExerciseSet(
            exercise = candidateExercise ?: return null,
            reps = reps,
            sets = sets
        )
    }
}
