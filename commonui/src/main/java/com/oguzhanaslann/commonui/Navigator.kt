package com.oguzhanaslann.commonui

import android.net.Uri
import androidx.navigation.NavController

interface Navigator {
    fun navigateToOnBoard(navController: NavController, onErrorAction: () -> Unit = {})
    fun navigateToAuthentication(navController: NavController, onErrorAction: () -> Unit = {})
}

fun Navigator(): Navigator {
    return object : Navigator {
        override fun navigateToOnBoard(navController: NavController, onErrorAction: () -> Unit) {
            navigateSafe(
                navController = navController,
                deeplinkUri = DeeplinkBuilder.asUri(DeeplinkBuilder.ONBOARD_DEEPLINK),
                onErrorAction = onErrorAction
            )
        }

        override fun navigateToAuthentication(
            navController: NavController,
            onErrorAction: () -> Unit
        ) {
            navigateSafe(
                navController = navController,
                deeplinkUri = DeeplinkBuilder.asUri(DeeplinkBuilder.AUTHENTICATION_DEEPLINK),
                onErrorAction = onErrorAction
            )
        }

        @Suppress("SwallowedException")
        private fun navigateSafe(
            navController: NavController,
            deeplinkUri: Uri,
            onErrorAction: () -> Unit = {},
        ) {
            try {
                navController.navigate(deeplinkUri)
            } catch (navException: IllegalArgumentException) {
                onErrorAction()
            }
        }
    }
}
