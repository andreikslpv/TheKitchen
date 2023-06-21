package com.andreikslpv.thekitchen.domain.usecases

import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow

class GetDefaultExcludeUseCase(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) {

    fun execute(): MutableStateFlow<List<String>> {
        val user = authRepository.getCurrentUser()
        return if (user != null) userRepository.getDefaultExclude()
        else MutableStateFlow(emptyList())
    }
}