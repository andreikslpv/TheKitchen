package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.domain.RecipeRepository
import com.andreikslpv.thekitchen.domain.models.Filters
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class FiltersViewModel : ViewModel() {

    @Inject
    lateinit var repository: RecipeRepository

    private val _filters = MutableLiveData(Filters())
    val filters: LiveData<Filters> = _filters

    init {
        App.instance.dagger.inject(this)
    }

    fun getAllCategories() = liveData(Dispatchers.IO) {
        repository.getAllCategories().collect { response ->
            emit(response)
        }
    }

    fun addFilters(filtersArray: Array<String>?) {
        filtersArray?.let {
            _filters.value?.addCategories(it)
        }
    }

    fun addFilter(category: String) {
        _filters.value?.addCategory(category)
    }

    fun removeFilter(category: String) {
        _filters.value?.removeCategory(category)
    }

    fun refreshFilters() {
        _filters.postValue(_filters.value)
    }
}