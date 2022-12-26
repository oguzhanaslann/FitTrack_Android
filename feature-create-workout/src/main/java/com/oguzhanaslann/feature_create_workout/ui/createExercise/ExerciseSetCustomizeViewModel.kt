package com.oguzhanaslann.feature_create_workout.ui.createExercise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Integer.max
import java.lang.Integer.min

class ExerciseSetCustomizeViewModel : ViewModel() {

    private val _reps = MutableLiveData<Int>(MIN_REPS)
    val reps: LiveData<Int> = _reps

    private val _sets = MutableLiveData<Int>(MIN_SETS)
    val sets: LiveData<Int> = _sets

    fun increaseReps() {
        _reps.value = _reps.value?.plus(1)
    }

    fun decreaseReps() {
        _reps.value = max(_reps.value?.minus(1) ?: 0, MIN_REPS)
    }

    fun increaseSets() {
        _sets.value = _sets.value?.plus(1)
    }

    fun decreaseSets() {
        _sets.value = max(_sets.value?.minus(1) ?: 0, MIN_SETS)
    }

    fun getCurrentReps() = _reps.value ?: 0
    fun getCurrentSets() = _sets.value ?: 0

    companion object {
        private const val MIN_REPS = 1
        private const val MIN_SETS = 1
    }
}
