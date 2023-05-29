package com.andreikslpv.thekitchen.domain.usecases

import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.IngredientRepository
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
    private val ingredientRepository: IngredientRepository,
) {

    fun execute(newShoppingList: List<Ingredient>): Boolean {
        val user = authRepository.getCurrentUser()
        return if (user != null) {
            // получаем теущий список
            val currentShoppingList: MutableList<ShoppingItem> = mutableListOf()
            currentShoppingList.addAll(userRepository.getShoppingList().value)
            val job = CoroutineScope(Dispatchers.IO).async {
                newShoppingList.forEach { ingredient ->
                    var isInclude = false
                    currentShoppingList.forEach { item ->
                        // если ингредиент есть в списке покупок, то увеличиваем его кол-во
                        if (item.ingredient.product == ingredient.product && item.ingredient.unit == ingredient.unit) {
                            item.ingredient.count += ingredient.count
                            isInclude = true
                        }
                    }
                    // ... иначе добавляем ингредиент в список
                    if (!isInclude)
                        currentShoppingList.add(
                            ShoppingItem(
                                showingName = ingredientRepository.getProductById(ingredient.product).name,
                                ingredient = ingredient
                            )
                        )
                }
                currentShoppingList
            }
            CoroutineScope(Dispatchers.IO).launch {
                userRepository.setShoppingList(user.uid, job.await())
            }
            true
        } else {
            false
        }
    }

}