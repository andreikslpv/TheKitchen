package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.CategoryRepository
import com.andreikslpv.thekitchen.domain.UserRepository
import com.andreikslpv.thekitchen.domain.models.Category
import com.andreikslpv.thekitchen.domain.models.Filters
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.andreikslpv.thekitchen.domain.usecases.GetRecipePreviewUseCase
import com.andreikslpv.thekitchen.domain.usecases.RemoveFilterUseCase
import com.andreikslpv.thekitchen.domain.usecases.SetDefaultExcludeFromDbUseCase
import com.andreikslpv.thekitchen.domain.usecases.TryToChangeFavoritesStatusUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogViewModel : ViewModel() {

    @Inject
    lateinit var categoryRepository: CategoryRepository

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var getRecipePreviewUseCase: GetRecipePreviewUseCase

    @Inject
    lateinit var tryToChangeFavoritesStatusUseCase: TryToChangeFavoritesStatusUseCase

    @Inject
    lateinit var removeFilterUseCase: RemoveFilterUseCase

    @Inject
    lateinit var setDefaultExcludeFromDbUseCase: SetDefaultExcludeFromDbUseCase

    val recipes: Flow<PagingData<RecipePreview>>

    private val _filters = MutableLiveData(Filters())
    val filters: LiveData<Filters> = _filters

    init {
        App.instance.dagger.inject(this)

        recipes = _filters
            .asFlow()
            .flatMapLatest { getRecipePreviewUseCase.execute(it) }
            // кешируем прлучившийся flow, чтобы на него можно было подписаться несколько раз
            .cachedIn(viewModelScope)

        setDefaultExcludeFromDbUseCase.execute()

        // начинаем отслеживать список установленных фильтров
        CoroutineScope(Dispatchers.IO).launch {
            categoryRepository.getFilters().collect { response ->
                _filters.postValue(response)
            }
        }

    }

    fun getCategoryById(id: String): Category? {
        return categoryRepository.getCategoryById(id)
    }

    fun removeFilter(categoryId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            categoryRepository.removeFilter(categoryId)
        }
        refresh()
        CoroutineScope(Dispatchers.IO).launch{
            removeFilterUseCase.execute(categoryId)
        }
    }

    fun refresh() {
        _filters.postValue(_filters.value)
    }

    fun tryToChangeFavoritesStatus(recipeId: String): Boolean {
        return tryToChangeFavoritesStatusUseCase.execute(recipeId)
    }

}