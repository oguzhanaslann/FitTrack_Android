package com.oguzhanaslann.feature_create_workout.ui.createDailyPlan

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.oguzhanaslann.common.orZero
import com.oguzhanaslann.feature_create_workout.domain.DailyPlanToBeSaved
import com.oguzhanaslann.feature_create_workout.domain.ExerciseSet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateDailyPlanViewModel @Inject constructor(

) : ViewModel() {
    private val _calories = MutableStateFlow<Int>(0)
    private val _planTitle = MutableStateFlow<String>("")
    private val _coverPhoto = MutableStateFlow<Uri?>(null)
    private val _exerciseSet = MutableStateFlow<List<ExerciseSet>>(emptyList())

    private val _userExerciseSet = _exerciseSet.map {
        it.map { exerciseSet ->
            ExerciseSetUIModel(
                name = exerciseSet.exercise.name,
                imageUrl = exerciseSet.exercise.imageUrl,
                sets = exerciseSet.sets.orZero(),
                reps = exerciseSet.reps.orZero(),
                weight = exerciseSet.weight.orZero(),
                restMillis = exerciseSet.rest.orZero()
            )
        }
    }

    private val _dailyPlanUIState = combine(
        _calories,
        _planTitle,
        _coverPhoto,
        _userExerciseSet
    ) { calories, title, photo, exercises ->
        CreateDailyPlanUIState(
            name = title,
            dailyPlanPhotoUri = photo,
            exercises = exercises,
            calories = calories
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CreateDailyPlanUIState()
    )

    val dailyPlanUIState = _dailyPlanUIState.asLiveData()

    val calories = _calories.asLiveData()

    private val _createDailyPlanEventsChannel = Channel<CreateDailyPlanEvent>()
    val createDailyPlanEvents = _createDailyPlanEventsChannel.receiveAsFlow()

    fun onTitleChanged(title: String) {
        _planTitle.update { title }
    }

    fun onPhotoSelected(uri: Uri) {
        _coverPhoto.update { uri }
    }

    fun onCaloriesChanged(calorieValue: Int) {
        _calories.update { calorieValue }
    }

    fun getCurrentTitle(): String {
        return _planTitle.value
    }

    fun getCurrentCalories(): Int {
        return _calories.value
    }

    fun onConfirmClicked() {
        viewModelScope.launch {

            val current = _dailyPlanUIState.value
            when {
                current.name.isEmpty() -> {
                    _createDailyPlanEventsChannel.send(CreateDailyPlanEvent.DailyPlanNameEmpty)
                }

                current.calories == 0 -> {
                    _createDailyPlanEventsChannel.send(CreateDailyPlanEvent.DailyPlanCaloriesEmpty)
                }

                current.exercises.isEmpty() -> {
                    _createDailyPlanEventsChannel.send(CreateDailyPlanEvent.DailyPlanExerciseEmpty)
                }

                else -> {
                    val dailyPlan = createDailyPlanInstance()
                    _createDailyPlanEventsChannel.send(CreateDailyPlanEvent.DailyPlanCreated(dailyPlan))
                }
            }
        }
    }

    private fun createDailyPlanInstance(): DailyPlanToBeSaved {
        return DailyPlanToBeSaved(
            name = _planTitle.value,
            imageUri = _coverPhoto.value,
            calories = _calories.value,
            exerciseList = _exerciseSet.value
        )
    }

    fun addExercise(exerciseSet: ExerciseSet) {
        _exerciseSet.update { it + exerciseSet }
    }
}
