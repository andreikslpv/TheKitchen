package com.andreikslpv.thekitchen.domain.usecases

import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.UserRepository

class TryToSetExcludeUseCase(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) {

    fun execute(newExclude: List<String>): Boolean {
        val user = authRepository.getCurrentUser()
        return if (user != null) {
            userRepository.setDefaultExclude(user.uid, newExclude)
            true
        } else {
            false
        }
    }
}