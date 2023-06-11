package com.andreikslpv.thekitchen.presentation.ui

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.admin.AdminUtils
import com.andreikslpv.thekitchen.databinding.ActivityMainBinding
import com.andreikslpv.thekitchen.domain.usecases.InitApplicationSettingsUseCase
import com.andreikslpv.thekitchen.presentation.ui.fragments.TabsFragment
import com.andreikslpv.thekitchen.presentation.vm.MainViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/**
 * Container for all screens in the app.
 */

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var initApplicationSettingsUseCase: InitApplicationSettingsUseCase

    @Inject
    lateinit var firestore: FirebaseFirestore
    @Inject
    lateinit var storage: FirebaseStorage

    // nav controller of the current screen
    private var navController: NavController? = null

    private val topLevelDestinations = setOf(getTabsDestination(), getAuthDestination())

    // fragment listener is sued for tracking current nav controller
    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            if (f is TabsFragment || f is NavHostFragment) return
            onNavControllerActivated(f.findNavController())
        }
    }

    init {
        App.instance.dagger.inject(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AdminUtils.uploadDb(firestore, storage)

        getAuthState()
        initApplicationSettings()

        // preparing root nav controller
        val navController = getRootNavController()
        prepareRootNavController(isSignedIn(), navController)
        onNavControllerActivated(navController)

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)
    }

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        navController = null
        super.onDestroy()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (isStartDestination(navController?.currentDestination)) {
            super.onBackPressed()
        } else {
            navController?.popBackStack()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    @ExperimentalCoroutinesApi
    private fun getAuthState() {
        viewModel.getAuthState().observe(this) {
            viewModel.startObserveUser()
        }
    }

    private fun initApplicationSettings() {
        // устанавливаем сохраненные настройки приложения
        initApplicationSettingsUseCase.execute()
    }

    override fun onSupportNavigateUp(): Boolean =
        (navController?.navigateUp() ?: false) || super.onSupportNavigateUp()

    private fun getRootNavController(): NavController {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        return navHost.navController
    }

    private fun prepareRootNavController(isSignedIn: Boolean, navController: NavController) {
        val graph = navController.navInflater.inflate(getMainNavigationGraphId())
        graph.setStartDestination(
            if (isSignedIn) {
                getTabsDestination()
            } else {
                getAuthDestination()
            }
        )
        navController.graph = graph
    }

    private fun onNavControllerActivated(navController: NavController) {
        if (this.navController == navController) return
        this.navController?.removeOnDestinationChangedListener(destinationListener)
        navController.addOnDestinationChangedListener(destinationListener)
        this.navController = navController
    }

    private val destinationListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.setDisplayHomeAsUpEnabled(!isStartDestination(destination))
        }

    private fun isStartDestination(destination: NavDestination?): Boolean {
        if (destination == null) return false
        val graph = destination.parent ?: return false
        val startDestinations = topLevelDestinations + graph.startDestinationId
        return startDestinations.contains(destination.id)
    }

    private fun isSignedIn(): Boolean = viewModel.isUserAuthenticated

    private fun getMainNavigationGraphId(): Int = R.navigation.graph_main

    private fun getTabsDestination(): Int = R.id.tabsFragment

    private fun getAuthDestination(): Int = R.id.authFragment

}