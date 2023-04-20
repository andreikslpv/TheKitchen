package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.domain.RecipeRepository
import com.andreikslpv.thekitchen.domain.models.CategoryType
import com.andreikslpv.thekitchen.domain.usecases.GetRecipeNewUseCase
import com.andreikslpv.thekitchen.domain.usecases.InitApplicationSettingsUseCase
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class HomeViewModel : ViewModel()  {

    @Inject
    lateinit var repository: RecipeRepository

    @Inject
    lateinit var getRecipeNewUseCase: GetRecipeNewUseCase

    init {
        App.instance.dagger.inject(this)
    }

    fun getCategoriesDish() = liveData(Dispatchers.IO) {
        repository.getCategoriesByType(CategoryType.DISH.value).collect { response ->
            emit(response)
        }
    }

    fun getCategoriesTime() = liveData(Dispatchers.IO) {
        repository.getCategoriesByType(CategoryType.TIME.value).collect { response ->
            emit(response)
        }
    }

    fun getRecipeNew() = liveData(Dispatchers.IO) {
        getRecipeNewUseCase.execute().collect { response ->
            emit(response)
        }
    }
}