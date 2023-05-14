package com.andreikslpv.thekitchen.domain.usecases

import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.UserRepository
import com.andreikslpv.thekitchen.domain.models.Ingredient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TryToAddIngredientToShoppingListUseCase(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) {

    fun execute(newShoppingList: List<Ingredient>): Boolean {
        val user = authRepository.getCurrentUser()
        return if (user != null) {
            CoroutineScope(Dispatchers.IO).launch {
                userRepository.setShoppingList(user.uid, newShoppingList)
            }
            true
        } else {
            false
        }
    }
}