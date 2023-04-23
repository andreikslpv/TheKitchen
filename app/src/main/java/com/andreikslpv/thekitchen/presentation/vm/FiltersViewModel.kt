package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.domain.RecipeRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class FiltersViewModel: ViewModel()  {

    @Inject
    lateinit var repository: RecipeRepository

    init {
        App.instance.dagger.inject(this)
    }

    fun getAllCategories() = liveData(Dispatchers.IO) {
        repository.getAllCategories().collect { response ->
            emit(response)
        }
    }
}