package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.domain.CategoryRepository
import com.andreikslpv.thekitchen.domain.models.CategoryType
import com.andreikslpv.thekitchen.domain.models.Filters
import com.andreikslpv.thekitchen.domain.usecases.TryToSetExcludeUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class FiltersViewModel : ViewModel() {

    @Inject
    lateinit var categoryRepository: CategoryRepository

    @Inject
    lateinit var tryToSetExcludeUseCase: TryToSetExcludeUseCase

    private val _filters = MutableLiveData(Filters())
    val filters: LiveData<Filters> = _filters

    init {
        App.instance.dagger.inject(this)
    }

    fun getAllCategories() = liveData(Dispatchers.IO) {
        categoryRepository.getAllCategories().collect { response ->
            emit(response)
        }
    }

    fun getFiltersFromRepository() {
        _filters.value?.addCategories(categoryRepository.getFilters().value.getCategoriesList())
    }

    fun sendFiltersToRepository() {
        CoroutineScope(Dispatchers.IO).launch {
            _filters.value?.let {
                categoryRepository.setFilters(it)
                val exclude = categoryRepository.getCategoriesIdByType(CategoryType.EXCLUDE.value)
                val temp = exclude.intersect(it.getCategoriesList().toSet())
                tryToSetExcludeUseCase.execute(temp.toList())
            }
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