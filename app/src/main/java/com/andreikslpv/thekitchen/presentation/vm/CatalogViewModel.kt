package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.domain.RecipeRepository
import com.andreikslpv.thekitchen.domain.models.Filters
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogViewModel: ViewModel()  {

    @Inject
    lateinit var repository: RecipeRepository

    val recipes: Flow<PagingData<RecipePreview>>

    private val filters = MutableLiveData(Filters())

    init {
        App.instance.dagger.inject(this)

        recipes = filters
            .asFlow()
            .flatMapLatest {
                repository.getRecipePreview(it)
            }
            // кешируем прлучившийся flow, чтобы на него можно было подписаться несколько раз
            .cachedIn(viewModelScope)
    }

    fun refresh() {
        this.filters.postValue(Filters(query="wefwef"))
    }

}