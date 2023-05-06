package com.andreikslpv.thekitchen.domain.usecases

import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.UserRepository

class TryToRemoveFromFavoritesUseCase(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) {

    fun execute(recipeId: String): Boolean {
        val user = authRepository.getCurrentUser()
        return if (user != null) {
            userRepository.removeFromFavorites(user.uid, recipeId)
            true
        } else {
            false
        }
    }
}