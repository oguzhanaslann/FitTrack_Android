package com.oguzhanaslann.feature_create_workout.ui

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.oguzhanaslann.feature_create_workout.domain.DailyPlan
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
class CreateWorkoutViewModel @Inject constructor(

) : ViewModel() {

    private val _workoutTitle = MutableStateFlow<String>("")
    private val _workoutDescription = MutableStateFlow<String>("")
    private val _coverPhoto = MutableStateFlow<Uri?>(null)

    private val addPlans = MutableStateFlow<List<DailyPlan>>(emptyList())

    private val _userDailyPlans = addPlans.map {
        it.map { dailyPlan ->
            Log.e("TAG", "mapping")
            DailyPlanUIModel(
                name = dailyPlan.name,
                calories = dailyPlan.calories,
                exerciseCount = dailyPlan.exerciseList.size
            )
        }
    }

    private val _workoutUIState = combine(
        _workoutTitle,
        _workoutDescription,
        _coverPhoto,
        _userDailyPlans
    ) { title, description, photo, plans ->
        CreateWorkoutUIState(
            name = title,
            description = description,
            workoutPhotoUri = photo,
            planList = plans
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CreateWorkoutUIState()
    )

    val workoutUIState = _workoutUIState.asLiveData()

    val descriptionLength = _workoutDescription.map { it.length }.asLiveData()

    private val _createWorkoutEventsChannel = Channel<CreateWorkoutEvent>()
    val createWorkoutEvents = _createWorkoutEventsChannel.receiveAsFlow()

    fun onDescriptionChanged(description: String) {
        _workoutDescription.update { description }
    }

    fun onTitleChanged(title: String) {
        _workoutTitle.update { title }
    }

    fun onPhotoSelected(uri: Uri) {
        _coverPhoto.update { uri }
    }

    fun onPublishClicked() {
        viewModelScope.launch {
            val currentState = _workoutUIState.value
            when {
                currentState.name.isEmpty() ->
                    _createWorkoutEventsChannel.send(CreateWorkoutEvent.WorkoutNameEmpty)

                currentState.description.isEmpty() ->
                    _createWorkoutEventsChannel.send(CreateWorkoutEvent.WorkoutDescriptionEmpty)

                currentState.planList.isEmpty() ->
                    _createWorkoutEventsChannel.send(CreateWorkoutEvent.WorkoutPlanEmpty)

                else -> createWorkout()
            }
        }
    }

    private suspend fun createWorkout() {

        _createWorkoutEventsChannel.send(CreateWorkoutEvent.WorkoutCreated)
    }

    fun getCurrentTitle(): String {
        return _workoutTitle.value
    }

    fun getCurrentDescription(): String {
        return _workoutDescription.value
    }

    fun onDailyPlanCreated(plan: DailyPlan) {
        addPlans.value = addPlans.value + plan
    }

    companion object {
        const val MAX_DESCRIPTION_LENGTH = 200
    }
}

sealed class CreateWorkoutEvent {
    object WorkoutNameEmpty : CreateWorkoutEvent()
    object WorkoutDescriptionEmpty : CreateWorkoutEvent()
    object WorkoutPlanEmpty : CreateWorkoutEvent()
    object WorkoutCreated : CreateWorkoutEvent()
}
