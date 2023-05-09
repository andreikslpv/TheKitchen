package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.domain.models.Filters
import com.andreikslpv.thekitchen.domain.usecases.GetUserFromDbUseCase
import com.andreikslpv.thekitchen.domain.usecases.TryToChangeFavoritesStatusUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecipeViewModel : ViewModel() {

    private val maxPortionsCount = 9
    private val minPortionsCount = 1

    @Inject
    lateinit var getUserFromDbUseCase: GetUserFromDbUseCase

    @Inject
    lateinit var tryToChangeFavoritesStatusUseCase: TryToChangeFavoritesStatusUseCase

    private val _portions = MutableLiveData(0)
    val portions: LiveData<Int> = _portions

    init {
        App.instance.dagger.inject(this)

        // начинаем отслеживать данные пользователя в бд
        CoroutineScope(Dispatchers.IO).launch {
            getUserFromDbUseCase.execute().collect {}
        }
    }

    fun validAndSetPortionsCount(newCount: Int) {
        if (newCount in minPortionsCount..maxPortionsCount)
            _portions.value = newCount
    }

    fun upPortionsCount() {
        val temp = _portions.value?.plus(1) ?: 0
        validAndSetPortionsCount(temp)
    }

    fun downPortionsCount() {
        val temp = _portions.value?.minus(1) ?: 0
        validAndSetPortionsCount(temp)
    }

    fun tryToChangeFavoritesStatus(recipeId: String): Boolean {
        return tryToChangeFavoritesStatusUseCase.execute(recipeId)
    }


}