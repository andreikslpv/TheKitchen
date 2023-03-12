package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.ViewModel
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.data.repository.AuthRepository
import javax.inject.Inject

class SplashViewModel : ViewModel() {
    @Inject
    lateinit var repository: AuthRepository

    val isUserAuthenticated get() = repository.isUserAuthenticatedInFirebase

    init {
        App.instance.dagger.inject(this)
    }

}
