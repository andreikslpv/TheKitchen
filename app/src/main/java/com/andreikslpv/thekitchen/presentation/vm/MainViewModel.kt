package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.ViewModel
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.usecases.GetUserFromDbUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel : ViewModel() {

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var getUserFromDbUseCase: GetUserFromDbUseCase

    val isUserAuthenticated get() = authRepository.isUserAuthenticatedInFirebase

    init {
        App.instance.dagger.inject(this)

        // начинаем отслеживать данные пользователя в бд
        CoroutineScope(Dispatchers.IO).launch {
            getUserFromDbUseCase.execute().collect {}
        }
    }


}