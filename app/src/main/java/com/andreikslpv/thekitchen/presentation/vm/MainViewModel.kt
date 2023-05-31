package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.usecases.GetUserFromDbUseCase
import javax.inject.Inject

class MainViewModel : ViewModel() {

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var getUserFromDbUseCase: GetUserFromDbUseCase

    val isUserAuthenticated get() = authRepository.isUserAuthenticatedInFirebase

    val user = liveData {
        getUserFromDbUseCase.execute().collect { response ->
            emit(response)
        }
    }

    init {
        App.instance.dagger.inject(this)
    }


}