package com.oguzhanaslann.fittrack.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
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

        binding.addNewItemFab?.setOnClickListener {
            navigator.navigateToCreateWorkout(navController)
        }

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
            val isNavigationPage = isNavigationPage(destinationId)
            val shouldShowAppBar = shouldShowAppBar(isNavigationPage, destinationId)
            binding.appBarLayout.isVisible = shouldShowAppBar
            binding.bottomNavigationView?.isVisible = isNavigationPage
            binding.addNewItemFab?.isVisible = isNavigationPage
            setToolbarTitleBy(isNavigationPage)
        }
    }

    private fun isNavigationPage(destinationId: Int) =
        (destinationId == com.oguzhanaslann.feature_home.R.id.homepageFragment
                || destinationId == com.oguzhanaslann.feature_workouts.R.id.workoutsFragment
                || destinationId == com.oguzhanaslann.feature_reports.R.id.reportsFragment
                || destinationId == com.oguzhanaslann.feature_profile.R.id.profileFragment)

    private fun shouldShowAppBar(
        isNavigationPage: Boolean,
        destinationId: Int,
    ): Boolean {
        val createWorkoutDestinationId =
            com.oguzhanaslann.feature_create_workout.R.id.createWorkoutFragment

        return isNavigationPage || destinationId == createWorkoutDestinationId
    }

    private fun setToolbarTitleBy(isNavigationPage: Boolean) {
        if (isNavigationPage) {
            binding.toolbar.title = ""
        }
    }

    private fun initNavController() {
        val host = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment? ?: return
        navController = host.navController
    }

    private fun setUpBottomNavigation() {
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val createResult =  super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_main, menu)
        return createResult
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

        /**
         * a work around to prevent default label from showing on toolbar
         * */
        binding.toolbar.title = ""
    }

    companion object {
        private const val MIDDLE_ELEMENT_INDEX = 2
    }
}
