package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.CategoryRepository
import com.andreikslpv.thekitchen.domain.UserRepository
import com.andreikslpv.thekitchen.domain.models.CategoryType
import com.andreikslpv.thekitchen.domain.usecases.GetRecipeHistoryUseCase
import com.andreikslpv.thekitchen.domain.usecases.TryToChangeFavoritesStatusUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class ProfileViewModel : ViewModel() {

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var categoryRepository: CategoryRepository

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var getRecipeHistoryUseCase: GetRecipeHistoryUseCase

    @Inject
    lateinit var tryToChangeFavoritesStatusUseCase: TryToChangeFavoritesStatusUseCase

    private var uid = ""

    init {
        App.instance.dagger.inject(this)
    }

    fun getRecipeHistory() = liveData(Dispatchers.IO) {
        getRecipeHistoryUseCase.execute().collect { response ->
            emit(response)
        }
    }

    fun getCategoryExclude() = liveData {
        categoryRepository.getAllCategories().collect { response ->
            emit(response.filter { it.type == CategoryType.EXCLUDE.value })
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

    fun deleteUserAuth(idToken: String?) = liveData(Dispatchers.IO) {
        authRepository.firebaseDeleteUser(idToken).collect { response ->
            emit(response)
        }
    }

    fun deleteUserDb() = liveData(Dispatchers.IO) {
        userRepository.deleteUser(uid).collect { response ->
            emit(response)
        }
    }

    fun saveUidBeforeDeleteUser() {
        uid = authRepository.getCurrentUser()?.uid ?: ""
    }

    fun editUserName(newName: String) = liveData(Dispatchers.IO) {
        authRepository.editUserName(newName).collect { response ->
            emit(response)
        }
    }

}