package com.oguzhanaslann.fittrack.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.oguzhanaslann.commonui.Navigator
import com.oguzhanaslann.fittrack.R
import com.oguzhanaslann.fittrack.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

//    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

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

            it.remove()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
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
}
