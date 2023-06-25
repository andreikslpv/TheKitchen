package com.andreikslpv.thekitchen.domain.usecases

import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.IngredientRepository
import com.andreikslpv.thekitchen.domain.UserRepository
import com.andreikslpv.thekitchen.domain.models.ShoppingItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TryToAddToShoppingListUseCase(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val ingredientRepository: IngredientRepository,
) {

    fun execute(shoppingItem: ShoppingItem): Boolean {
        val user = authRepository.getCurrentUser()
        return if (user != null) {
            // получаем теущий список
            val currentShoppingList: MutableList<ShoppingItem> = mutableListOf()
            currentShoppingList.addAll(userRepository.getShoppingList().value)
            CoroutineScope(Dispatchers.IO).launch {
                // ... и проверяем его на наличие shoppingItem с тем же productId и  unitId
                currentShoppingList.forEach { item ->
                    // если item есть в списке покупок
                    if (item.ingredient.product == shoppingItem.ingredient.product
                        && item.showingName == shoppingItem.showingName
                        && item.ingredient.unit == shoppingItem.ingredient.unit
                    ) {
                        // ... то складываем их кол-во в новом итеме,
                        shoppingItem.ingredient.count += item.ingredient.count
                        // ... удаляем старый
                        userRepository.removeFromShoppingList(user.uid, item)
                        return@forEach
                    }
                }
                // если showingName пустое, пробуем получить из бд имя продукта
                if (shoppingItem.showingName.isBlank() && shoppingItem.ingredient.product.isNotBlank())
                    shoppingItem.showingName =
                        ingredientRepository.getProductById(shoppingItem.ingredient.product).name
                userRepository.addToShoppingList(user.uid, shoppingItem)
            }
            true
        } else {
            false
        }
    }

}