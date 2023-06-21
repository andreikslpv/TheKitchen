package com.andreikslpv.thekitchen.domain.usecases

import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.UserRepository
import com.andreikslpv.thekitchen.domain.models.ShoppingItem
import kotlinx.coroutines.flow.MutableStateFlow

class GetShoppingListUseCase(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) {

    fun execute(): MutableStateFlow<List<ShoppingItem>> {
        val user = authRepository.getCurrentUser()
        return if (user != null) userRepository.getShoppingList()
        else MutableStateFlow(emptyList())
    }
}