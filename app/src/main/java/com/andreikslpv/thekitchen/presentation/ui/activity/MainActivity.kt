package com.andreikslpv.thekitchen.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.ActivityMainBinding
import com.andreikslpv.thekitchen.di.AuthIntent
import com.andreikslpv.thekitchen.domain.models.Response
import com.andreikslpv.thekitchen.presentation.vm.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @AuthIntent
    @Inject
    lateinit var authIntent: Intent

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel by viewModels<MainViewModel>()

    init {
        App.instance.dagger.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.text.text = "Пользователь ${viewModel.getCurrentUser()?.displayName} поздравляет прекрасную половину команды Pixels с праздником 8 марта!!!"
        //setNavController()
        getAuthState()
    }

//    private fun setNavController() {
//        val navHostFragment = supportFragmentManager
//            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        navController = navHostFragment.navController
//        setupActionBarWithNavController(navController)
//    }

//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }

    private fun signOut() {
        viewModel.signOut().observe(this) { response ->
            when (response) {
                is Response.Loading -> binding.progressBar.show()
                is Response.Success -> binding.progressBar.hide()
                is Response.Failure -> {
                    print(response.errorMessage)
                    binding.progressBar.hide()
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun getAuthState() {
        viewModel.getAuthState().observe(this) { isUserSignedOut ->
            if (isUserSignedOut) {
                goToAuthInActivity()
            }
        }
    }

    private fun goToAuthInActivity() {
        startActivity(authIntent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sign_out_item -> signOut()
        }
        return super.onOptionsItemSelected(item)
    }
}