package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.domain.RecipeRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class HomeViewModel : ViewModel()  {

    @Inject
    lateinit var repository: RecipeRepository

    init {
        App.instance.dagger.inject(this)
    }

    fun getCategoriesDish() = liveData(Dispatchers.IO) {
        repository.getCategoriesByType("ct00002").collect { response ->
            emit(response)
        }
    }
    fun getCategoriesTime() = liveData(Dispatchers.IO) {
        repository.getCategoriesByType("ct00001").collect { response ->
            emit(response)
        }
    }
}