package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.domain.UserRepository
import com.andreikslpv.thekitchen.domain.usecases.GetUserFromDbUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShoppingListViewModel : ViewModel() {

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var getUserFromDbUseCase: GetUserFromDbUseCase

    init {
        App.instance.dagger.inject(this)

        // начинаем отслеживать данные пользователя в бд
        CoroutineScope(Dispatchers.IO).launch {
            getUserFromDbUseCase.execute().collect {}
        }
    }

    fun getShoppingList() = liveData(Dispatchers.IO) {
        userRepository.getShoppingList().collect { response ->
            emit(response)
        }
    }

}