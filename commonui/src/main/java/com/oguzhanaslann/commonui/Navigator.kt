package com.oguzhanaslann.commonui

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import timber.log.Timber

interface Navigator {
    fun navigateToOnBoard(
        navController: NavController,
        navOptions: NavOptions? = null,
        onErrorAction: () -> Unit = {}
    )

    fun navigateToAuthentication(
        navController: NavController,
        navOptions: NavOptions? = null,
        clearBackStack: Boolean = false,
        onErrorAction: () -> Unit = {}
    )

    fun navigateToHome(
        navController: NavController,
        navOptions: NavOptions? = null,
        onErrorAction: () -> Unit = {}
    )

    fun navigateToCreateWorkout(
        navController: NavController,
        navOptions: NavOptions? = null,
        onErrorAction: () -> Unit = {}
    )

    fun navigateToWorkoutDetail(
        navController: NavController,
        workoutId: String,
        navOptions: NavOptions? = null,
        onErrorAction: () -> Unit = {}
    )
}

fun Navigator(): Navigator {
    return object : Navigator {
        override fun navigateToOnBoard(
            navController: NavController,
            navOptions: NavOptions?,
            onErrorAction: () -> Unit
        ) {
            navigateSafe(
                navController = navController,
                deeplinkUri = DeeplinkBuilder.asUri(DeeplinkBuilder.ONBOARD_DEEPLINK),
                navOptions = navOptions,
                onErrorAction = onErrorAction
            )
        }

        override fun navigateToAuthentication(
            navController: NavController,
            navOptions: NavOptions?,
            clearBackStack: Boolean,
            onErrorAction: () -> Unit
        ) {

            if (clearBackStack) {
                navController.popBackStack(navController.graph.startDestinationId, true)
            }


            navigateSafe(
                navController = navController,
                deeplinkUri = DeeplinkBuilder.asUri(DeeplinkBuilder.AUTHENTICATION_DEEPLINK),
                navOptions = navOptions,
                onErrorAction = onErrorAction
            )
        }

        override fun navigateToHome(
            navController: NavController,
            navOptions: NavOptions?,
            onErrorAction: () -> Unit
        ) {
            navigateSafe(
                navController = navController,
                deeplinkUri = DeeplinkBuilder.asUri(DeeplinkBuilder.HOME_DEEPLINK),
                navOptions = navOptions,
                onErrorAction = onErrorAction
            )
        }

        @Suppress("SwallowedException")
        private fun navigateSafe(
            navController: NavController,
            deeplinkUri: Uri,
            navOptions: NavOptions? = null,
            onErrorAction: () -> Unit = {}
        ) {
            try {
                navController.navigate(deeplinkUri,navOptions)
            } catch (navException: IllegalArgumentException) {
                Timber.e(navException.message)
                onErrorAction()
            }
        }

        override fun navigateToCreateWorkout(
            navController: NavController,
            navOptions: NavOptions?,
            onErrorAction: () -> Unit
        ) {
            navigateSafe(
                navController = navController,
                deeplinkUri = DeeplinkBuilder.asUri(DeeplinkBuilder.CREATE_WORKOUT_DEEPLINK),
                navOptions = navOptions,
                onErrorAction = onErrorAction
            )
        }

        override fun navigateToWorkoutDetail(
            navController: NavController,
            workoutId: String,
            navOptions: NavOptions?,
            onErrorAction: () -> Unit
        ) {
            navigateSafe(
                navController = navController,
                deeplinkUri = DeeplinkBuilder
                    .asUri(DeeplinkBuilder.WORKOUT_DETAIL_DEEPLINK, workoutId(workoutId)),
                navOptions = navOptions,
                onErrorAction = onErrorAction
            )
        }
    }
}
