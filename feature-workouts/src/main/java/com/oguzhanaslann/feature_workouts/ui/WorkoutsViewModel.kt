package com.oguzhanaslann.feature_workouts.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.oguzhanaslann.common.State
import com.oguzhanaslann.common.mapBy
import com.oguzhanaslann.common.mapByStateSuspend
import com.oguzhanaslann.feature_workouts.domain.mapper.WorkoutSearchItemMapper
import com.oguzhanaslann.feature_workouts.domain.model.Workout
import com.oguzhanaslann.feature_workouts.domain.repository.WorkoutsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutsViewModel @Inject constructor(
    private val repository: WorkoutsRepository,
) : ViewModel() {
    private val _workouts = MutableStateFlow<State<List<Workout>>>(State.Initial)
    val workouts: LiveData<State<List<WorkoutSearchItem>>> = _workouts.map {
        it.mapByStateSuspend {
            it.mapBy(mapper)
        }
    }.asLiveData()

    private val mapper = WorkoutSearchItemMapper()

    private val _searchQuery = MutableStateFlow("")

    val isLoading = _workouts.map { it is State.Loading }.asLiveData()
    val isError = _workouts.map { it is State.Error }.asLiveData()

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

    private suspend fun doEmptySearch() {
        _workouts.update { State.Loading }
        repository.getWorkouts(10)
            .catch { _workouts.emit(State.Error(it.localizedMessage ?: "")) }
            .collectLatest {
                _workouts.emit(State.Success(it))
            }
    }

    private fun doSearch(query: String) {
        viewModelScope.launch {
            _workouts.update { State.Loading }
            repository.searchWorkouts(query)
                .catch { _workouts.emit(State.Error(it.localizedMessage ?: "")) }
                .collectLatest {
                    _workouts.emit(State.Success(it))
                }
        }
    }

    fun search(query: String) {
        _searchQuery.update { query } // make sure it is thread safe
    }

    fun currentQuery(): String = _searchQuery.value
}
