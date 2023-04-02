package com.andreikslpv.thekitchen.presentation.ui

import android.os.Bundle
import android.view.View
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
import com.andreikslpv.thekitchen.data.db.FirestoreConstants.PATH_CATEGORY
import com.andreikslpv.thekitchen.databinding.ActivityMainBinding
import com.andreikslpv.thekitchen.domain.models.Category
import com.andreikslpv.thekitchen.domain.usecases.InitApplicationSettingsUseCase
import com.andreikslpv.thekitchen.presentation.ui.fragments.TabsFragment
import com.andreikslpv.thekitchen.presentation.vm.MainViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.regex.Pattern
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

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        CoroutineScope(Dispatchers.IO).launch {
//            addToDb()
//        }

        initApplicationSettings()

        // preparing root nav controller
        val navController = getRootNavController()
        prepareRootNavController(isSignedIn(), navController)
        onNavControllerActivated(navController)

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)
    }

    private suspend fun addToDb() {

        val collection = firestore.collection(PATH_CATEGORY)
        var id = "ca00000"
        var image = ""
        try {
            image = storage.reference.child("category/$id.png").downloadUrl.await().toString() ?: ""
        } catch (e: Exception) {

        }
        var document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "category",
                type = "ct00000",
                image = image,
            )
        ).await()

        id = "ca00001"
        try {
            image = storage.reference.child("category/$id.png").downloadUrl.await().toString() ?: ""
        } catch (e: Exception) {

        }
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "до 30 минут",
                type = "ct00001",
                image = image,
            )
        ).await()

        id = "ca00002"
        try {
            image = storage.reference.child("category/$id.png").downloadUrl.await().toString() ?: ""
        } catch (e: Exception) {

        }
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "до 45 минут",
                type = "ct00001",
                image = image,
            )
        ).await()

        id = "ca00003"
        try {
            image = storage.reference.child("category/$id.png").downloadUrl.await().toString() ?: ""
        } catch (e: Exception) {

        }
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "до 60 минут",
                type = "ct00001",
                image = image,
            )
        ).await()

        id = "ca00004"
        try {
            image = storage.reference.child("category/$id.png").downloadUrl.await().toString() ?: ""
        } catch (e: Exception) {

        }
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "Супы",
                type = "ct00002",
                image = image,
            )
        ).await()

        id = "ca00005"
        try {
            image = storage.reference.child("category/$id.png").downloadUrl.await().toString() ?: ""
        } catch (e: Exception) {

        }
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "Салаты",
                type = "ct00002",
                image = image,
            )
        ).await()

        id = "ca00006"
        try {
            image = storage.reference.child("category/$id.png").downloadUrl.await().toString() ?: ""
        } catch (e: Exception) {

        }
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "Десерты",
                type = "ct00002",
                image = image,
            )
        ).await()

        id = "ca00007"
        try {
            image = storage.reference.child("category/$id.png").downloadUrl.await().toString() ?: ""
        } catch (e: Exception) {

        }
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "Горячее",
                type = "ct00002",
                image = image,
            )
        ).await()

        id = "ca00008"
        try {
            image = storage.reference.child("category/$id.png").downloadUrl.await().toString() ?: ""
        } catch (e: Exception) {

        }
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "Завтраки",
                type = "ct00002",
                image = image,
            )
        ).await()

        id = "ca00009"
        try {
            image = storage.reference.child("category/$id.png").downloadUrl.await().toString() ?: ""
        } catch (e: Exception) {

        }
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "Выпечка",
                type = "ct00002",
                image = image,
            )
        ).await()

        id = "ca00010"
        try {
            image = storage.reference.child("category/$id.png").downloadUrl.await().toString() ?: ""
        } catch (e: Exception) {

        }
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "Запеканки",
                type = "ct00002",
                image = image,
            )
        ).await()

        id = "ca00011"
        try {
            image = storage.reference.child("category/$id.png").downloadUrl.await().toString() ?: ""
        } catch (e: Exception) {

        }
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "Заготовки",
                type = "ct00002",
                image = image,
            )
        ).await()

        id = "ca00012"
        try {
            image = storage.reference.child("category/$id.png").downloadUrl.await().toString() ?: ""
        } catch (e: Exception) {

        }
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "Без мяса",
                type = "ct00003",
                image = image,
            )
        ).await()

        id = "ca00013"
        try {
            image = storage.reference.child("category/$id.png").downloadUrl.await().toString() ?: ""
        } catch (e: Exception) {

        }
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "Без сахара",
                type = "ct00003",
                image = image,
            )
        ).await()

        id = "ca00014"
        try {
            image = storage.reference.child("category/$id.png").downloadUrl.await().toString() ?: ""
        } catch (e: Exception) {

        }
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "Без лактозы",
                type = "ct00003",
                image = image,
            )
        ).await()

        id = "ca00015"
        try {
            image = storage.reference.child("category/$id.png").downloadUrl.await().toString() ?: ""
        } catch (e: Exception) {

        }
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "Без глютена",
                type = "ct00003",
                image = image,
            )
        ).await()

        id = "ca00016"
        try {
            image = storage.reference.child("category/$id.png").downloadUrl.await().toString() ?: ""
        } catch (e: Exception) {

        }
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "Без лука",
                type = "ct00003",
                image = image,
            )
        ).await()

        id = "ca00017"
        try {
            image = storage.reference.child("category/$id.png").downloadUrl.await().toString() ?: ""
        } catch (e: Exception) {

        }
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "Без свинины",
                type = "ct00003",
                image = image,
            )
        ).await()

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
            //getTabsDestination()
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
        NavController.OnDestinationChangedListener { _, destination, arguments ->
            supportActionBar?.title = prepareTitle(destination.label, arguments)
            supportActionBar?.setDisplayHomeAsUpEnabled(!isStartDestination(destination))
        }

    private fun isStartDestination(destination: NavDestination?): Boolean {
        if (destination == null) return false
        val graph = destination.parent ?: return false
        val startDestinations = topLevelDestinations + graph.startDestinationId
        return startDestinations.contains(destination.id)
    }

    private fun prepareTitle(label: CharSequence?, arguments: Bundle?): String {

        // code for this method has been copied from Google sources :)

        if (label == null) return ""
        val title = StringBuffer()
        val fillInPattern = Pattern.compile("\\{(.+?)\\}")
        val matcher = fillInPattern.matcher(label)
        while (matcher.find()) {
            val argName = matcher.group(1)
            if (arguments != null && arguments.containsKey(argName)) {
                matcher.appendReplacement(title, "")
                title.append(arguments[argName].toString())
            } else {
                throw IllegalArgumentException(
                    "Could not find $argName in $arguments to fill label $label"
                )
            }
        }
        matcher.appendTail(title)
        return title.toString()
    }

    fun isSignedIn(): Boolean = viewModel.isUserAuthenticated

    private fun getMainNavigationGraphId(): Int = R.navigation.graph_main

    private fun getTabsDestination(): Int = R.id.tabsFragment

    private fun getAuthDestination(): Int = R.id.authFragment

}