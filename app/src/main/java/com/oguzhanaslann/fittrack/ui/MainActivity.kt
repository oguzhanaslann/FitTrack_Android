package com.oguzhanaslann.fittrack.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.oguzhanaslann.commonui.Navigator
import com.oguzhanaslann.commonui.activityViewBinding
import com.oguzhanaslann.fittrack.R
import com.oguzhanaslann.fittrack.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by activityViewBinding { ActivityMainBinding.inflate(layoutInflater) }

    private val mainViewModel: MainViewModel by viewModels()

    private val navController
        get() = findNavController(R.id.nav_host_fragment_content_main)

    private val navigator = Navigator()

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            isEnabled = navController.popBackStack()
            if (!isEnabled) {
                onBackPressedDispatcher.onBackPressed()
            }
            Timber.d("MainActivity handleOnBackPressed: handled, isEnabled: $isEnabled")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        mainViewModel.initializeApp()
        splashScreen.setKeepOnScreenCondition { mainViewModel.appUiState.value.isInitializing }
        splashScreen.setOnExitAnimationListener {
            when {
                mainViewModel.currentState().hasSeenOnBoard.not() -> navigateOnboard()
                mainViewModel.currentState().isAuthenticated.not() -> navigateAuthentication()
                else -> Unit // navigateHome()
            }

            Handler(Looper.getMainLooper()).postDelayed({
                it.remove()
                binding.bottomNavigationView.setupWithNavController(navController)

                navController.addOnDestinationChangedListener { _, destination , _ ->
                    val destinationId = destination.id
                    val isOnBoard = destinationId == com.oguzhanaslann.feature_onboard.R.id.onboardFragment
                    val isSignIn = destinationId == com.oguzhanaslann.feature_auth.R.id.signInFragment
                    val isSignUp = destinationId == com.oguzhanaslann.feature_auth.R.id.signUpFragment
                    val isAuthentication = isSignIn || isSignUp
                    binding.appBarLayout.isVisible = !(isOnBoard || isAuthentication)
                    binding.bottomAppBar.isVisible = !(isOnBoard || isAuthentication)
                    binding.addNewItemFab.isVisible = !(isOnBoard || isAuthentication)
                }
            }, 100)
        }

        setContentView(binding.root)
        setUpBottomNavigation()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }


    private fun setUpBottomNavigation() = binding.bottomNavigationView.run {
        background = null
        menu.getItem(MIDDLE_ELEMENT_INDEX).isEnabled = false
    }


    private fun navigateOnboard() {
        navigator.navigateToOnBoard(
            navController,
            NavOptions.Builder()
                .setPopUpTo(com.oguzhanaslann.feature_home.R.id.nav_graph_home, true)
                .build()
        ) {
            Timber.e("Navigation to OnBoard failed")
        }

    }

    private fun navigateAuthentication() {
        navigator.navigateToAuthentication(
            navController,
            NavOptions.Builder()
                .setPopUpTo(com.oguzhanaslann.feature_home.R.id.nav_graph_home, true)
                .build()
        ) {
            Timber.e("Navigation to authentication failed")
        }
    }

    companion object {
        private const val MIDDLE_ELEMENT_INDEX = 2
    }
}
