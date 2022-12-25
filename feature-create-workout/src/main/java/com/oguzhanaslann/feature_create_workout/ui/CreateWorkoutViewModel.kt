package com.oguzhanaslann.feature_create_workout.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class DailyPlanUIModel(
    val id: Int,
    val name: String,
    val calories: Int,
    val exerciseCount: Int
)

data class CreateWorkoutUIState(
    val name: String = "",
    val description: String = "",
    val workoutPhotoUri: Uri? = null,
    val planList: List<DailyPlanUIModel> = emptyList()
)

@HiltViewModel
class CreateWorkoutViewModel @Inject constructor(

) : ViewModel() {

    private val _workoutTitle = MutableStateFlow<String>("")
    private val _workoutDescription = MutableStateFlow<String>("")
    private val _userProfilePhoto = MutableStateFlow<Uri?>(null)
    private val _userDailyPlans = MutableStateFlow<List<DailyPlanUIModel>>(emptyList())

    val workoutUIState = combine(
        _workoutTitle,
        _workoutDescription,
        _userProfilePhoto,
        _userDailyPlans
    ) { title, description, photo , plans ->
        CreateWorkoutUIState(
            name = title,
            description = description,
            workoutPhotoUri = photo,
            planList = plans
        )
    }.asLiveData()

    val descriptionLength = _workoutDescription.map { it.length }.asLiveData()

    fun onDescriptionChanged(description: String) {
        _workoutDescription.update { description }
    }

    fun onTitleChanged(title: String) {
        _workoutTitle.update { title }
    }

    fun onPhotoSelected(uri: Uri) {
        _userProfilePhoto.update { uri }
    }

    fun onPublishClicked() {
        //TODO("Not yet implemented")
    }

    fun getCurrentTitle(): String {
        return _workoutTitle.value
    }

    fun getCurrentDescription(): String {
        return _workoutDescription.value
    }

    companion object {
        const val MAX_DESCRIPTION_LENGTH = 200
    }
}
