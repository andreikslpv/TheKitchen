package com.andreikslpv.thekitchen.domain.usecases

import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.UserRepository
import com.andreikslpv.thekitchen.domain.models.Ingredient
import com.andreikslpv.thekitchen.domain.models.ShoppingItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TryToAddIngredientToShoppingListUseCase(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) {

    fun execute(newShoppingList: List<Ingredient>): Boolean {
        val user = authRepository.getCurrentUser()
        return if (user != null) {
            // получаем теущий список
            val job = CoroutineScope(Dispatchers.IO).async {
                userRepository.getShoppingList()
            }
            // дожидаемся получения текущего списка и проверяем для каждого пришедшего ингредиента
            // ... есть ли он в текущем списке покупок
            CoroutineScope(Dispatchers.IO).launch {
                val currentShoppingList: MutableList<ShoppingItem> = mutableListOf()
                currentShoppingList.addAll(job.await().value)
                newShoppingList.forEach { ingredient ->
                    var isInclude = false
                    currentShoppingList.forEach { item ->
                        // если ингредиент есть в списке покупок, то увеличиваем его кол-во
                        if (item.ingredient.product == ingredient.product && item.ingredient.unit == ingredient.unit) {
                            item.ingredient.count += ingredient.count
                            isInclude = true
                        }
                    }
                    if (!isInclude)
                        // ... иначе жоюавляем ингредиентв список
                        currentShoppingList.add(ShoppingItem(ingredient = ingredient))
                }
                userRepository.setShoppingList(user.uid, currentShoppingList)
            }
            true
        } else {
            false
        }
    }

}