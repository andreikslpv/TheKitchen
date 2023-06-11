package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.CategoryRepository
import com.andreikslpv.thekitchen.domain.models.CategoryType
import com.andreikslpv.thekitchen.domain.usecases.ClearFiltersDishAndTimeUseCase
import com.andreikslpv.thekitchen.domain.usecases.GetRecipeNewUseCase
import com.andreikslpv.thekitchen.domain.usecases.TryToChangeFavoritesStatusUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel : ViewModel() {

    @Inject
    lateinit var categoryRepository: CategoryRepository

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var getRecipeNewUseCase: GetRecipeNewUseCase

    @Inject
    lateinit var tryToChangeFavoritesStatusUseCase: TryToChangeFavoritesStatusUseCase

    @Inject
    lateinit var clearFiltersDishAndTimeUseCase: ClearFiltersDishAndTimeUseCase

    val currentUserFromAuth by lazy {
        authRepository.getCurrentUser()
    }

    init {
        App.instance.dagger.inject(this)
        // удаляем фильтры категорий времени и типа блюда, чтобы при переходе на экран Поиск их не было
        clearFiltersDishAndTimeUseCase.execute()
    }

    fun getAllCategories() = liveData(Dispatchers.IO) {
        categoryRepository.getAllCategories().collect { response ->
            emit(response)
        }
    }

    fun setCategoryDish(categoryId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            categoryRepository.setFilterCategory(categoryId, CategoryType.DISH)
        }

    }

    fun setCategoryTime(categoryId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            categoryRepository.setFilterCategory(categoryId, CategoryType.TIME)
        }

    }

    fun getRecipeNew() = liveData(Dispatchers.IO) {
        getRecipeNewUseCase.execute().collect { response ->
            emit(response)
        }
    }

    fun tryToChangeFavoritesStatus(recipeId: String): Boolean {
        return tryToChangeFavoritesStatusUseCase.execute(recipeId)
    }

}