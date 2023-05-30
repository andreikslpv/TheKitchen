package com.andreikslpv.thekitchen.domain.usecases

import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.UserRepository

class TryToRemoveAllFromShoppingList(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) {

    fun execute(): Boolean {
        val user = authRepository.getCurrentUser()
        return if (user != null) {
            userRepository.removeAllFromShoppingList(user.uid)
            true
        } else {
            false
        }
    }

}