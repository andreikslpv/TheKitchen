package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.domain.UserRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ShoppingListViewModel : ViewModel() {

    @Inject
    lateinit var userRepository: UserRepository

    init {
        App.instance.dagger.inject(this)
    }

    fun getShoppingList() = liveData(Dispatchers.IO) {
        userRepository.getShoppingList().collect { response ->
            emit(response)
        }
    }

}