package com.andreikslpv.thekitchen.domain.usecases

import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.UserRepository

class TryToRemoveAllFromFavoritesUseCase(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) {

    fun execute(): Boolean {
        val user = authRepository.getCurrentUser()
        return if (user != null) {
            userRepository.removeAllFromFavorites(user.uid)
            true
        } else {
            false
        }
    }
}