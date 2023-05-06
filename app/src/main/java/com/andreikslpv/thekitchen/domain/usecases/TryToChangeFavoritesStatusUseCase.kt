package com.andreikslpv.thekitchen.domain.usecases

import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.UserRepository

class TryToChangeFavoritesStatusUseCase(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) {

    fun execute(recipeId: String): Boolean {
        val user = authRepository.getCurrentUser()
        return if (user != null) {
            userRepository.getFavorites().value.let { favorites ->
                if (favorites.contains(recipeId))
                    userRepository.removeFromFavorites(user.uid, recipeId)
                else
                    userRepository.addToFavorites(user.uid, recipeId)
            }
            true
        } else {
            false
        }
    }
}