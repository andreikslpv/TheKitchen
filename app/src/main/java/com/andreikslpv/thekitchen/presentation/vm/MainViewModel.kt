package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.ViewModel
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.data.repository.AuthRepository
import javax.inject.Inject

class MainViewModel : ViewModel() {

    @Inject
    lateinit var authRepository: AuthRepository

    val isUserAuthenticated get() = authRepository.isUserAuthenticatedInFirebase

    init {
        App.instance.dagger.inject(this)
    }


}