package com.andreikslpv.thekitchen.domain.usecases

import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.UserRepository
import com.andreikslpv.thekitchen.domain.models.ShoppingItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TryToRemoveFromShoppingList(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) {

    fun execute(shoppingItems: List<ShoppingItem>): Boolean {
        val user = authRepository.getCurrentUser()
        return if (user != null) {
            // получаем теущий список
            val currentShoppingList: MutableList<ShoppingItem> = mutableListOf()
            currentShoppingList.addAll(userRepository.getShoppingList().value)
            CoroutineScope(Dispatchers.IO).launch {
                // дожидаемся получения текущего списка и проверяем его на то, что надо удалить всё
                // ... иначе удаляем по одному
//                if (shoppingItems.containsAll(currentShoppingList))
//                    userRepository.removeAllFromShoppingList(user.uid)
//                else
                    shoppingItems.forEach { item ->
                        userRepository.removeFromShoppingList(user.uid, item)
                    }
            }
            true
        } else {
            false
        }
    }

}