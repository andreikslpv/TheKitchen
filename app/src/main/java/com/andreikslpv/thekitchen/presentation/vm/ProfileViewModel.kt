package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.usecases.GetRecipeHistoryUseCase
import com.andreikslpv.thekitchen.domain.usecases.GetUserFromDbUseCase
import com.andreikslpv.thekitchen.domain.usecases.TryToChangeFavoritesStatusUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel : ViewModel() {

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var getRecipeHistoryUseCase: GetRecipeHistoryUseCase

    @Inject
    lateinit var getUserFromDbUseCase: GetUserFromDbUseCase

    @Inject
    lateinit var tryToChangeFavoritesStatusUseCase: TryToChangeFavoritesStatusUseCase

    init {
        App.instance.dagger.inject(this)

        // начинаем отслеживать данные пользователя в бд
        CoroutineScope(Dispatchers.IO).launch {
            getUserFromDbUseCase.execute().collect {}
        }
    }

    fun getRecipeHistory() = liveData(Dispatchers.IO) {
        getRecipeHistoryUseCase.execute().collect { response ->
            emit(response)
        }
    }

    fun tryToChangeFavoritesStatus(recipeId: String): Boolean {
        return tryToChangeFavoritesStatusUseCase.execute(recipeId)
    }

    fun signOut() = liveData(Dispatchers.IO) {
        authRepository.signOut().collect { response ->
            emit(response)
        }
    }

    @ExperimentalCoroutinesApi
    fun getAuthState() = liveData(Dispatchers.IO) {
        authRepository.getFirebaseAuthState().collect { response ->
            emit(response)
        }
    }

    fun getCurrentUser() = liveData(Dispatchers.IO) {
        emit(authRepository.getCurrentUser())
    }

}