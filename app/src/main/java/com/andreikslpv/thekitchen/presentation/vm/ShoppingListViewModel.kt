package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.domain.UserRepository
import com.andreikslpv.thekitchen.domain.models.ShoppingItem
import com.andreikslpv.thekitchen.domain.usecases.TryToRemoveFromShoppingList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShoppingListViewModel : ViewModel() {

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var tryToRemoveFromShoppingList: TryToRemoveFromShoppingList

    val shoppingList = liveData(Dispatchers.IO) {
        userRepository.getShoppingList().collect { response ->
            emit(response)
        }
    }

    val selectedShoppingItem = MutableStateFlow(mutableListOf<ShoppingItem>())

    init {
        App.instance.dagger.inject(this)
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

}