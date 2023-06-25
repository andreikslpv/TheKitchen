package com.andreikslpv.thekitchen.domain.usecases

import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow

class GetFavoritesUseCase(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) {

    fun execute(): MutableStateFlow<List<String>> {
        val user = authRepository.getCurrentUser()
        return if (user != null) userRepository.getFavorites()
        else MutableStateFlow(emptyList())
    }
}