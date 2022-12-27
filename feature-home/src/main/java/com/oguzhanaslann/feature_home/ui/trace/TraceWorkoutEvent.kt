package com.oguzhanaslann.feature_home.ui.trace

sealed class TraceWorkoutEvent {
    object WorkoutCompleted : TraceWorkoutEvent()

}
