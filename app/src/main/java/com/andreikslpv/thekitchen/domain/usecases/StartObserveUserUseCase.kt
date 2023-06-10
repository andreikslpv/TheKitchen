package com.andreikslpv.thekitchen.domain.usecases

import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.UserRepository

class StartObserveUserUseCase(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) {

    fun execute() {
        authRepository.getCurrentUser()?.let { user ->
            userRepository.startObserveUser(user.uid)
        }
    }
}