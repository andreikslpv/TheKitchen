package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.data.repository.SplashRepository
import javax.inject.Inject

class SplashViewModel : ViewModel() {
    @Inject
    lateinit var repository: SplashRepository

    val isUserAuthenticated get() = repository.isUserAuthenticatedInFirebase

    init {
        App.instance.dagger.inject(this)
    }

}
