package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.domain.IngredientRepository
import com.andreikslpv.thekitchen.domain.models.ShoppingItem
import com.andreikslpv.thekitchen.domain.models.Unit
import com.andreikslpv.thekitchen.domain.usecases.ClearFiltersDishAndTimeUseCase
import com.andreikslpv.thekitchen.domain.usecases.GetShoppingListUseCase
import com.andreikslpv.thekitchen.domain.usecases.TryToAddToShoppingListUseCase
import com.andreikslpv.thekitchen.domain.usecases.TryToEditShoppingItemUseCase
import com.andreikslpv.thekitchen.domain.usecases.TryToRemoveAllFromShoppingList
import com.andreikslpv.thekitchen.domain.usecases.TryToRemoveFromShoppingList
import com.andreikslpv.thekitchen.presentation.utils.ingredientCountToString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShoppingListViewModel : ViewModel() {

    @Inject
    lateinit var getShoppingListUseCase: GetShoppingListUseCase

    @Inject
    lateinit var ingredientRepository: IngredientRepository

    @Inject
    lateinit var tryToRemoveFromShoppingList: TryToRemoveFromShoppingList

    @Inject
    lateinit var tryToRemoveAllFromShoppingList: TryToRemoveAllFromShoppingList

    @Inject
    lateinit var tryToAddToShoppingListUseCase: TryToAddToShoppingListUseCase

    @Inject
    lateinit var tryToEditShoppingItemUseCase: TryToEditShoppingItemUseCase

    @Inject
    lateinit var clearFiltersDishAndTimeUseCase: ClearFiltersDishAndTimeUseCase

    val shoppingList = liveData(Dispatchers.IO) {
        getShoppingListUseCase.execute().collect { response ->
            emit(response)
        }
    }

    private var unitList = MutableLiveData<List<Unit>>()


    val selectedShoppingItem = MutableStateFlow(mutableListOf<ShoppingItem>())

    init {
        App.instance.dagger.inject(this)
        // удаляем фильтры категорий времени и типа блюда, чтобы при переходе на экран Поиск их не было
        clearFiltersDishAndTimeUseCase.execute()

       CoroutineScope(Dispatchers.IO).launch {
            ingredientRepository.getAllUnits().collect { response ->
                unitList.postValue(response)
            }
        }
    }

    fun changeSelectedStatus(shoppingItem: ShoppingItem) {
        if (selectedShoppingItem.value.contains(shoppingItem))
            selectedShoppingItem.value.remove(shoppingItem)
        else
            selectedShoppingItem.value.add(shoppingItem)
    }

    fun selectAll() {
        shoppingList.value?.let {
            refreshShoppingList(it as MutableList<ShoppingItem>)
        }
    }

    fun unSelectAll() {
        refreshShoppingList(mutableListOf())
    }

    private fun refreshShoppingList(newList: MutableList<ShoppingItem>) {
        CoroutineScope(Dispatchers.Main).launch {
            selectedShoppingItem.emit(newList)
        }
    }

    fun removeSelectedFromShoppingList(): Boolean {
        return tryToRemoveFromShoppingList.execute(selectedShoppingItem.value)
    }

    fun removeFromShoppingList(shoppingItems: List<ShoppingItem>): Boolean {
        return tryToRemoveFromShoppingList.execute(shoppingItems)
    }

    fun removeAllFromShoppingList(): Boolean {
        return tryToRemoveAllFromShoppingList.execute()
    }

    fun addToShoppingList(shoppingItem: ShoppingItem): Boolean {
        return tryToAddToShoppingListUseCase.execute(shoppingItem)
    }

    fun editShoppingItem(shoppingItem: ShoppingItem): Boolean {
        return tryToEditShoppingItemUseCase.execute(shoppingItem)
    }

    fun getUnitsNameListEdit() = unitList.value?.map { it.name } ?: emptyList()

    fun getUnitsNameListAdd() =
        unitList.value?.filter { it.showWhenAdding }?.map { it.name } ?: emptyList()

    fun getUnitsNameById(id: String) = unitList.value?.find {
        it.id == id
    }?.name ?: ""

    fun getUnitsIdByName(name: String) = unitList.value?.find {
        it.name == name
    }?.id ?: ""

    suspend fun generateTextFromShoppingList(): String {
        val job = CoroutineScope(Dispatchers.IO).async {
            var products = ""
            var i = 1
            shoppingList.value?.forEach {
                val unit = ingredientRepository.getUnitById(it.ingredient.unit).name
                products += "$i. ${it.showingName} (${it.ingredient.count.ingredientCountToString()} $unit);\n"
                i++
            }
            products
        }
        return job.await()
    }

}