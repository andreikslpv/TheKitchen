package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.RecipeRepository
import com.andreikslpv.thekitchen.domain.UserRepository
import com.andreikslpv.thekitchen.domain.usecases.GetRecipeNewUseCase
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class HomeViewModel : ViewModel() {

    @Inject
    lateinit var recipeRepository: RecipeRepository

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var getRecipeNewUseCase: GetRecipeNewUseCase

    val currentUserFromAuth by lazy {
        authRepository.getCurrentUser()
    }

    val currentUserFromDb = liveData(Dispatchers.IO) {
        authRepository.getCurrentUser()?.let {
            userRepository.getCurrentUser(it.uid).collect { response ->
                emit(response)
            }
        }
    }

    init {
        App.instance.dagger.inject(this)
    }

    fun getAllCategories() = liveData(Dispatchers.IO) {
        recipeRepository.getAllCategories().collect { response ->
            emit(response)
        }
    }

    fun getRecipeNew() = liveData(Dispatchers.IO) {
        getRecipeNewUseCase.execute().collect { response ->
            emit(response)
        }
    }

    fun changeFavoritesStatus(recipeId: String) = authRepository.getCurrentUser()
        ?.let { user ->
            currentUserFromDb.value?.favorites?.let { favorites ->
                if (favorites.contains(recipeId))
                    userRepository.removeFromFavorites(user.uid, recipeId)
                else
                    userRepository.addToFavorites(user.uid, recipeId)
            }
        }



}