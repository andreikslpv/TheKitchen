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

    fun getCategoriesByType(type: String) = liveData(Dispatchers.IO) {
        repository.getCategoriesByType(type).collect { response ->
            emit(response)
        }
    }
}