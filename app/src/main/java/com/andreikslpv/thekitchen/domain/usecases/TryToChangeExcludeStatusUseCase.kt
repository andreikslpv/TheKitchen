package com.andreikslpv.thekitchen.domain.usecases

import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.UserRepository

class TryToChangeExcludeStatusUseCase(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) {

    fun execute(categoryId: String): Boolean {
        val user = authRepository.getCurrentUser()
        return if (user != null) {
            userRepository.getDefaultExclude().value.let { exclude ->
                if (exclude.contains(categoryId))
                    userRepository.removeFromDefaultExclude(user.uid, categoryId)
                else
                    userRepository.addToDefaultExclude(user.uid, categoryId)
            }
            true
        } else {
            false
        }
    }
}