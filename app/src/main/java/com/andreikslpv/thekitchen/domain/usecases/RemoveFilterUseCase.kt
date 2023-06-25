package com.andreikslpv.thekitchen.domain.usecases

import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.UserRepository

class RemoveFilterUseCase(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) {

    fun execute(categoryId: String) {
        val user = authRepository.getCurrentUser()
        if (user != null) {
            userRepository.getDefaultExclude().value.let { exclude ->
                if (exclude.contains(categoryId)) {
                    userRepository.removeFromDefaultExclude(user.uid, categoryId)
                }
            }
        }
    }
}