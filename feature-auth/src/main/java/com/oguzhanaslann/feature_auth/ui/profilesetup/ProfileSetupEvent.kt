package com.oguzhanaslann.feature_auth.ui.profilesetup

sealed class ProfileSetupEvent {
    object NameEmpty : ProfileSetupEvent()
    object SurnameEmpty : ProfileSetupEvent()
    object BirthdateEmpty : ProfileSetupEvent()
    object HeightEmpty : ProfileSetupEvent()
    object WeightEmpty : ProfileSetupEvent()
    object ProfileSaved : ProfileSetupEvent()
}
