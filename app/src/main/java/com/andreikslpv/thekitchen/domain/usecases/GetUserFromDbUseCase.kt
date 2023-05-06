package com.andreikslpv.thekitchen.domain.usecases

import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.UserRepository
import kotlinx.coroutines.flow.flow

class GetUserFromDbUseCase(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) {

    suspend fun execute() = flow {
        authRepository.getCurrentUser()?.let { user ->
            userRepository.getCurrentUser(user.uid).collect { userFromDb ->
                emit(userFromDb)
            }
        }
    }
}