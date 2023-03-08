package com.andreikslpv.thekitchen.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.di.AuthIntent
import com.andreikslpv.thekitchen.di.MainIntent
import com.andreikslpv.thekitchen.presentation.vm.SplashViewModel
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {
    @AuthIntent
    @Inject
    lateinit var authIntent: Intent

    @MainIntent
    @Inject
    lateinit var mainIntent: Intent

    private val viewModel by viewModels<SplashViewModel>()

    init {
        App.instance.dagger.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkIfUserIsAuthenticated()
    }

    private fun checkIfUserIsAuthenticated() {
        if (viewModel.isUserAuthenticated) {
            goToMainActivity()
        } else {
            goToAuthInActivity()
        }
    }

    private fun goToMainActivity() {
        startActivity(mainIntent)
        finish()
    }

    private fun goToAuthInActivity() {
        startActivity(authIntent)
        finish()
    }
}