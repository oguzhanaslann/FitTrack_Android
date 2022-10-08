package com.oguzhanaslann.commonui

import androidx.navigation.NavController

interface Navigator {
    fun navigateToOnBoard( navController: NavController)
    fun navigateToAuthentication( navController: NavController)
}

fun Navigator(): Navigator {
    return object : Navigator {
        override fun navigateToOnBoard( navController: NavController) {
            navController.navigate(DeeplinkBuilder.asUri(DeeplinkBuilder.ONBOARD_DEEPLINK))
        }

        override fun navigateToAuthentication( navController: NavController) {
            navController.navigate(DeeplinkBuilder.asUri(DeeplinkBuilder.AUTHENTICATION_DEEPLINK))
        }
    }
}
