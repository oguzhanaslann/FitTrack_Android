package com.oguzhanaslann.fittrack.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.oguzhanaslann.commonui.Navigator
import com.oguzhanaslann.commonui.activityViewBinding
import com.oguzhanaslann.fittrack.R
import com.oguzhanaslann.fittrack.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.Locale

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by activityViewBinding { ActivityMainBinding.inflate(layoutInflater) }

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var navController: NavController

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
        setUpSplashScreen(splashScreen)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        initNavController()
        setUpNavigationListener()
        setUpBottomNavigation()
        setUpDrawerNavigation()

        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun setUpSplashScreen(splashScreen: SplashScreen) {
        splashScreen.setKeepOnScreenCondition { mainViewModel.appUiState.value.isInitializing }
        splashScreen.setOnExitAnimationListener {
            when {
                mainViewModel.currentState().hasSeenOnBoard.not() -> navigateOnboard()
                mainViewModel.currentState().isAuthenticated.not() -> navigateAuthentication()
                else -> Unit // navigateHome()
            }

            it.remove()
        }
    }

    private fun setUpNavigationListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val destinationId = destination.id
            val isNavigationPage =
                destinationId == com.oguzhanaslann.feature_home.R.id.homepageFragment
                        || destinationId == com.oguzhanaslann.feature_workouts.R.id.workoutsFragment
                        || destinationId == com.oguzhanaslann.feature_reports.R.id.reportsFragment
                        || destinationId == com.oguzhanaslann.feature_profile.R.id.profileFragment

            binding.appBarLayout.isVisible = isNavigationPage
            binding.bottomNavigationView?.isVisible = isNavigationPage
            binding.addNewItemFab?.isVisible = isNavigationPage
            if (isNavigationPage) {
                binding.toolbar.title = ""
            }
        }
    }

    private fun initNavController() {
        val host = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment? ?: return
        navController = host.navController
    }

    override fun onResume() {
        super.onResume()

        /**
         * a work around to prevent default label from showing on toolbar
         * */
        binding.toolbar.title = ""
    }

    private fun setUpBottomNavigation(){
        binding.bottomNavigationView?.run {
            background = null
            menu.getItem(MIDDLE_ELEMENT_INDEX).isEnabled = false
            setupWithNavController(navController)
        }
    }

    private fun setUpDrawerNavigation() {
        binding.navView?.setupWithNavController(navController)
        val drawerLayout = binding.root as? DrawerLayout
        drawerLayout?.let {
            binding.toolbar.setNavigationOnClickListener {
                drawerLayout.open()
            }
        }
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
