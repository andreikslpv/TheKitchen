package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.usecases.StartObserveUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class MainViewModel : ViewModel() {

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var startObserveUserUseCase: StartObserveUserUseCase

    val isUserAuthenticated get() = authRepository.isUserAuthenticatedInFirebase

    init {
        App.instance.dagger.inject(this)
    }

    @ExperimentalCoroutinesApi
    fun getAuthState() = liveData(Dispatchers.IO) {
        authRepository.getFirebaseAuthState().collect { response ->
            emit(response)
        }
    }

    fun startObserveUser() {
        startObserveUserUseCase.execute()
    }


}